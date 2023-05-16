package me.deftware.mixin.mixins.game;

import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.minecraft.UserApiService;
import com.mojang.blaze3d.systems.RenderSystem;
import lombok.Getter;
import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.event.events.EventScreen;
import me.deftware.client.framework.event.events.EventWorldLoad;
import me.deftware.client.framework.gui.screens.GenericScreen;
import me.deftware.client.framework.gui.screens.MinecraftScreen;
import me.deftware.client.framework.minecraft.Chat;
import me.deftware.client.framework.minecraft.GameSetting;
import me.deftware.client.framework.minecraft.Minecraft;
import me.deftware.client.framework.minecraft.ServerDetails;
import me.deftware.client.framework.render.WorldEntityRenderer;
import me.deftware.client.framework.render.camera.GameCamera;
import me.deftware.client.framework.session.AccountSession;
import me.deftware.client.framework.util.minecraft.BlockSwingResult;
import me.deftware.client.framework.world.ClientWorld;
import me.deftware.client.framework.world.WorldTimer;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.ProfileKeys;
import net.minecraft.client.util.Session;
import net.minecraft.client.util.Window;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.server.SaveLoader;
import net.minecraft.util.ModStatus;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.level.storage.LevelStorage;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraft implements Minecraft {

    @Getter
    @Unique
    private ServerDetails lastConnectedServer;

    @Unique
    private static BlockSwingResult swingResult;

    @Getter
    @Unique
    private final List<Function<List<String>, List<String>>> debugModifiers = new ArrayList<>();

    @Shadow
    @Final
    private RenderTickCounter renderTickCounter;

    @Mutable
    @Shadow
    @Final
    private Session session;

    @Mutable
    @Shadow
    @Final
    private MinecraftSessionService sessionService;

    @Mutable
    @Shadow
    @Final
    private UserApiService userApiService;

    @Shadow
    private static int currentFps;

    @Shadow
    private int itemUseCooldown;

    @Shadow
    protected abstract boolean doAttack();

    @Shadow
    protected abstract void doItemUse();

    @Shadow
    protected abstract void doItemPick();

    @Shadow
    @Final
    private static Logger LOGGER;

    @Shadow
    @Mutable
    @Final
    private ProfileKeys profileKeys;

    @Shadow
    @Final
    public File runDirectory;

    @Unique
    private String worldName;

    @Inject(method = "startIntegratedServer", at = @At("HEAD"))
    private void onIntegratedServer(String levelName, LevelStorage.Session session, ResourcePackManager dataPackManager, SaveLoader saveLoader, boolean bl, CallbackInfo ci) {
        this.worldName = levelName;
    }

    @Override
    public String _getWorldName() {
        return worldName;
    }

    @Override
    public GameCamera getCamera() {
        return (GameCamera) ((MinecraftClient) (Object) this).getEntityRenderDispatcher().camera;
    }

    @Nullable
    @Override
    public ClientWorld getClientWorld() {
        return (ClientWorld) ((MinecraftClient) (Object) this).world;
    }

    @Override
    public WorldTimer getWorldTimer() {
        return (WorldTimer) renderTickCounter;
    }

    @Nullable
    @Override
    public ServerDetails getConnectedServer() {
        return (ServerDetails) ((MinecraftClient) (Object) this).getCurrentServerEntry();
    }

    @Override
    public void openScreen(GenericScreen screen) {
        ((MinecraftClient) (Object) this).setScreen((Screen) screen);
    }

    @Nullable
    @Override
    public MinecraftScreen getScreen() {
        return (MinecraftScreen) ((MinecraftClient) (Object) this).currentScreen;
    }

    @Override
    public boolean _isOnRealms() {
        return ((MinecraftClient) (Object) this).isConnectedToRealms();
    }

    @Override
    public boolean _isSinglePlayer() {
        return ((MinecraftClient) (Object) this).isInSingleplayer();
    }

    @Override
    public boolean isMouseOver() {
        return ((MinecraftClient) (Object) this).crosshairTarget != null;
    }

    @Override
    public void runOnRenderThread(Runnable runnable) {
        RenderSystem.recordRenderCall(runnable::run);
    }

    @Override
    public int getFPS() {
        return currentFps;
    }

    @Override
    public void shutdown() {
        ((MinecraftClient) (Object) this).stop();
    }

    @Override
    public Session getSession() {
        return this.session;
    }

    @Override
    public void setSession(AccountSession session) {
        this.session = session.getSession();
        try {
            this.sessionService = session.getSessionService();
            this.userApiService = session.getAuthenticationService().createUserApiService(this.session.getAccessToken());
            this.profileKeys = ProfileKeys.create(this.userApiService, this.session, this.runDirectory.toPath()); // TODO: Verify this
        } catch (Exception ex) {
            this.userApiService = UserApiService.OFFLINE;
            LOGGER.error("Failed to authenticate session", ex);
        }
    }

    @Override
    public BlockSwingResult getHitBlock() {
        if (MinecraftClient.getInstance().crosshairTarget instanceof BlockHitResult blockHitResult) {
            if (swingResult == null)
                swingResult = new BlockSwingResult(blockHitResult);
            else
                swingResult.setReference(blockHitResult);
            return swingResult;
        }
        return null;
    }

    @Override
    public Entity getHitEntity() {
        if (MinecraftClient.getInstance().crosshairTarget instanceof EntityHitResult entityHitResult) {
            return ClientWorld.getClientWorld().getEntityByReference(
                    entityHitResult.getEntity()
            );
        }
        return null;
    }

    @Override
    public void setRightClickDelayTimer(int delay) {
        this.itemUseCooldown = delay;
    }

    @Override
    public void doClickMouse() {
        doAttack();
    }

    @Override
    public void doRightClickMouse() {
        doItemUse();
    }

    @Override
    public void doMiddleClickMouse() {
        doItemPick();
    }

    @Inject(method = "getModStatus", at = @At(value = "TAIL"), cancellable = true)
    private static void isModdedCheck(CallbackInfoReturnable<ModStatus> cir) {
        cir.setReturnValue(
                new ModStatus(ModStatus.Confidence.PROBABLY_NOT, "Client jar signature and brand is untouched")
        );
    }

    @Inject(method = "getVersionType", at = @At("HEAD"), cancellable = true)
    private void onGetVersionType(CallbackInfoReturnable<String> cir) {
        cir.setReturnValue("release");
    }

    @Inject(method = "getGameVersion", at = @At("TAIL"), cancellable = true)
    private void onGetGameVersion(CallbackInfoReturnable<String> cir) {
        cir.setReturnValue(SharedConstants.getGameVersion().getName());
    }

    @Redirect(method = "tick", at = @At(value = "FIELD", target = "Lnet/minecraft/client/option/GameOptions;debugEnabled:Z"))
    private boolean onScreenTick(GameOptions options) {
        if (((MinecraftClient) (Object) this).currentScreen instanceof MinecraftScreen screen) {
            screen.getEventScreen().setType(EventScreen.Type.Tick).broadcast();
        }
        return options.debugEnabled;
    }

    @Override
    public WorldEntityRenderer getWorldEntityRenderer() {
        return (WorldEntityRenderer) ((MinecraftClient) (Object) this).worldRenderer;
    }

    @Inject(method = "setWorld", at = @At("TAIL"))
    private void onSetWorld(net.minecraft.client.world.ClientWorld world, CallbackInfo ci) {
        new EventWorldLoad((ClientWorld) world).broadcast();
    }

    @Redirect(method = "getFramerateLimit", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/Window;getFramerateLimit()I"))
    private int onGetMaxFps(Window instance) {
        return GameSetting.MAX_FPS.get();
    }

    @Override
    public Chat getChatSender() {
        return (Chat) ((MinecraftClient) (Object) this).player;
    }

    @Unique
    @Override
    public void setLastConnected(ServerDetails details) {
        this.lastConnectedServer = details;
    }

}
