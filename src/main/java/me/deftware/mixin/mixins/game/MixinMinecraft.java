package me.deftware.mixin.mixins.game;

import com.mojang.authlib.minecraft.MinecraftSessionService;
import me.deftware.client.framework.event.events.EventKeyAction;
import me.deftware.client.framework.event.events.EventMouseClick;
import me.deftware.client.framework.gui.screens.GenericScreen;
import me.deftware.client.framework.main.EMCMod;
import me.deftware.client.framework.main.bootstrap.Bootstrap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.util.Session;
import net.minecraft.util.Timer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import me.deftware.client.framework.entity.Entity;
import net.minecraft.util.math.RayTraceResult;
import org.spongepowered.asm.mixin.*;
import me.deftware.client.framework.chat.hud.ChatHud;
import me.deftware.client.framework.event.events.EventScreen;
import me.deftware.client.framework.gui.screens.MinecraftScreen;
import me.deftware.client.framework.minecraft.ClientOptions;
import me.deftware.client.framework.minecraft.ServerDetails;
import me.deftware.client.framework.render.camera.GameCamera;
import me.deftware.client.framework.util.minecraft.BlockSwingResult;
import me.deftware.client.framework.world.ClientWorld;
import me.deftware.client.framework.world.WorldTimer;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft implements me.deftware.client.framework.minecraft.Minecraft {

    @Unique
    private ServerDetails lastConnectedServer;

    @Unique
    private final List<Function<List<String>, List<String>>> debugModifiers = new ArrayList<>();

    @Shadow
    @Final
    private Timer timer;

    @Shadow
    public GuiScreen currentScreen;

    @Mutable
    @Shadow
    @Final
    private Session session;

    @Mutable
    @Shadow
    @Final
    private MinecraftSessionService sessionService;

    @Shadow
    private static int debugFPS;

    @Shadow
    private int rightClickDelayTimer;

    @Shadow
    protected abstract void rightClickMouse();

    @Shadow
    protected abstract void clickMouse();

    @Shadow
    protected abstract void middleClickMouse();

    @Override
    public GameCamera getCamera() {
        return (GameCamera) ((Minecraft) (Object) this).getRenderManager();
    }

    @Nullable
    @Override
    public ClientWorld getClientWorld() {
        return (ClientWorld) ((Minecraft) (Object) this).world;
    }

    @Override
    public WorldTimer getWorldTimer() {
        return (WorldTimer) timer;
    }

    @Override
    public ClientOptions getClientOptions() {
        return (ClientOptions) ((Minecraft) (Object) this).gameSettings;
    }

    @Nullable
    @Override
    public ServerDetails getConnectedServer() {
        return (ServerDetails) ((Minecraft) (Object) this).getCurrentServerData();
    }

    @Override
    public void openScreen(GenericScreen screen) {
        ((Minecraft) (Object) this).displayGuiScreen((GuiScreen) screen);
    }

    @Nullable
    @Override
    public MinecraftScreen getScreen() {
        return (MinecraftScreen) ((Minecraft) (Object) this).currentScreen;
    }

    @Override
    public boolean _isOnRealms() {
        return ((Minecraft) (Object) this).isConnectedToRealms();
    }

    @Override
    public boolean _isSinglePlayer() {
        return ((Minecraft) (Object) this).isSingleplayer();
    }

    @Override
    public boolean isMouseOver() {
        return ((Minecraft) (Object) this).objectMouseOver != null;
    }

    @Override
    public void runOnRenderThread(Runnable runnable) {
        ChatHud.getChatMessageQueue().add(runnable);
    }

    @Override
    public int getFPS() {
        return debugFPS;
    }

    @Override
    public void shutdown() {
        ((Minecraft) (Object) this).shutdownMinecraftApplet();
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
        if (Minecraft.getMinecraft().objectMouseOver != null && Minecraft.getMinecraft().objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
            return new BlockSwingResult(Minecraft.getMinecraft().objectMouseOver);
        }
        return null;
    }

    @Override
    public Entity getHitEntity() {
        if (Minecraft.getMinecraft().objectMouseOver != null && Minecraft.getMinecraft().objectMouseOver.typeOfHit == RayTraceResult.Type.ENTITY) {
            return ClientWorld.getClientWorld().getEntityByReference(
                    Minecraft.getMinecraft().objectMouseOver.entityHit
            );
        }
        return null;
    }

    @Inject(method = "setServerData", at = @At("HEAD"))
    private void onServerConnectionSet(ServerData serverEntry, CallbackInfo ci) {
        if (serverEntry != null)
            this.lastConnectedServer = (ServerDetails) serverEntry;
    }

    @Override
    public void setRightClickDelayTimer(int delay) {
        this.rightClickDelayTimer = delay;
    }

    @Override
    public void doClickMouse() {
        clickMouse();
    }

    @Override
    public void doRightClickMouse() {
        rightClickMouse();
    }

    @Override
    public void doMiddleClickMouse() {
        middleClickMouse();
    }

    @Redirect(method = "runTick", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/GuiScreen;allowUserInput:Z"))
    private boolean onScreenTick(GuiScreen self) {
        if (self instanceof MinecraftScreen) {
            ((MinecraftScreen) self).getEventScreen().setType(EventScreen.Type.Tick).broadcast();
        }
        return self.allowUserInput;
    }

    @Inject(method = "init()V", at = @At("TAIL"))
    private void init(CallbackInfo ci) {
        if (!Bootstrap.initialized) {
            Bootstrap.initialized = true;
            Bootstrap.init();
            Bootstrap.getMods().values().forEach(EMCMod::postInit);
        }
    }

    @Override
    public ServerDetails getLastConnectedServer() {
        return lastConnectedServer;
    }

    @Override
    public List<Function<List<String>, List<String>>> getDebugModifiers() {
        return debugModifiers;
    }

    @Inject(method = "runTickKeyboard", at = @At(value = "INVOKE_ASSIGN", target = "org/lwjgl/input/Keyboard.getEventKeyState()Z", remap = false))
    private void onKeyEvent(CallbackInfo ci) {
        if (Keyboard.getEventKeyState()) {
            new EventKeyAction(Keyboard.getEventKey()).broadcast();
        }
    }

    @Inject(method = "runTickMouse", at = @At(value = "INVOKE_ASSIGN", target = "org/lwjgl/input/Mouse.getEventButton()I", remap = false))
    private void onMouseEvent(CallbackInfo info) {
        if (currentScreen != null) {
            return;
        }
        if (((Minecraft) (Object) this).inGameHasFocus && Mouse.getEventButtonState())
            new EventKeyAction(Mouse.getEventButton()).broadcast();
        new EventMouseClick(Mouse.getEventButton(), Mouse.getEventButtonState()).broadcast();
    }

    @Inject(method = "getLimitFramerate", at = @At("HEAD"), cancellable = true)
    public void adjustLimitFramerate(CallbackInfoReturnable<Integer> cir) {
        // Update menu fps to 60 to match > 1.13.2 mc versions
        if (((Minecraft) (Object) this).world == null && this.currentScreen != null) {
            cir.setReturnValue(60);
        }
    }

}
