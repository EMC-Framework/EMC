package me.deftware.client.framework.world;

import com.google.gson.Gson;
import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.entity.block.TileEntity;
import me.deftware.client.framework.maps.SettingsMap;
import me.deftware.client.framework.math.position.BlockPosition;
import me.deftware.client.framework.world.block.Block;
import me.deftware.client.framework.world.block.BlockState;
import me.deftware.mixin.imp.IMixinWorld;
import me.deftware.mixin.imp.IMixinWorldClient;
import net.minecraft.block.FluidBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Deftware, CDAGaming
 */
public class World {

	private static final float[] tickRates = new float[20];
	private static long timeLastTimeUpdate;
	private static int nextIndex = 0;

	public static boolean isLoaded() {
		return MinecraftClient.getInstance().world != null;
	}

	public static Stream<TileEntity> getLoadedTileEntities() {
		return Objects.requireNonNull(((IMixinWorld) MinecraftClient.getInstance().world)).getLoadedTilesAccessor().stream();
	}

	public static Stream<Entity> getLoadedEntities() {
		return Objects.requireNonNull(((IMixinWorldClient) MinecraftClient.getInstance().world)).getLoadedEntitiesAccessor().values().stream();
	}

	public static long getWorldTime() {
		return Objects.requireNonNull(MinecraftClient.getInstance().world).getTime();
	}

	public static void sendQuittingPacket() {
		Objects.requireNonNull(MinecraftClient.getInstance().world).disconnect();
	}

	public static void leaveWorld() {
		MinecraftClient.getInstance().joinWorld(null);
	}

	public static int getWorldHeight() {
		return Objects.requireNonNull(MinecraftClient.getInstance().world).getHeight();
	}

	public static BlockState getStateFromBlockPos(BlockPosition position) {
		return new BlockState(Objects.requireNonNull(MinecraftClient.getInstance().world).getBlockState(position.getMinecraftBlockPos()));
	}

	public static Block getBlockFromPosition(BlockPosition position) {
		BlockState blockState = getStateFromBlockPos(position);
		Block block = Block.newInstance(blockState.getMinecraftBlockState().getBlock());
		block.setBlockPosition(position);
		block.setLocationBlockState(blockState);
		return block;
	}

	public static void addEntityToWorld(int id, Entity entity) {
		Objects.requireNonNull(MinecraftClient.getInstance().world).addEntity(id, entity.getMinecraftEntity());
	}

	public static void removeEntityFromWorld(int id) {
		Objects.requireNonNull(MinecraftClient.getInstance().world).removeEntity(id);
	}

	public static void determineRenderState(net.minecraft.block.BlockState state, BlockPos pos, CallbackInfoReturnable<Boolean> ci) {
		if (state.getBlock() instanceof FluidBlock) {
			ci.setReturnValue(((boolean) SettingsMap.getValue(SettingsMap.MapKeys.RENDER, "FLUIDS", true)));
		} else {
			int id = Registry.BLOCK.getRawId(state.getBlock());
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
		net.minecraft.world.World dimension = Objects.requireNonNull(MinecraftClient.getInstance().player).world;
		return dimension.getRegistryKey() == net.minecraft.world.World.OVERWORLD ? 0 :
				dimension.getRegistryKey() == net.minecraft.world.World.END ? 1 :
						dimension.getRegistryKey() == net.minecraft.world.World.NETHER ? -1
								: dimension.getDimension().hashCode();
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
		if (uuid == null) return null;
		CompletableFuture<String> future = new CompletableFuture<>();
		new Thread(() -> {
			ClientPlayNetworkHandler h = MinecraftClient.getInstance().getNetworkHandler();
			if (h != null) {
				PlayerListEntry result = h.getPlayerListEntry(uuid);
				if (result != null) {
					future.complete(result.getProfile().getName());
					return;
				} else {
					// rate limit: You can request the same profile once per minute, however you can send as many unique requests as you like.
					try {
						URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString());
						HttpURLConnection conn = (HttpURLConnection) url.openConnection();
						conn.setRequestMethod("GET");
						InputStream is = conn.getInputStream();
						String response = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8)).lines().collect(Collectors.joining(""));
						PlayerData d = new Gson().fromJson(response, PlayerData.class);
						if (d != null && d.name != null) {
							future.complete(d.name);
						} else {
							future.completeExceptionally(new NullPointerException("username not found from UUID"));
						}
					} catch (IOException e) {
						e.printStackTrace();
						future.completeExceptionally(new NullPointerException("something went wrong requesting the UUID"));
					}
				}
			}
		}).start();
		return future;
	}

	private static class PlayerData {
		String id;
		String name;
	}
}
