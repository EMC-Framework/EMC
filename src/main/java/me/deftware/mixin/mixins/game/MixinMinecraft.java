package me.deftware.mixin.mixins.game;

import com.mojang.authlib.minecraft.MinecraftSessionService;
import lombok.Getter;
import me.deftware.client.framework.chat.hud.ChatHud;
import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.event.events.EventScreen;
import me.deftware.client.framework.gui.screens.GenericScreen;
import me.deftware.client.framework.gui.screens.MinecraftScreen;
import me.deftware.client.framework.minecraft.ClientOptions;
import me.deftware.client.framework.minecraft.Minecraft;
import me.deftware.client.framework.minecraft.ServerDetails;
import me.deftware.client.framework.render.camera.GameCamera;
import me.deftware.client.framework.util.minecraft.BlockSwingResult;
import me.deftware.client.framework.world.ClientWorld;
import me.deftware.client.framework.world.WorldTimer;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.Session;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
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

    @Shadow
    private static int currentFps;

    @Shadow
    private int itemUseCooldown;

    @Shadow
    protected abstract void doAttack();

    @Shadow
    protected abstract void doItemUse();

    @Shadow
    protected abstract void doItemPick();

    @Override
    public GameCamera getCamera() {
        return (GameCamera) ((MinecraftClient) (Object) this).getEntityRenderManager().camera;
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

    @Override
    public ClientOptions getClientOptions() {
        return (ClientOptions) ((MinecraftClient) (Object) this).options;
    }

    @Nullable
    @Override
    public ServerDetails getConnectedServer() {
        return (ServerDetails) ((MinecraftClient) (Object) this).getCurrentServerEntry();
    }

    @Override
    public void openScreen(GenericScreen screen) {
        ((MinecraftClient) (Object) this).openScreen((Screen) screen);
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
        ChatHud.getChatMessageQueue().add(runnable);
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
    public void setSession(Session session) {
        this.session = session;
    }

    @Override
    public void setSessionService(MinecraftSessionService service) {
        this.sessionService = service;
    }

    @Override
    public BlockSwingResult getHitBlock() {
        if (MinecraftClient.getInstance().crosshairTarget instanceof BlockHitResult) {
            BlockHitResult blockHitResult = (BlockHitResult) MinecraftClient.getInstance().crosshairTarget;
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
        if (MinecraftClient.getInstance().crosshairTarget instanceof EntityHitResult) {
            return ClientWorld.getClientWorld().getEntityByReference(
                    ((EntityHitResult) MinecraftClient.getInstance().crosshairTarget).getEntity()
            );
        }
        return null;
    }

    @Inject(method = "setCurrentServerEntry", at = @At("HEAD"))
    private void onServerConnectionSet(ServerInfo serverEntry, CallbackInfo ci) {
        if (serverEntry != null)
            this.lastConnectedServer = (ServerDetails) serverEntry;
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

    @Inject(method = "getVersionType", at = @At("HEAD"), cancellable = true)
    private void onGetVersionType(CallbackInfoReturnable<String> cir) {
        cir.setReturnValue("release");
    }

    @Inject(method = "getGameVersion", at = @At("TAIL"), cancellable = true)
    private void onGetGameVersion(CallbackInfoReturnable<String> cir) {
        cir.setReturnValue(SharedConstants.getGameVersion().getName());
    }

    @Redirect(method = "tick", at = @At(value = "FIELD", target = "Lnet/minecraft/client/options/GameOptions;debugEnabled:Z"))
    private boolean onScreenTick(GameOptions options) {
        Screen screen = ((MinecraftClient) (Object) this).currentScreen;
        if (screen instanceof MinecraftScreen) {
            ((MinecraftScreen) screen).getEventScreen().setType(EventScreen.Type.Tick).broadcast();
        }
        return options.debugEnabled;
    }

}
