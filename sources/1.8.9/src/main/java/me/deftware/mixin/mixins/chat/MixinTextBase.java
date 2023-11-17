package me.deftware.mixin.mixins.chat;

import net.minecraft.util.ChatComponentStyle;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@SuppressWarnings("OverwriteAuthorRequired")
@Mixin(ChatComponentStyle.class)
public class MixinTextBase {

    @Shadow
    private ChatStyle style;

    @Shadow
    protected List<IChatComponent> siblings;

    @Overwrite
    public int hashCode() {
        int hash = siblings.hashCode();
        if (style != null) {
            hash += 31 * style.hashCode();
        }
        return hash;
    }

}
