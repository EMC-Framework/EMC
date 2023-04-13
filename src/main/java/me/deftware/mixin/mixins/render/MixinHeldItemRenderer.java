package me.deftware.mixin.mixins.render;

import me.deftware.client.framework.math.Vector3;
import me.deftware.client.framework.event.events.EventStructureLocation;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class MixinHeldItemRenderer {

    @Unique
    private static ItemStack copiedStack = null;

    @Unique
    private static EventStructureLocation.StructureType getStructure(String name) {
        if (name.equals("{\"translate\":\"filled_map.buried_treasure\"}")) {
            return EventStructureLocation.StructureType.BuriedTreasure;
        }
        if (name.equals("{\"translate\":\"filled_map.monument\"}")) {
            return EventStructureLocation.StructureType.OceanMonument;
        }
        if (name.equals("{\"translate\":\"filled_map.mansion\"}")) {
            return EventStructureLocation.StructureType.WoodlandMansion;
        }
        return EventStructureLocation.StructureType.OtherMapIcon;
    }
    
   // @Inject(method = "renderMapFirstPerson(Lnet/minecraft/item/ItemStack;)V", at = @At("HEAD"))
    //private void renderFirstPersonMap(ItemStack stack, CallbackInfo info) {
        /*if (copiedStack != null && ItemStack.areItemsEqual(copiedStack, stack)) return; FIXME
        copiedStack = stack.copy();
        NBTTagCompound compoundTag = stack.getTag();
        if (compoundTag != null && compoundTag.contains("Decorations", 9)) {
            // Try and Get Decoration X and Z
            String mapName = compoundTag.getCompound("display").getString("Name");
            final EventStructureLocation.StructureType structure = getStructure(mapName);
            NBTTagList icons = compoundTag.getList("Decorations", 10);
           
            icons.forEach((icon) -> {
                if (icon instanceof NBTTagCompound) {
                    EventStructureLocation event = new EventStructureLocation(
                        Vector3.ofDouble(((NBTTagCompound) icon).getDouble("x"), 0d,
                            ((NBTTagCompound) icon).getDouble("z")),
                        structure);
                    event.broadcast();
                }
            });
        }*/
   // }

}
