package me.deftware.mixin.mixins.render;

import me.deftware.client.framework.math.Vector3;
import me.deftware.client.framework.event.events.EventStructureLocation;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.MapDecorationsComponent;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(HeldItemRenderer.class)
public class MixinHeldItemRenderer {

    @Shadow @Final private EntityRenderDispatcher entityRenderDispatcher;
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
    
    @Inject(method = "renderFirstPersonMap", at = @At("HEAD"))
    private void renderFirstPersonMap(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int swingProgress,
        ItemStack stack, CallbackInfo info) {
        if (copiedStack != null && ItemStack.areEqual(copiedStack, stack)) {
            return;
        }
        copiedStack = stack.copy();
        var data = stack.get(DataComponentTypes.MAP_DECORATIONS);
        if (data != null) {
            for (Map.Entry<String, MapDecorationsComponent.Decoration> entry : data.decorations().entrySet()) {
                var structure = getStructure(entry.getKey());
                var decoration = entry.getValue();
                new EventStructureLocation(
                        Vector3.ofDouble(decoration.x(), 0d, decoration.z()), structure
                ).broadcast();
            }
        }
    }
}
