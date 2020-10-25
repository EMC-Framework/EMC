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
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
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
		return net.minecraft.client.Minecraft.getMinecraft().theWorld != null;
	}

	public static Stream<TileEntity> getLoadedTileEntities() {
		return Objects.requireNonNull(((IMixinWorld) net.minecraft.client.Minecraft.getMinecraft().theWorld)).getLoadedTilesAccessor().stream();
	}

	public static Stream<Entity> getLoadedEntities() {
		return Objects.requireNonNull(((IMixinWorldClient) net.minecraft.client.Minecraft.getMinecraft().theWorld)).getLoadedEntitiesAccessor().values().stream();
	}

	public static int getDifficulty() {
		return Objects.requireNonNull(Minecraft.getMinecraft().world).getDifficulty().getId();
	}

	public static long getWorldTime() {
		return Objects.requireNonNull(net.minecraft.client.Minecraft.getMinecraft().theWorld).getWorldTime();
	}

	public static void sendQuittingPacket() {
		Objects.requireNonNull(net.minecraft.client.Minecraft.getMinecraft().theWorld).sendQuittingDisconnectingPacket();
	}

	public static void leaveWorld() {
		net.minecraft.client.Minecraft.getMinecraft().loadWorld(null);
	}

	public static int getWorldHeight() {
		return Objects.requireNonNull(net.minecraft.client.Minecraft.getMinecraft().theWorld).getHeight();
	}

	public static BlockState getStateFromBlockPos(BlockPosition position) {
		return new BlockState(Objects.requireNonNull(net.minecraft.client.Minecraft.getMinecraft().theWorld).getBlockState(position.getMinecraftBlockPos()));
	}

	public static Block getBlockFromPosition(BlockPosition position) {
		BlockState blockState = getStateFromBlockPos(position);
		Block block = Block.newInstance(blockState.getMinecraftBlockState().getBlock());
		block.setBlockPosition(position);
		block.setLocationBlockState(blockState);
		return block;
	}

	public static void addEntityToWorld(int id, Entity entity) {
		Objects.requireNonNull(net.minecraft.client.Minecraft.getMinecraft().theWorld).addEntityToWorld(id, entity.getMinecraftEntity());
	}

	public static void removeEntityFromWorld(int id) {
		Objects.requireNonNull(net.minecraft.client.Minecraft.getMinecraft().theWorld).removeEntityFromWorld(id);
	}

	public static void determineRenderState(IBlockState state, BlockPos pos, CallbackInfoReturnable<Boolean> ci) {
		if (state.getBlock() instanceof BlockLiquid) {
			ci.setReturnValue(((boolean) SettingsMap.getValue(SettingsMap.MapKeys.RENDER, "FLUIDS", true)));
		} else {
			int id = net.minecraft.block.Block.blockRegistry.getIDForObject(state.getBlock());
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
		return Objects.requireNonNull(net.minecraft.client.Minecraft.getMinecraft().thePlayer).dimension;
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
			return MathHelper.clamp_double(sumTickRates / numTicks, 0.0F, 20.0F);
		} else {
			return 0.0d;
		}
	}

	public static void updateTime() {
		if (timeLastTimeUpdate != -1L) {
			float timeElapsed = (float) (System.currentTimeMillis() - timeLastTimeUpdate) / 1000.0F;
			tickRates[(nextIndex % tickRates.length)] = MathHelper.clamp_float(20.0F / timeElapsed, 0.0F, 20.0F);
			nextIndex += 1;
		}
		timeLastTimeUpdate = System.currentTimeMillis();
	}

	public static CompletableFuture<String> getUsernameFromUUID(UUID uuid) {
		return CompletableFuture.supplyAsync(() -> {
			// Check local cache first
			NetHandlerPlayClient networkHandler = net.minecraft.client.Minecraft.getMinecraft().getNetHandler();
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
