package me.deftware.mixin.mixins.chat;

import me.deftware.client.framework.message.Appearance;
import net.minecraft.text.Style;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Style.class)
public class MixinStyle implements Appearance {

    @Shadow
    @Final
    Boolean italic;

    @Shadow
    @Final
    Boolean bold;

    @Shadow
    @Final
    Boolean underlined;

    @Shadow
    @Final
    Boolean strikethrough;

    @Shadow
    @Final
    Boolean obfuscated;

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

}
