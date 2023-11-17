package me.deftware.mixin.mixins.gui;

import me.deftware.client.framework.minecraft.Minecraft;
import net.minecraft.util.ScreenShotHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

@Mixin(ScreenShotHelper.class)
public class MixinScreenShotHelper {

    @Redirect(method = "saveScreenshot(Ljava/io/File;Ljava/lang/String;IILnet/minecraft/client/shader/Framebuffer;)Lnet/minecraft/util/IChatComponent;",
            at = @At(value = "INVOKE", target = "Ljavax/imageio/ImageIO;write(Ljava/awt/image/RenderedImage;Ljava/lang/String;Ljava/io/File;)Z"))
    private static boolean onScreenshot(RenderedImage renderedImage, String s, File file) throws IOException {
        File custom = Minecraft.getMinecraftGame().getScreenshotFile();
        if (custom != null) {
            file = custom;
        }
        return ImageIO.write(renderedImage, s, file);
    }

}
