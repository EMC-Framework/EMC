package me.deftware.mixin.mixins.game;

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
import net.minecraft.client.Screenshot;
import net.minecraft.client.User;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ProfileKeyPairManager;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.server.Services;
import net.minecraft.server.WorldStem;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.util.ModCheck;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Mixin(net.minecraft.client.Minecraft.class)
public abstract class MixinMinecraft implements Minecraft {

    @Getter
    @Unique
    private ServerDetails lastConnectedServer;

    @Unique
    private static BlockSwingResult swingResult;

    @Getter
    @Unique
    private final List<Function<List<String>, List<String>>> debugModifiers = new ArrayList<>();

    @Mutable
    @Shadow
    @Final
    private User user;

    @Mutable
    @Shadow
    @Final
    private Services services;

    @Mutable
    @Shadow
    @Final
    private UserApiService userApiService;

    @Shadow
    private static int fps;

    @Shadow
    private int rightClickDelay;

    @Shadow
    protected abstract boolean startAttack();

    @Shadow
    protected abstract void startUseItem();

    @Shadow
    protected abstract void pickBlockOrEntity();

    @Shadow
    @Final
    private static Logger LOGGER;

    @Shadow
    @Mutable
    @Final
    private ProfileKeyPairManager profileKeyPairManager;

    @Shadow
    @Final
    public File gameDirectory;

    @Unique
    private String worldName;

    @Inject(method = "doWorldLoad", at = @At("HEAD"))
    private void onIntegratedServer(LevelStorageSource.LevelStorageAccess session, PackRepository resourcePackManager, WorldStem saveLoader, Optional<GameRules> gameRules, boolean bl, CallbackInfo ci) {
        this.worldName = saveLoader.worldDataAndGenSettings().data().getLevelName();
    }

    @Override
    public String _getWorldName() {
        return worldName;
    }

    @Override
    public GameCamera getCamera() {
        return (GameCamera) ((net.minecraft.client.Minecraft) (Object) this).getEntityRenderDispatcher().camera;
    }

    @Nullable
    @Override
    public ClientWorld getClientWorld() {
        return (ClientWorld) ((net.minecraft.client.Minecraft) (Object) this).level;
    }

    @Override
    public WorldTimer getWorldTimer() {
        return (WorldTimer) this;
    }

    @Nullable
    @Override
    public ServerDetails getConnectedServer() {
        return (ServerDetails) ((net.minecraft.client.Minecraft) (Object) this).getCurrentServer();
    }

    @Override
    public void openScreen(GenericScreen screen) {
        ((net.minecraft.client.Minecraft) (Object) this).gui.setScreen((Screen) screen);
    }

    @Nullable
    @Override
    public MinecraftScreen getScreen() {
        return (MinecraftScreen) ((net.minecraft.client.Minecraft) (Object) this).gui.screen();
    }

    @Override
    public boolean _isOnRealms() {
        ServerData serverInfo = ((net.minecraft.client.Minecraft) (Object) this).getCurrentServer();
        return serverInfo != null && serverInfo.isRealm();
    }

    @Override
    public boolean _isSinglePlayer() {
        return ((net.minecraft.client.Minecraft) (Object) this).isLocalServer();
    }

    @Override
    public boolean isMouseOver() {
        return ((net.minecraft.client.Minecraft) (Object) this).hitResult != null;
    }

    @Override
    public void runOnRenderThread(Runnable runnable) {
        if (runnable != null) {
            ((net.minecraft.client.Minecraft) (Object) this).execute(runnable);
        }
    }

    @Override
    public int getFPS() {
        return fps;
    }

    @Override
    public void shutdown() {
        ((net.minecraft.client.Minecraft) (Object) this).stop();
    }

    @Override
    public User getSession() {
        return this.user;
    }

