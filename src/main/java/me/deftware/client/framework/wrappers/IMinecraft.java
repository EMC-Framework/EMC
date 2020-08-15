package me.deftware.client.framework.wrappers;

import me.deftware.client.framework.wrappers.entity.IEntity;
import me.deftware.client.framework.wrappers.entity.IEntity.EntityType;
import me.deftware.client.framework.wrappers.gui.IGuiInventory;
import me.deftware.client.framework.wrappers.gui.IGuiScreen;
import me.deftware.client.framework.wrappers.gui.IScreens;
import me.deftware.client.framework.wrappers.world.IBlockPos;
import me.deftware.mixin.imp.IMixinMinecraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.main.Main;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.realms.RealmsSharedConstants;
import net.minecraft.util.Tuple;
import org.lwjgl.opengl.Display;

import javax.annotation.Nullable;
import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IMinecraft {

    public static IServerData lastServer = null;
    private static IServerData iServerCache = null;

    public static File getMinecraftFile() throws URISyntaxException {
        return new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
    }

    public static void clickMouse() {
        ((IMixinMinecraft) Minecraft.getMinecraft()).doClickMouse();
    }

    public synchronized static IServerData getCurrentServer() {
        if (Minecraft.getMinecraft().getCurrentServerData() == null) {
            return null;
        }
        if (iServerCache != null && Minecraft.getMinecraft().getCurrentServerData() != null) {
            if (iServerCache.serverIP.equals(Minecraft.getMinecraft().getCurrentServerData().serverIP)) {
                return iServerCache;
            }
        }
        ServerData sd = Minecraft.getMinecraft().getCurrentServerData();
        iServerCache = new IServerData(sd.serverName, sd.serverIP, sd.isOnLAN());
        iServerCache.gameVersion = sd.gameVersion;
        return iServerCache;
    }

    public static long getWindowHandle() {
        return 0;
    }

    public static String getRunningLocation() throws URISyntaxException {
        return new File(Minecraft.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath())
                .getParent();
    }

    public static boolean isFocused() {
        return Display.isActive();
    }

    public static double getScaleFactor() {
        return Minecraft.getMinecraft().gameSettings.guiScale;
    }

    public static IGuiScreen getIScreen() {
        if (Minecraft.getMinecraft().currentScreen != null) {
            if (Minecraft.getMinecraft().currentScreen instanceof IGuiScreen) {
                return (IGuiScreen) Minecraft.getMinecraft().currentScreen;
            }
        }
        return null;
    }

    public static float getRenderPartialTicks() {
        return 0;
    }

    public static void leaveServer() {
        Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C01PacketChatMessage(new String(new char[]{167})));
    }

    public static IBlockPos getBlockOver() {
        if (!IMinecraft.isMouseOver()) {
            return null;
        }
        if (Minecraft.getMinecraft().objectMouseOver.getBlockPos() != null) {
            return new IBlockPos(Minecraft.getMinecraft().objectMouseOver.getBlockPos());
        }
        return null;
    }

    public static IEntity getPointedEntity() {
        Entity pointedEntity = Minecraft.getMinecraft().pointedEntity;
        if ((pointedEntity != null) && ((pointedEntity instanceof EntityPlayer))) {
            return IEntity.fromEntity(pointedEntity);
        }
        return null;
    }

    public static boolean isEntityHit() {
        return Minecraft.getMinecraft().objectMouseOver.entityHit != null;
    }

    public static int getFPS() {
        return Minecraft.getDebugFPS();
    }

    public static boolean isInGame() {
        return Minecraft.getMinecraft().currentScreen == null;
    }

    public static void reloadRenderers() {
        Minecraft.getMinecraft().renderGlobal.loadRenderers();
    }

    public static void triggerGuiRenderer() {
        Minecraft.getMinecraft().entityRenderer.setupOverlayRendering();
    }

    public static void addEntityToWorld(int id, IEntity entity) {
        Minecraft.getMinecraft().theWorld.addEntityToWorld(id, entity.getEntity());
    }

    public static void removeEntityFromWorld(int id) {
        Minecraft.getMinecraft().theWorld.removeEntityFromWorld(id);
    }

    public static void connectToServer(IServerData server) {
        Minecraft.getMinecraft()
                .displayGuiScreen(new GuiConnecting(new GuiMultiplayer(null), Minecraft.getMinecraft(), server));
    }

    public static int thridPersonView() {
        return Minecraft.getMinecraft().gameSettings.thirdPersonView;
    }

    public static int getGuiScaleRaw() {
        return Minecraft.getMinecraft().gameSettings.guiScale;
    }

    public static int getGuiScale() {
        int factor =  Minecraft.getMinecraft().gameSettings.guiScale;
        if (factor == 0) {
            factor = 4;
        }
        return factor;
    }

    public static void setScaleFactor(int factor) {
        Minecraft.getMinecraft().gameSettings.guiScale = factor;
    }

    public static boolean isDebugInfoShown() {
        return Minecraft.getMinecraft().gameSettings.showDebugInfo;
    }

    public static IGuiScreen getCurrentScreen() {
        if (Minecraft.getMinecraft().currentScreen != null) {
            if (Minecraft.getMinecraft().currentScreen instanceof IGuiScreen) {
                return (IGuiScreen) Minecraft.getMinecraft().currentScreen;
            }
        }
        return null;
    }

    public static void setGuiScreen(IGuiScreen screen) {
        Minecraft.getMinecraft().displayGuiScreen(screen);
    }

    /**
     * For internal use only! Does NOT return a compatible {@link IGuiScreen} for use in EMC mods!
     *
     * @return Returns an instance of a class in the current classpath, however it does NOT return a current instance, but a new one.
     */
    @Nullable
    public static GuiScreen createScreenInstance(Object clazz, Tuple<Class<?>, Object>... constructorParameters) {
        try {
            Class<?> screenClass = clazz instanceof Class ? (Class<?>) clazz : Class.forName((String) clazz);
            List<Class<?>> paramList = new ArrayList<>();
            List<Object> targetList = new ArrayList<>();
            Arrays.stream(constructorParameters).forEach(c -> {
                paramList.add(c.getFirst());
                targetList.add(c.getSecond());
            });
            return (GuiScreen) screenClass.getConstructor(paramList.toArray(new Class<?>[paramList.size()]))
                    .newInstance(targetList.toArray(new Object[targetList.size()]));
        } catch (Exception ignored) { }
        return null;
    }

    public static void openInventory(IGuiInventory inventory) {
        Minecraft.getMinecraft().displayGuiScreen(inventory);
    }

    public static void setGuiScreenType(IScreens.ScreenType screen) {
        Minecraft.getMinecraft().displayGuiScreen(IScreens.translate(screen, null));
    }

    public static void shutdown() {
        Minecraft.getMinecraft().shutdown();
    }

    public static void setGamma(double value) {
        Minecraft.getMinecraft().gameSettings.gammaSetting = (float) value;
    }

    public static double getGamma() {
        return Minecraft.getMinecraft().gameSettings.gammaSetting;
    }

    public static void setRightClickDelayTimer(int delay) {
        ((IMixinMinecraft) Minecraft.getMinecraft()).setRightClickDelayTimer(delay);
    }

    public static boolean isChatOpen() {
        if (Minecraft.getMinecraft().currentScreen != null) {
            if (Minecraft.getMinecraft().currentScreen instanceof GuiChat) {
                return true;
            }
        }
        return false;
    }

    public static boolean isContainerOpen() {
        if (Minecraft.getMinecraft().currentScreen != null) {
            if (Minecraft.getMinecraft().currentScreen instanceof GuiContainer
                    && !(Minecraft.getMinecraft().currentScreen instanceof GuiInventory)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isInventoryOpen() {
        if (Minecraft.getMinecraft().currentScreen != null) {
            if (Minecraft.getMinecraft().currentScreen instanceof GuiContainer
                    && (Minecraft.getMinecraft().currentScreen instanceof GuiInventory
                    || Minecraft.getMinecraft().currentScreen instanceof GuiContainerCreative)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isChestOpen() {
        if (Minecraft.getMinecraft().thePlayer.openContainer != null) {
            if (Minecraft.getMinecraft().thePlayer.openContainer instanceof ContainerChest) {
                return true;
            }
        }
        return false;
    }

    public static String getMinecraftVersion() {
        return RealmsSharedConstants.VERSION_STRING;
    }

    public static int getMinecraftProtocolVersion() {
        return RealmsSharedConstants.NETWORK_PROTOCOL_VERSION;
    }

    public static boolean isMouseOver() {
        if (Minecraft.getMinecraft().objectMouseOver != null) {
            return true;
        }
        return false;
    }

    public static IEntity getHit() {
        if (!isMouseOver()) {
            return null;
        }
        return IEntity.fromEntity(Minecraft.getMinecraft().objectMouseOver.entityHit);
    }

    public static IEntity getRenderViewEntity() {
        if(Minecraft.getMinecraft().getRenderViewEntity() == null) {
            return null;
        }
        return IEntity.fromEntity(Minecraft.getMinecraft().getRenderViewEntity());
    }

    public static boolean entityHitInstanceOf(EntityType type) {
        if (!isMouseOver()) {
            return false;
        }
        if (type.equals(EntityType.ENTITY_LIVING_BASE)) {
            return Minecraft.getMinecraft().objectMouseOver.entityHit instanceof EntityLivingBase;
        }
        return false;
    }

}
