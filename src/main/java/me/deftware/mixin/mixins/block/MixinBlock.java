package me.deftware.mixin.mixins.block;

import me.deftware.client.framework.event.events.EventSlowdown;
import me.deftware.client.framework.item.Item;
import me.deftware.client.framework.message.Message;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HoneyBlock;
import net.minecraft.world.level.block.IceBlock;
import net.minecraft.world.level.block.SoulSandBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public abstract class MixinBlock extends BlockBehaviour implements me.deftware.client.framework.world.block.Block {

    public MixinBlock(Properties settings) {
        super(settings);
    }

    @Unique
    private EventSlowdown eventSlowdown;

    @Unique
    private EventSlowdown slowdownEvent() {
        if (this.eventSlowdown == null) {
            this.eventSlowdown = new EventSlowdown();
        }
        return this.eventSlowdown;
    }

    @Inject(method = "getFriction", at = @At("TAIL"), cancellable = true)
    public void getSlipperiness(CallbackInfoReturnable<Float> cir) {
        if (friction != 0.6f) {
            Block block = Block.byItem(this.asItem());
            boolean flag = false;
            EventSlowdown slowdown = slowdownEvent();
            if (block instanceof IceBlock || block.getDescriptionId().contains("blue_ice") || block.getDescriptionId().contains("packed_ice")) {
                flag = true;
                slowdown.create(EventSlowdown.SlowdownType.Slipperiness, 0.6f);
            }
            if (flag) {
                slowdown.broadcast();
                if (slowdown.isCanceled()) {
                    cir.setReturnValue(slowdown.getMultiplier());
                }
            }
        }
    }

    @Inject(method = "getSpeedFactor", at = @At(value = "TAIL"), cancellable = true)
    private void onGetVelocityMultiplier(CallbackInfoReturnable<Float> cir) {
        if (speedFactor != 1.0f) {
            Block block = Block.byItem(this.asItem());
            boolean flag = false;
            EventSlowdown slowdown = slowdownEvent();
            if (block instanceof HoneyBlock) {
                flag = true;
                slowdown.create(EventSlowdown.SlowdownType.Honey, 1);
            } else if (block instanceof SoulSandBlock) {
                flag = true;
                slowdown.create(EventSlowdown.SlowdownType.Soulsand, 1);
            }
            if (flag) {
                slowdown.broadcast();
                if (slowdown.isCanceled()) {
                    cir.setReturnValue(slowdown.getMultiplier());
                }
            }
        }
    }

    @Unique
    @Override
    public String getIdentifierKey() {
        return BuiltInRegistries.BLOCK.getKey((Block) (Object) this).getPath();
    }

    @Unique
    @Override
    public Item getItem() {
        return (Item) this.asItem();
    }

    @Unique
    @Override
    public int getID() {
        return BuiltInRegistries.BLOCK.getId((Block) (Object) this);
    }

    @Unique
    @Override
    public Message getName() {
        return (Message) (Object) ((Block) (Object) this).getName();
    }

}