    @Override
    public void setSession(AccountSession session) {
        this.user = session.getSession();
        try {
            this.services = new Services(
                    session.getSessionService(),
                    this.services.servicesKeySet(),
                    this.services.profileRepository(),
                    this.services.nameToIdCache(),
                    this.services.profileResolver()
            );
            this.userApiService = session.getDiscoveryService().createUserApiService(this.user.getAccessToken());
            this.profileKeyPairManager = ProfileKeyPairManager.create(this.userApiService, this.user, this.gameDirectory.toPath());
        } catch (Exception ex) {
            this.userApiService = UserApiService.OFFLINE;
            LOGGER.error("Failed to authenticate session", ex);
        }
    }

    @Override
    public BlockSwingResult getHitBlock() {
        if (net.minecraft.client.Minecraft.getInstance().hitResult instanceof BlockHitResult blockHitResult) {
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
        if (net.minecraft.client.Minecraft.getInstance().hitResult instanceof EntityHitResult entityHitResult) {
            return ClientWorld.getClientWorld().getEntityByReference(
                    entityHitResult.getEntity()
            );
        }
        return null;
    }

    @Override
    public void setRightClickDelayTimer(int delay) {
        this.rightClickDelay = delay;
    }

    @Override
    public void doClickMouse() {
        startAttack();
    }

    @Override
    public void doRightClickMouse() {
        startUseItem();
    }

    @Override
    public void doMiddleClickMouse() {
        pickBlockOrEntity();
    }

    @Inject(method = "checkModStatus", at = @At(value = "TAIL"), cancellable = true)
    private static void isModdedCheck(CallbackInfoReturnable<ModCheck> cir) {
        cir.setReturnValue(
                new ModCheck(ModCheck.Confidence.PROBABLY_NOT, "Client jar signature and brand is untouched")
        );
    }

    @Inject(method = "getLaunchedVersion", at = @At("TAIL"), cancellable = true)
    private void onGetGameVersion(CallbackInfoReturnable<String> cir) {
        cir.setReturnValue(SharedConstants.getCurrentVersion().name());
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;tick()V"))
    private void onScreenTick(CallbackInfo ci) {
        if (((net.minecraft.client.Minecraft) (Object) this).gui.screen() instanceof MinecraftScreen screen) {
            screen.getEventScreen().setType(EventScreen.Type.Tick).broadcast();
        }
    }

    @Override
    public WorldEntityRenderer getWorldEntityRenderer() {
        return (WorldEntityRenderer) ((net.minecraft.client.Minecraft) (Object) this).levelRenderer;
    }

    @Inject(method = "updateLevelInEngines(Lnet/minecraft/client/multiplayer/ClientLevel;)V", at = @At("TAIL"))
    private void onSetWorld(net.minecraft.client.multiplayer.ClientLevel world, CallbackInfo ci) {
        new EventWorldLoad((ClientWorld) world).broadcast();
    }

    @Override
    public Chat getChatSender() {
        return (Chat) ((net.minecraft.client.Minecraft) (Object) this).player;
    }

    @Unique
    @Override
    public void setLastConnected(ServerDetails details) {
        this.lastConnectedServer = details;
    }

    @Unique
    @Override
    public void screenshot(File file) throws IOException {
        Screenshot.takeScreenshot(((net.minecraft.client.Minecraft) (Object) this).gameRenderer.mainRenderTarget(), image -> {
            try {
                image.writeToFile(file);
            } catch (Exception ex) {
                LOGGER.error("Failed to write screenshot to {}", file.getName(), ex);
            }
        });
    }

    @Inject(method = "shouldEntityAppearGlowing", at = @At("HEAD"), cancellable = true)
    private void onShouldEntityAppearGlowing(net.minecraft.world.entity.Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (me.deftware.client.framework.render.shader.EntityShader.getMatchingOutlineColor(entity) != 0) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "close", at = @At("RETURN"))
    private void onClose(CallbackInfo ci) {
        // Aristois spawns non-daemon BlockESP Mesher worker threads that keep DestroyJavaVM
        // waiting until ClientShutdownWatchdog triggers; exit cleanly after close() finishes.
        System.exit(0);
    }

}
