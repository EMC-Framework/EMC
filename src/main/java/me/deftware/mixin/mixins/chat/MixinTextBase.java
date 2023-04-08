package me.deftware.mixin.mixins.chat;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@SuppressWarnings("OverwriteAuthorRequired")
@Mixin(TextComponentBase.class)
public class MixinTextBase {

    @Shadow
    private Style style;

    @Shadow
    protected List<ITextComponent> siblings;

    @Overwrite
    public int hashCode() {
        int hash = siblings.hashCode();
        if (style != null) {
            hash += 31 * style.hashCode();
        }
        return hash;
    }

}
