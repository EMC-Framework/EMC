package me.deftware.client.framework.render.texture;

import net.minecraft.client.renderer.texture.DynamicTexture;
import org.lwjgl.opengl.GL11;

import java.awt.image.BufferedImage;

import static org.lwjgl.opengl.GL11.*;

public class Texture {
    int width;
    int height;
    DynamicTexture dynamicTexture;
    BufferedImage bufferedImage;

    public Texture(int width, int height, boolean clear) {
        //System.out.println("Warning: clear bool unsupported in this version of MC!");
        this.width = width;
        this.height = height;
        this.bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        this.dynamicTexture = new DynamicTexture(bufferedImage);
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void refreshParameters() {
        this.width = bufferedImage.getWidth();
        this.height = bufferedImage.getHeight();
    }

    public int fillFromBufferedImage(BufferedImage img) {
        try {
            this.bufferedImage = img;
            this.refreshParameters();
        } catch (Exception e) {
            e.printStackTrace();
            return 2;
        }
        return 0;
    }

    public int fillFromBufferedImageFlip(BufferedImage img) {
        try {
            this.bufferedImage = img;
            this.refreshParameters();
        } catch (Exception e) {
            e.printStackTrace();
            return 2;
        }
        return 0;
    }

    public int fillFromBufferedFormatImage(BufferedImage img, int pixelFormat) {
        //byte[] imageBytes = ((DataBufferByte) img.getData().getDataBuffer()).getData();
        try {
            this.bufferedImage = img;
            this.dynamicTexture = new DynamicTexture(bufferedImage);
            this.refreshParameters();
        } catch (Exception e) {
            e.printStackTrace();
            return 2;
        }
        return 0;
    }

    public int fillFromBufferedRGBImage(BufferedImage img) {
        return fillFromBufferedFormatImage(img, BufferedImage.TYPE_INT_ARGB);
    }


    public int fillFromBufferedRGBAImage(BufferedImage img) {
        return fillFromBufferedFormatImage(img, BufferedImage.TYPE_INT_ARGB);
    }

    public int clearPixels() {
        try {
            for (int x = 0; x < bufferedImage.getWidth(); x++) {
                for (int y = 0; y < bufferedImage.getHeight(); y++) {
                    int rgb = ((0xFF) << 24) |
                            ((0xFF) << 16) |
                            ((0xFF) << 8) |
                            ((0xFF));
                    this.setPixel(x, y, rgb);
                }
            }
            this.dynamicTexture = new DynamicTexture(bufferedImage);
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
        return 0;
    }


    public void setPixel(int x, int y, int red, int green, int blue) {
        int rgb = ((red & 0xFF) << 16) |
                ((green & 0xFF) << 8) |
                ((blue & 0xFF));
        this.bufferedImage.setRGB(x, y, rgb);
    }

    public void setPixel(int x, int y, int red, int green, int blue, int alpha) {
        int rgba = ((alpha & 0xFF) << 24) |
                ((red & 0xFF) << 16) |
                ((green & 0xFF) << 8) |
                ((blue & 0xFF));
        this.bufferedImage.setRGB(x, y, rgba);
    }

    public void setPixel(int x, int y, int rgba) {
        this.bufferedImage.setRGB(x, y, rgba);
    }

    public int getPixel(int x, int y) {
        return this.bufferedImage.getRGB(x, y);
    }

    public byte getAlpha(int x, int y) {
        return (byte) this.bufferedImage.getTransparency();
    }

    public int updatePixels() {
        this.dynamicTexture = new DynamicTexture(bufferedImage);
        return 0;
    }

    public int updateTexture() {
        this.dynamicTexture.updateDynamicTexture();
        return 0;
    }

    public int update() {
        int errorCode = 0;
        errorCode += this.updateTexture();
        this.refreshParameters();
        errorCode += this.updatePixels();
        return errorCode;
    }

    public void bind() {
        this.bind(GL_ONE);
    }


    public void bind(int blendfunc) {
        GL11.glEnable(GL_BLEND);
        GL11.glBlendFunc(GL_SRC_ALPHA, blendfunc);
        this.blindBind();
    }


    public void blindBind() {
        this.dynamicTexture.updateDynamicTexture();
    }


    public void unbind() {
        if (GL11.glIsEnabled(GL_BLEND))
            GL11.glDisable(GL_BLEND);
    }

    public void destroy() {
        bufferedImage.flush();
        bufferedImage = null;
        dynamicTexture.deleteGlTexture();
        dynamicTexture = null;
    }

}
