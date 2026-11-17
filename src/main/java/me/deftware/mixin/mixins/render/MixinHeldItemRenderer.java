package me.deftware.mixin.mixins.render;

import me.deftware.client.framework.math.Vector3;
import net.minecraft.client.renderer.FirstPersonHandsAndItemsRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.FirstPersonHandsAndItemsRenderState;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.MapDecorations;
import me.deftware.client.framework.event.events.EventStructureLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Map;

@Mixin(FirstPersonHandsAndItemsRenderer.class)
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
    
    @Inject(method = "renderMap", at = @At("HEAD"))
    private void renderFirstPersonMap(PoseStack matrices, SubmitNodeCollector submitNodeCollector, int swingProgress,
        ItemStack stack, boolean bl, FirstPersonHandsAndItemsRenderState state, CallbackInfo info) {
        if (copiedStack != null && ItemStack.matches(copiedStack, stack)) {
            return;
        }
        copiedStack = stack.copy();
        var data = stack.get(DataComponents.MAP_DECORATIONS);
        if (data != null) {
            for (Map.Entry<String, MapDecorations.Entry> entry : data.decorations().entrySet()) {
                var structure = getStructure(entry.getKey());
                var decoration = entry.getValue();
                new EventStructureLocation(
                        Vector3.ofDouble(decoration.x(), 0d, decoration.z()), structure
                ).broadcast();
            }
        }
    }
}
