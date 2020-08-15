package me.deftware.client.framework.wrappers.gui;

import me.deftware.mixin.imp.IMixinGuiTextField;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class IGuiPasswordTextField extends IGuiTextField {

    private int enabledColor = 14737632;
    private int disabledColor = 7368816;

    public IGuiPasswordTextField(int componentId, int x, int y, int width, int height) {
        super(componentId, x, y, width, height);
    }

    @Override
    public void drawTextBox() {
        if (getVisible()) {
            if (getEnableBackgroundDrawing()) {
                Gui.drawRect(xPosition - 1, yPosition - 1, xPosition + getWidth() + 1,
                        yPosition + ((IMixinGuiTextField) this).getHeight() + 1, -6250336);
                Gui.drawRect(xPosition, yPosition, xPosition + getWidth(), yPosition + ((IMixinGuiTextField) this).getHeight(),
                        -16777216);
            }
            int var1 = getEnableBackgroundDrawing() ? enabledColor : disabledColor;
            int var2 = getCursorPosition() - ((IMixinGuiTextField) this).getLineScrollOffset();
            int var3 = getSelectionEnd() - ((IMixinGuiTextField) this).getLineScrollOffset();
            String text = getText().substring(((IMixinGuiTextField) this).getLineScrollOffset());
            StringBuilder hidden = new StringBuilder();
            for (int i = 0; i < text.length(); i++) {
                hidden.append("*");
            }
            String var4 = ((IMixinGuiTextField) this).getFontRendererInstance().trimStringToWidth(hidden.toString(), getWidth());
            boolean var5 = (var2 >= 0) && (var2 <= var4.length());
            boolean var6 = (isFocused()) && (((IMixinGuiTextField) this).getCursorCounter() / 6 % 2 == 0) && (var5);
            int var7 = getEnableBackgroundDrawing() ? xPosition + 4 : xPosition;
            int var8 = getEnableBackgroundDrawing() ? yPosition + (((IMixinGuiTextField) this).getHeight() - 8) / 2 : yPosition;
            int var9 = var7;
            if (var3 > var4.length()) {
                var3 = var4.length();
            }
            if (var4.length() > 0) {
                String var10 = var5 ? var4.substring(0, var2) : var4;
                var9 = ((IMixinGuiTextField) this).getFontRendererInstance().drawStringWithShadow(var10, var7, var8, var1);
            }
            boolean var13 = (getCursorPosition() < getText().length()) || (getText().length() >= getMaxStringLength());
            int var11 = var9;
            if (!var5) {
                var11 = var2 > 0 ? var7 + getWidth() : var7;
            } else if (var13) {
                var11 = var9 - 1;
                var9--;
            }
            if ((var4.length() > 0) && (var5) && (var2 < var4.length())) {
                var9 = ((IMixinGuiTextField) this).getFontRendererInstance().drawStringWithShadow(var4.substring(var2), var9, var8, var1);
            }
            if (var6) {
                if (var13) {
                    Gui.drawRect(var11, var8 - 1, var11 + 1, var8 + 1 + ((IMixinGuiTextField) this).getFontRendererInstance().FONT_HEIGHT,
                            -3092272);
                } else {
                    ((IMixinGuiTextField) this).getFontRendererInstance().drawStringWithShadow("_", var11, var8, var1);
                }
            }
            if (var3 != var2) {
                int var12 = var7 + ((IMixinGuiTextField) this).getFontRendererInstance().getStringWidth(var4.substring(0, var3));
                drawCursorVertical(var11, var8 - 1, var12 - 1, var8 + 1 + ((IMixinGuiTextField) this).getFontRendererInstance().FONT_HEIGHT);
            }
        }
    }

    protected void drawCursorVertical(int startX, int startY, int endX, int endY) {
        if (startX < endX) {
            int i = startX;
            startX = endX;
            endX = i;
        }

        if (startY < endY) {
            int j = startY;
            startY = endY;
            endY = j;
        }

        if (endX > xPosition + getWidth()) {
            endX = xPosition + getWidth();
        }

        if (startX > xPosition + getWidth()) {
            startX = xPosition + getWidth();
        }

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer VertexBuffer = tessellator.getWorldRenderer();
        GlStateManager.color(0.0F, 0.0F, 255.0F, 255.0F);
        GlStateManager.disableTexture2D();
        GlStateManager.enableColorLogic();
        GlStateManager.colorLogicOp(5387);
        VertexBuffer.begin(7, DefaultVertexFormats.POSITION);
        VertexBuffer.pos(startX, endY, 0.0D).endVertex();
        VertexBuffer.pos(endX, endY, 0.0D).endVertex();
        VertexBuffer.pos(endX, startY, 0.0D).endVertex();
        VertexBuffer.pos(startX, startY, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.disableColorLogic();
        GlStateManager.enableTexture2D();
    }

}
