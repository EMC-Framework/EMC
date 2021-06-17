package me.deftware.client.framework.world;

import com.google.gson.Gson;
import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.entity.block.TileEntity;
import me.deftware.client.framework.global.GameKeys;
import me.deftware.client.framework.global.GameMap;
import me.deftware.client.framework.global.types.BlockProperty;
import me.deftware.client.framework.global.types.PropertyManager;
import me.deftware.client.framework.main.bootstrap.Bootstrap;
import me.deftware.client.framework.math.position.BlockPosition;
import me.deftware.client.framework.util.WebUtils;
import me.deftware.client.framework.world.block.Block;
import me.deftware.client.framework.world.block.BlockState;
import me.deftware.client.framework.world.player.PlayerEntry;
import me.deftware.mixin.imp.IMixinNetworkHandler;
import me.deftware.mixin.imp.IMixinWorld;
import me.deftware.mixin.imp.IMixinWorldClient;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

/**
 * @author Deftware, CDAGaming
 */
public class World {

	private static final float[] tickRates = new float[20];
	private static long timeLastTimeUpdate;
	private static int nextIndex = 0;

	public static boolean isLoaded() {
		return net.minecraft.client.Minecraft.getMinecraft().world != null;
	}

	public static Stream<TileEntity> getLoadedTileEntities() {
		return Objects.requireNonNull(((IMixinWorld) net.minecraft.client.Minecraft.getMinecraft().world)).getLoadedTilesAccessor().values().stream();
	}

	public static Stream<Entity> getLoadedEntities() {
		return Objects.requireNonNull(((IMixinWorldClient) net.minecraft.client.Minecraft.getMinecraft().world)).getLoadedEntitiesAccessor().values().stream();
	}

	public static Entity getEntityById(int id) {
		return Objects.requireNonNull(((IMixinWorldClient) Minecraft.getMinecraft().world)).getLoadedEntitiesAccessor().getOrDefault(id, null);
	}

	public static TileEntity getTileEntityFromEntity(net.minecraft.tileentity.TileEntity entity) {
		return Objects.requireNonNull(((IMixinWorld) net.minecraft.client.Minecraft.getMinecraft().world)).getLoadedTilesAccessor().get(entity);
	}

	public static int getDifficulty() {
		return Objects.requireNonNull(Minecraft.getMinecraft().world).getDifficulty().getId();
	}

	public static long getWorldTime() {
		return Objects.requireNonNull(net.minecraft.client.Minecraft.getMinecraft().world).getWorldTime();
	}

	public static void sendQuittingPacket() {
		Objects.requireNonNull(net.minecraft.client.Minecraft.getMinecraft().world).sendQuittingDisconnectingPacket();
	}

	public static void leaveWorld() {
		net.minecraft.client.Minecraft.getMinecraft().loadWorld(null);
	}

	public static int getWorldHeight() {
		return Objects.requireNonNull(net.minecraft.client.Minecraft.getMinecraft().world).getHeight();
	}

	public static int getBlockLightLevel(BlockPosition position) {
		return Objects.requireNonNull(Minecraft.getMinecraft().world).getLightFor(EnumSkyBlock.BLOCK, position.getMinecraftBlockPos());
	}

	public static BlockState getStateFromBlockPos(BlockPosition position) {
		return new BlockState(Objects.requireNonNull(net.minecraft.client.Minecraft.getMinecraft().world).getBlockState(position.getMinecraftBlockPos()));
	}

	public static Block getBlockFromPosition(BlockPosition position) {
		BlockState blockState = getStateFromBlockPos(position);
		Block block = Block.newInstance(blockState.getMinecraftBlockState().getBlock());
		block.setBlockPosition(position);
		block.setLocationBlockState(blockState);
		return block;
	}

	public static void addEntityToWorld(int id, Entity entity) {
		Objects.requireNonNull(net.minecraft.client.Minecraft.getMinecraft().world).addEntityToWorld(id, entity.getMinecraftEntity());
	}

	public static void removeEntityFromWorld(int id) {
		Objects.requireNonNull(net.minecraft.client.Minecraft.getMinecraft().world).removeEntityFromWorld(id);
	}

	public static void determineRenderState(IBlockState state, BlockPos pos, CallbackInfoReturnable<Boolean> ci) {
		if (state.getBlock() instanceof BlockLiquid) {
			ci.setReturnValue(
					GameMap.INSTANCE.get(GameKeys.RENDER_FLUIDS, true)
			);
		} else {
			PropertyManager<BlockProperty> blockProperties = Bootstrap.blockProperties;
			if (blockProperties.isActive()) {
				int id = net.minecraft.block.Block.REGISTRY.getIDForObject(state.getBlock());
				if (blockProperties.contains(id) && blockProperties.get(id).isRender())
					ci.setReturnValue(false);
			}
		}
	}

	/**
	 * Which dimension the player is in (-1 = the Nether, 0 = normal world)
	 *
	 * @return The dimension
	 */
	public static int getDimension() {
		return Objects.requireNonNull(net.minecraft.client.Minecraft.getMinecraft().player).dimension;
	}

	public static String getBiomeCategoryName() {
		net.minecraft.entity.Entity player = Objects.requireNonNull(Minecraft.getMinecraft().player);
		return player.world.getBiome(player.getPosition()).getBiomeName();
	}

	public static double getTPS() {
		if (isLoaded()) {
			float numTicks = 0.0F;
			float sumTickRates = 0.0F;
			for (float tickRate : tickRates) {
				if (tickRate > 0.0F) {
					sumTickRates += tickRate;
					numTicks += 1.0F;
				}
			}
			return MathHelper.clamp(sumTickRates / numTicks, 0.0F, 20.0F);
		} else {
			return 0.0d;
		}
	}

	public static void updateTime() {
		if (timeLastTimeUpdate != -1L) {
			float timeElapsed = (float) (System.currentTimeMillis() - timeLastTimeUpdate) / 1000.0F;
			tickRates[(nextIndex % tickRates.length)] = MathHelper.clamp(20.0F / timeElapsed, 0.0F, 20.0F);
			nextIndex += 1;
		}
		timeLastTimeUpdate = System.currentTimeMillis();
	}

	public static CompletableFuture<String> getUsernameFromUUID(UUID uuid) {
		return CompletableFuture.supplyAsync(() -> {
			// Check local cache first
			NetHandlerPlayClient networkHandler = net.minecraft.client.Minecraft.getMinecraft().getConnection();
			if (networkHandler != null) {
				NetworkPlayerInfo result = networkHandler.getPlayerInfo(uuid);
				if (result != null) {
					return result.getGameProfile().getName();
				}
			}
			// Rate limit: You can request the same profile once per minute, however you can send as many unique requests as you like.
			try {
				String response = WebUtils.get("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString());
				PlayerData playerData = new Gson().fromJson(response, PlayerData.class);
				return playerData != null && playerData.name != null ? playerData.name : null;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		});
	}

	public static Map<UUID, PlayerEntry> getPlayerList() {
		if (Minecraft.getMinecraft().getConnection() != null) {
			return ((IMixinNetworkHandler) Minecraft.getMinecraft().getConnection()).getPlayerEntryMap();
		}
		return null;
	}

	private static class PlayerData {
		String id;
		String name;
	}

}
