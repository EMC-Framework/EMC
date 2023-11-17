package me.deftware.mixin.mixins.chat;

import me.deftware.client.framework.message.Appearance;
import net.minecraft.util.text.Style;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(Style.class)
public class MixinStyle implements Appearance {

    @Shadow
    private Boolean italic;

    @Shadow
    private Boolean bold;

    @Shadow
    private Boolean underlined;

    @Shadow
    private Boolean strikethrough;

    @Shadow
    private Boolean obfuscated;

    @Unique
    @Override
    public boolean isItalic() {
        return this.italic;
    }

    @Unique
    @Override
    public boolean isBold() {
        return this.bold;
    }

    @Unique
    @Override
    public boolean isUnderlined() {
        return this.underlined;
    }

    @Unique
    @Override
    public boolean isStrikethrough() {
        return this.strikethrough;
    }

    @Unique
    @Override
    public boolean isObfuscated() {
        return this.obfuscated;
    }

    @Unique
    private FormattingColor formattingColor;

    @Override
    public Optional<FormattingColor> getFormatting() {
        return Optional.ofNullable(formattingColor);
    }

    @Override
    public void setFormatting(FormattingColor color) {
        this.formattingColor = color;
    }

    @Inject(method = "createShallowCopy", at = @At("RETURN"))
    private void onCopy(CallbackInfoReturnable<Style> cir) {
        Appearance returned = (Appearance) cir.getReturnValue();
        returned.setFormatting(this.getFormatting().orElse(null));
    }

    @Inject(method = "createDeepCopy", at = @At("RETURN"))
    private void onDeepCopy(CallbackInfoReturnable<Style> cir) {
        Appearance returned = (Appearance) cir.getReturnValue();
        returned.setFormatting(this.getFormatting().orElse(null));
    }

}
