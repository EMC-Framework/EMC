package me.deftware.mixin.mixins.game;

import com.mojang.authlib.minecraft.MinecraftSessionService;
import me.deftware.client.framework.event.events.EventGuiScreenDisplay;
import me.deftware.client.framework.event.events.EventShutdown;
import me.deftware.client.framework.main.EMCMod;
import me.deftware.client.framework.main.bootstrap.Bootstrap;
import me.deftware.mixin.imp.IMixinMinecraft;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.realms.RealmsSharedConstants;
import net.minecraft.util.Session;
import net.minecraft.util.Timer;
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
    private boolean isWindowFocused;

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
            Bootstrap.getMods().values().forEach(EMCMod::postInit);
        }
    }

    @Inject(method = "runTick", at = @At("HEAD"))
    private void runTick(CallbackInfo ci) {
        if (Minecraft.getInstance().currentScreen instanceof GuiMainMenu) {
            EventGuiScreenDisplay event = new EventGuiScreenDisplay(Minecraft.getInstance().currentScreen);
            event.broadcast();
            if (!(event.getScreen() instanceof GuiMainMenu)) {
                displayGuiScreen(event.getScreen());
            }
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
    public MainWindow getMainWindow() {
        return Minecraft.getInstance().mainWindow;
    }

    @Override
    public boolean getIsWindowFocused() {
        return isWindowFocused;
    }

}
