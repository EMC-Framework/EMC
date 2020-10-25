package me.deftware.client.framework.world;

import com.google.gson.Gson;
import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.entity.block.TileEntity;
import me.deftware.client.framework.maps.SettingsMap;
import me.deftware.client.framework.math.position.BlockPosition;
import me.deftware.client.framework.util.WebUtils;
import me.deftware.client.framework.world.block.Block;
import me.deftware.client.framework.world.block.BlockState;
import me.deftware.mixin.imp.IMixinWorld;
import me.deftware.mixin.imp.IMixinWorldClient;
import net.minecraft.block.BlockFlowingFluid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.IRegistry;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;
import java.util.UUID;
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
		return net.minecraft.client.Minecraft.getInstance().world != null;
	}

	public static Stream<TileEntity> getLoadedTileEntities() {
		return Objects.requireNonNull(((IMixinWorld) net.minecraft.client.Minecraft.getInstance().world)).getLoadedTilesAccessor().stream();
	}

	public static Stream<Entity> getLoadedEntities() {
		return Objects.requireNonNull(((IMixinWorldClient) net.minecraft.client.Minecraft.getInstance().world)).getLoadedEntitiesAccessor().values().stream();
	}

	public static int getDifficulty() {
		return Objects.requireNonNull(Minecraft.getInstance().world).getDifficulty().getId();
	}

	public static long getWorldTime() {
		return Objects.requireNonNull(net.minecraft.client.Minecraft.getInstance().world).getGameTime();
	}

	public static void sendQuittingPacket() {
		Objects.requireNonNull(net.minecraft.client.Minecraft.getInstance().world).sendQuittingDisconnectingPacket();
	}

	public static void leaveWorld() {
		net.minecraft.client.Minecraft.getInstance().loadWorld(null);
	}

	public static int getWorldHeight() {
		return Objects.requireNonNull(net.minecraft.client.Minecraft.getInstance().world).getHeight();
	}

	public static BlockState getStateFromBlockPos(BlockPosition position) {
		return new BlockState(Objects.requireNonNull(net.minecraft.client.Minecraft.getInstance().world).getBlockState(position.getMinecraftBlockPos()));
	}

	public static Block getBlockFromPosition(BlockPosition position) {
		BlockState blockState = getStateFromBlockPos(position);
		Block block = Block.newInstance(blockState.getMinecraftBlockState().getBlock());
		block.setBlockPosition(position);
		block.setLocationBlockState(blockState);
		return block;
	}

	public static void addEntityToWorld(int id, Entity entity) {
		Objects.requireNonNull(net.minecraft.client.Minecraft.getInstance().world).addEntityToWorld(id, entity.getMinecraftEntity());
	}

	public static void removeEntityFromWorld(int id) {
		Objects.requireNonNull(net.minecraft.client.Minecraft.getInstance().world).removeEntityFromWorld(id);
	}

	public static void determineRenderState(net.minecraft.block.state.BlockState state, BlockPos pos, CallbackInfoReturnable<Boolean> ci) {
		if (state.getBlock() instanceof BlockFlowingFluid) {
			ci.setReturnValue(((boolean) SettingsMap.getValue(SettingsMap.MapKeys.RENDER, "FLUIDS", true)));
		} else {
			int id = IRegistry.BLOCK.getId(state.getBlock());
			if (SettingsMap.isOverrideMode() || (SettingsMap.isOverwriteMode() && SettingsMap.hasValue(id, "render"))) {
				boolean doRender = (boolean) SettingsMap.getValue(id, "render", false);
				if (!doRender) {
					ci.setReturnValue(false);
				}
			}
		}
	}

	/**
	 * Which dimension the player is in (-1 = the Nether, 0 = normal world)
	 *
	 * @return The dimension
	 */
	public static int getDimension() {
		return Objects.requireNonNull(net.minecraft.client.Minecraft.getInstance().player).world.getDimension().getType().getId();
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
			NetHandlerPlayClient networkHandler = net.minecraft.client.Minecraft.getInstance().getConnection();
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

	private static class PlayerData {
		String id;
		String name;
	}

}
