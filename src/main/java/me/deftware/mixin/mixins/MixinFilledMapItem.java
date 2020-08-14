package me.deftware.mixin.mixins;

import me.deftware.client.framework.event.events.EventStructureLocation;
import me.deftware.client.framework.wrappers.world.IBlockPos;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.MapData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MapData.class)
public class MixinFilledMapItem {
    @Inject(method = "updateVisiblePlayers", at = @At("TAIL"))
    private void fillMap_after(EntityPlayer player, ItemStack stack, CallbackInfo ci) {
        NBTTagCompound compoundTag = stack.getTag();
        if (compoundTag != null && compoundTag.contains("Decorations", 9)) {
            // Try and Get Decoration X and Z
            EventStructureLocation event = new EventStructureLocation(new IBlockPos(compoundTag.getDouble("x"), 0, compoundTag.getDouble("z")), EventStructureLocation.StructureType.BuriedTreasure);
            event.broadcast();
        }
    }
}
