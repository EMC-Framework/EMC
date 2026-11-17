package me.deftware.mixin.mixins.world;

import me.deftware.client.framework.math.BlockPosition;
import me.deftware.client.framework.math.Vector3;
import me.deftware.client.framework.entity.block.TileEntity;
import me.deftware.client.framework.world.Biome;
import me.deftware.client.framework.world.chunk.ChunkAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.GenericMessageScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.stream.Stream;

/**
 * @author Deftware
 */
@Mixin(Level.class)
public class MixinWorld implements me.deftware.client.framework.world.World {

	@Unique
	private static final Biome _biome = new Biome();

	@Unique
	public HashMap<BlockEntity, TileEntity> emcTileEntities = new HashMap<>();

	@Unique
	public HashMap<Long, BlockEntity> longTileEntities = new HashMap<>();

	@Unique
	private HashMap<BlockEntity, TileEntity> emcTileEntitiesMap() {
		if (this.emcTileEntities == null) {
			this.emcTileEntities = new HashMap<>();
		}
		return this.emcTileEntities;
	}

	@Unique
	private HashMap<Long, BlockEntity> longTileEntitiesMap() {
		if (this.longTileEntities == null) {
			this.longTileEntities = new HashMap<>();
		}
		return this.longTileEntities;
	}

	@Inject(method = "addBlockEntityTicker", at = @At("HEAD"))
	public void addBlockEntityTicker(TickingBlockEntity blockEntityTickInvoker, CallbackInfo ci) {
		HashMap<Long, BlockEntity> pending = longTileEntitiesMap();
		if (pending.containsKey(blockEntityTickInvoker.getPos().asLong())) {
			BlockEntity entity = pending.remove(blockEntityTickInvoker.getPos().asLong());
			emcTileEntitiesMap().put(entity, TileEntity.newInstance(entity, blockEntityTickInvoker));
		}
	}

	@Redirect(method = "tickBlockEntities", at = @At(value = "INVOKE", target = "Ljava/util/Iterator;next()Ljava/lang/Object;", opcode = 180))
	protected Object tickBlockEntities(Iterator<TickingBlockEntity> iterator) {
		TickingBlockEntity ticker = iterator.next();
		if (ticker.isRemoved()) {
			emcTileEntitiesMap().values().removeIf(e -> e.getTicker().equals(ticker));
		}
		return ticker;
	}

	@Override
	public Stream<TileEntity> getLoadedTileEntities() {
		return emcTileEntitiesMap().values().stream();
	}

	@Override
	public int _getDifficulty() {
		return ((Level) (Object) this).getDifficulty().getId();
	}

	@Override
	public long _getWorldTime() {
		return ((Level) (Object) this).getDefaultClockTime();
	}

	@Override
	public int _getWorldHeight() {
		return ((Level) (Object) this).getHeight();
	}

	@Override
	public int _getBlockLightLevel(BlockPosition position) {
		return ((Level) (Object) this).getMaxLocalRawBrightness((BlockPos) position);
	}

	@Override
	public void _disconnect() {
		Minecraft mc = Minecraft.getInstance();
		boolean sp = mc.isLocalServer();
		if (((Object) this) instanceof net.minecraft.client.multiplayer.ClientLevel clientLevel) {
			clientLevel.disconnect(Component.translatable("disconnect.closed"));
		}
		if (sp) {
			mc.disconnect(new GenericMessageScreen(Component.translatable("menu.savingLevel")), false);
		} else {
			mc.disconnect(null, false);
		}
	}

	@Override
	public ChunkAccessor getChunk(int x, int z) {
		return (ChunkAccessor) ((Level) (Object) this).getChunk(x, z);
	}

	@Override
	public boolean _hasChunk(int x, int z) {
		return ((Level) (Object) this).hasChunk(x, z);
	}

	@Override
	public int _getDimension() {
		ResourceKey<Level> key = ((Level) (Object) this).dimension();
		if (Level.END.equals(key)) {
			return 1;
		} else if (Level.OVERWORLD.equals(key)) {
			return 0;
		} else if (Level.NETHER.equals(key)) {
			return -1;
		}
		return -2;
	}

	@Override
	public me.deftware.client.framework.world.block.BlockState _getBlockState(BlockPosition position) {
		return (me.deftware.client.framework.world.block.BlockState) ((Level) (Object) this).getBlockState((BlockPos) position);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends TileEntity> T getTileEntityByReference(BlockEntity reference) {
		if (reference != null) {
			return (T) emcTileEntitiesMap().get(reference);
		}
		return null;
	}

	@Override
	public HashMap<Long, BlockEntity> getInternalLongToBlockEntity() {
		return longTileEntitiesMap();
	}

	@Override
	public Biome _getBiome() {
		return _biome.setReference(
				((ClientLevel) (Object) this).getBiome(
						Minecraft.getInstance().player.blockPosition()
				)
		);
	}

	@Unique
	@Override
	public boolean rayTraceBlocks(Vector3<Double> start, Vector3<Double> end) {
		ClipContext context = new ClipContext(
				(Vec3) start, (Vec3) end,
				ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE,
				Minecraft.getInstance().getCameraEntity());
		return ((Level) (Object) this).clip(context).getType() != HitResult.Type.MISS;
	}

}
