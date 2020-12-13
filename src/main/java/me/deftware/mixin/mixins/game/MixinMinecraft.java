package me.deftware.mixin.mixins.game;

import com.mojang.authlib.minecraft.MinecraftSessionService;
import me.deftware.client.framework.event.events.EventGuiScreenDisplay;
import me.deftware.client.framework.event.events.EventKeyAction;
import me.deftware.client.framework.event.events.EventMouseClick;
import me.deftware.client.framework.event.events.EventShutdown;
import me.deftware.client.framework.main.EMCMod;
import me.deftware.client.framework.main.bootstrap.Bootstrap;
import me.deftware.mixin.imp.IMixinMinecraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.realms.RealmsSharedConstants;
import net.minecraft.util.Session;
import net.minecraft.util.Timer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft implements IMixinMinecraft {

    @Shadow
    private boolean inGameHasFocus;

    @Mutable
    @Shadow
    @Final
    private Session session;

    @Shadow
    @Final
    private Timer timer;

    @Shadow
    private GuiScreen currentScreen;

    @Shadow
    private int rightClickDelayTimer;

    @Shadow
    private static int debugFPS;

    @Shadow
    @Final
    @Mutable
    private MinecraftSessionService sessionService;

    @Override
    @Shadow
    public abstract void displayGuiScreen(@Nullable GuiScreen guiScreenIn);

    @Shadow
    public abstract void rightClickMouse();

    @Shadow
    public abstract void clickMouse();

    @Shadow
    public abstract void middleClickMouse();

    @Shadow public WorldClient world;

    @ModifyVariable(method = "displayGuiScreen", at = @At("HEAD"))
    private GuiScreen displayGuiScreenModifier(GuiScreen screen) {
        EventGuiScreenDisplay event = new EventGuiScreenDisplay(screen);
        event.broadcast();
        return event.isCanceled() ? currentScreen : event.getScreen();
    }

    @Override
    public void setSessionService(MinecraftSessionService service) {
        sessionService = service;
    }

    @Override
    public int getFPS() {
        return debugFPS;
    }

    @Inject(method = "init()V", at = @At("TAIL"))
    private void init(CallbackInfo ci) {
        if (!Bootstrap.initialized) {
            Bootstrap.initialized = true;
            Bootstrap.init();
            Bootstrap.getMods().values().forEach(EMCMod::postInit);
        }
    }

    @Inject(method = "runTick", at = @At("HEAD"))
    private void runTick(CallbackInfo ci) {
        if (Minecraft.getMinecraft().currentScreen instanceof GuiMainMenu) {
            EventGuiScreenDisplay event = new EventGuiScreenDisplay(Minecraft.getMinecraft().currentScreen);
            event.broadcast();
            if (!(event.getScreen() instanceof GuiMainMenu)) {
                displayGuiScreen(event.getScreen());
            }
        }
    }

    @Inject(method = "runTickMouse", at = @At(value = "INVOKE_ASSIGN", target = "org/lwjgl/input/Mouse.getEventButton()I", remap = false))
    private void onMouseEvent(CallbackInfo info) {
        if (currentScreen != null) {
            return;
        }
        if (inGameHasFocus && Mouse.getEventButtonState()) new EventKeyAction(Mouse.getEventButton()).broadcast();
        new EventMouseClick(Mouse.getEventButton(), Mouse.getEventButtonState()).broadcast();
    }

    @Inject(method = "runTickKeyboard", at = @At(value = "INVOKE_ASSIGN", target = "org/lwjgl/input/Keyboard.getEventKeyState()Z", remap = false))
    private void onKeyEvent(CallbackInfo ci) {
        if (Keyboard.getEventKeyState()) {
            new EventKeyAction(Keyboard.getEventKey()).broadcast();
        }
    }

    @Inject(method = "getVersionType", at = @At("HEAD"), cancellable = true)
    private void onGetVersionType(CallbackInfoReturnable<String> cir) {
        cir.setReturnValue("release");
    }

    @Inject(method = "getVersion", at = @At("TAIL"), cancellable = true)
    private void onGetGameVersion(CallbackInfoReturnable<String> cir) {
        cir.setReturnValue(RealmsSharedConstants.VERSION_STRING);
    }

    @Inject(method = "shutdownMinecraftApplet", at = @At("HEAD"))
    public void shutdownMinecraftApplet(CallbackInfo ci) {
        new EventShutdown().broadcast();
        Bootstrap.isRunning = false;
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

    @Override
    public Session getSession() {
        return session;
    }

    @Override
    public void setSession(Session session) {
        this.session = session;
    }

    @Override
    public Timer getTimer() {
        return timer;
    }

    @Override
    public boolean getIsWindowFocused() {
        return inGameHasFocus;
    }

    @Inject(method = "getLimitFramerate", at = @At("HEAD"), cancellable = true)
    public void adjustLimitFramerate(CallbackInfoReturnable<Integer> cir) {
        // Update menu fps to 60 to match > 1.13.2 mc versions
        if (world == null && this.currentScreen != null) {
            cir.setReturnValue(60);
        }
    }

}
