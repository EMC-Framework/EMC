package me.deftware.client.framework.entity;

import me.deftware.client.framework.item.Item;
import me.deftware.client.framework.math.BlockPosition;
import me.deftware.client.framework.math.BoundingBox;
import me.deftware.client.framework.math.ChunkPosition;
import me.deftware.client.framework.math.Vector3;
import me.deftware.client.framework.message.Message;
import me.deftware.client.framework.entity.types.EntityPlayer;
import me.deftware.client.framework.entity.types.OwnedEntity;
import me.deftware.client.framework.entity.types.animals.HorseEntity;
import me.deftware.client.framework.entity.types.animals.MobEntity;
import me.deftware.client.framework.entity.types.animals.WaterEntity;
import me.deftware.client.framework.entity.types.animals.WolfEntity;
import me.deftware.client.framework.entity.types.main.MainEntityPlayer;
import me.deftware.client.framework.entity.types.objects.BoatEntity;
import me.deftware.client.framework.entity.types.objects.EndCrystalEntity;
import me.deftware.client.framework.entity.types.objects.ItemEntity;
import me.deftware.client.framework.entity.types.objects.ProjectileEntity;
import me.deftware.client.framework.item.ItemStack;
import me.deftware.client.framework.nbt.NbtCompound;
import me.deftware.client.framework.util.Util;
import me.deftware.client.framework.world.ClientWorld;
import me.deftware.client.framework.world.EnumFacing;
import me.deftware.mixin.imp.IMixinAbstractClientPlayer;
import me.deftware.mixin.imp.IMixinEntity;
import me.deftware.mixin.imp.IMixinEntityLivingBase;
import me.deftware.mixin.imp.IMixinNetworkPlayerInfo;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Vec3d;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author Deftware
 */
public class Entity {

	private List<ItemStack> armourItems = Collections.emptyList();

	private Entity vehicle;

	protected final net.minecraft.entity.Entity entity;

	public static Entity newInstance(net.minecraft.entity.Entity entity) {
		if (entity == MinecraftClient.getInstance().player) {
			return new MainEntityPlayer((PlayerEntity) entity);
		} else if (entity instanceof PlayerEntity) {
			return new EntityPlayer((PlayerEntity) entity);
		} else if (entity instanceof net.minecraft.entity.projectile.ProjectileEntity) {
			return new ProjectileEntity(entity);
		} else if (entity instanceof net.minecraft.entity.decoration.EndCrystalEntity) {
			return new EndCrystalEntity(entity);
		} else if (entity instanceof net.minecraft.entity.passive.HorseEntity) {
			return new HorseEntity(entity);
		} else if (entity instanceof net.minecraft.entity.vehicle.BoatEntity) {
			return new BoatEntity(entity);
		} else if (entity instanceof net.minecraft.entity.passive.WolfEntity) {
			return new WolfEntity(entity);
		} else if (entity instanceof net.minecraft.entity.mob.WaterCreatureEntity) {
			return new WaterEntity(entity);
		} else if (entity instanceof net.minecraft.entity.mob.MobEntity) {
			return new MobEntity(entity);
		} else if (entity instanceof net.minecraft.entity.ItemEntity) {
			return new ItemEntity(entity);
		} else if (entity instanceof net.minecraft.entity.LivingEntity) {
			if (entity.writeNbt(new net.minecraft.nbt.NbtCompound()).containsUuid("Owner")) {
				return new OwnedEntity(entity);
			}
			return new me.deftware.client.framework.entity.types.LivingEntity(entity);
		}
		return new Entity(entity);
	}

	protected Entity(net.minecraft.entity.Entity entity) {
		this.entity = entity;
		if (entity.getVehicle() != null)
			this.vehicle = ClientWorld.getClientWorld().getEntityByReference(entity.getVehicle());
	}

	public EnumFacing getHorizontalFacing() {
		return EnumFacing.fromMinecraft(getMinecraftEntity().getHorizontalFacing());
	}

	public BlockPosition getBlockPosition() {
		return (BlockPosition) getMinecraftEntity().getBlockPos();
	}

	public net.minecraft.entity.Entity getMinecraftEntity() {
		return entity;
	}

	public BoundingBox getBoundingBox() {
		return (BoundingBox) getMinecraftEntity().getBoundingBox();
	}

	public boolean isSpectating() {
		return entity.isSpectator();
	}

	public boolean isRiding() {
		return entity.hasVehicle();
	}

	public boolean isAirBorne() {
		return entity.velocityDirty; // TODO
	}

	public float getFallDistance() {
		return entity.fallDistance;
	}

	public Entity getVehicle() {
		if (entity.getVehicle() == null)
			return null;
		if (vehicle == null || vehicle.getMinecraftEntity() != entity.getVehicle())
			vehicle = ClientWorld.getClientWorld().getEntityByReference(entity.getVehicle());
		return vehicle;
	}

	public boolean isTouchingWater() {
		return entity.isTouchingWater();
	}

	public ItemStack getEntityHeldItem(boolean offhand) {
		return ItemStack.EMPTY;
	}

	public void armorInventory(Consumer<ItemStack> consumer) {
		if (entity instanceof LivingEntity e) {
			e.getArmorItems().forEach(itemStack -> consumer.accept((ItemStack) itemStack));
		}
	}

	public void setInPortal(boolean inPortal) {
		((IMixinEntity) entity).setInPortal(inPortal);
	}

	public void reloadSkin() {
		if (entity instanceof AbstractClientPlayerEntity abstractEntity) {
			var texture = abstractEntity.getSkinTextures();
			if (texture.elytraTexture() != null) {
				((IMixinNetworkPlayerInfo) ((IMixinAbstractClientPlayer) abstractEntity).getPlayerNetworkInfo()).reloadTextures();
			}
		}
	}

	public boolean isCollidedHorizontally() {
		return entity.horizontalCollision;
	}

	public boolean isCollidedVertically() {
		return entity.verticalCollision;
	}

	public int getResponseTime() {
		if (MinecraftClient.getInstance().getNetworkHandler() != null) {
			ClientPlayNetworkHandler networkHandler = MinecraftClient.getInstance().getNetworkHandler();
			if (networkHandler.getPlayerListEntry(entity.getUuid()) != null) {
				return Objects.requireNonNull(networkHandler.getPlayerListEntry(entity.getUuid())).getLatency();
			}
		}
		return -1;
	}

	public boolean hasNbt() {
		return entity.writeNbt(new net.minecraft.nbt.NbtCompound()) != null && entity.writeNbt(new net.minecraft.nbt.NbtCompound()).getSize() != 0;
	}

	public NbtCompound getNbt() {
		return (NbtCompound) entity.writeNbt(new net.minecraft.nbt.NbtCompound());
	}

	public int getTicksExisted() {
		return entity.age;
	}

	public boolean isSneaking() {
		return entity.isSneaking();
	}

	public boolean isInLiquid() {
		return entity.isTouchingWater() || entity.isInLava();
	}

	public boolean isSelf() {
		return entity == MinecraftClient.getInstance().player;
	}

	public int getEntityId() {
		return entity.getId();
	}

	public float getHeight() {
		return entity.getHeight();
	}

	public void setGlowing(boolean state) {
		entity.setGlowing(state);
	}

	public boolean isOnGround() {
		return entity.isOnGround();
	}

	public void setOnGround(boolean flag) {
		entity.setOnGround(flag);
	}

	public boolean isOnFire() {
		return entity.isOnFire();
	}

	public void setOnFire(int seconds) {
		entity.setOnFireFor(seconds);
	}

	public float getStepHeight() {
		return entity.getStepHeight();
	}

	public void setStepHeight(float height) {
		if (entity instanceof IMixinEntityLivingBase base) {
			base._setStepHeight(height);
		}
	}

	public boolean isWithinChunk(ChunkPosition chunkPos) {
		return getPosX() >= chunkPos.getStartX() && getPosX() <= chunkPos.getEndX() && getPosZ() >= chunkPos.getStartZ() && getPosZ() <= chunkPos.getEndZ();
	}

	public boolean isHostile() {
		if (entity instanceof ChickenEntity) {
			return ((ChickenEntity) entity).hasJockey;
		}
		return entity instanceof Monster;
	}

	public boolean isAlive() {
		return entity.isAlive();
	}

	public boolean instanceOf(EntityType type) {
		return EntityType.isInstance(this, type);
	}

	public boolean isInvisible() {
		return entity.isInvisible();
	}

	public double getEyeHeight() {
		return entity.getEyeHeight(entity.getPose());
	}

	public double getStandingEyeHeight() {
		return entity.getEyeHeight(EntityPose.STANDING);
	}

	public boolean canBeSeenBy(EntityPlayer entity) {
		return !this.entity.isInvisibleTo(entity.getMinecraftEntity());
	}

	public float distanceToEntity(Entity entity) {
		return this.entity.distanceTo(entity.getMinecraftEntity());
	}

	public Message getName() {
		return (Message) entity.getDisplayName();
	}

	public String getEntityTypeName() {
		return entity.getType().getName().getString();
	}

	public void setNoClip(boolean state) {
		entity.noClip = state;
	}

	public void setFallDistance(float distance) {
		entity.fallDistance = distance;
	}

	public String getEntityName() {
		return entity.getUuidAsString();
	}

	public double getLastTickPosX() {
		return entity.lastRenderX;
	}

	public double getLastTickPosY() {
		return entity.lastRenderY;
	}

	public double getLastTickPosZ() {
		return entity.lastRenderZ;
	}

	public Vector3<Double> getRotationVector() {
		return (Vector3<Double>) getMinecraftEntity().getRotationVector();
	}

	public Vector3<Double> getPosition() {
		return (Vector3<Double>) getMinecraftEntity().getPos();
	}

	public int getChunkX() {
		return entity.getChunkPos().x;
	}

	public int getChunkY() {
		return 0;
	}

	public int getChunkZ() {
		return entity.getChunkPos().z;
	}

	public double getPosX() {
		return entity.getX();
	}

	public double getPosY() {
		return entity.getY();
	}

	public double getPosZ() {
		return entity.getZ();
	}

	public double getPrevPosX() {
		return entity.prevX;
	}

	public double getPrevPosY() {
		return entity.prevY;
	}

	public double getPrevPosZ() {
		return entity.prevZ;
	}

	public float getRotationYaw() {
		return entity.getYaw();
	}

	public float getRotationPitch() {
		return entity.getPitch();
	}

	public void setRotationYaw(float yaw) {
		entity.setYaw(yaw);
	}

	public void setRotationPitch(float pitch) {
		entity.setPitch(pitch);
	}

	public void setPosition(double x, double y, double z) {
		entity.updatePosition(x, y, z);
	}

	public void setPositionAndRotation(double x, double y, double z, float yaw, float pitch) {
		entity.refreshPositionAndAngles(x, y, z, yaw, pitch);
	}

	public Vector3<Double> getEyesPos() {
		return Vector3.ofDouble(entity.getX(), entity.getY() + entity.getEyeHeight(entity.getPose()), entity.getZ());
	}

	public boolean getFlag(int id) {
		return ((IMixinEntity) entity).getAFlag(id);
	}

	public void setVelocity(double x, double y, double z) {
		entity.setVelocity(x, y, z);
	}

	public void setVelocity(Vector3<Double> vector3d) {
		entity.setVelocity((Vec3d) vector3d);
	}

	public Vector3<Double> getVelocity() {
		return (Vector3<Double>) entity.getVelocity();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof EntityCapsule) {
			return ((EntityCapsule) o).getTranslationKey().equalsIgnoreCase(entity.getType().getTranslationKey());
		}
		return super.equals(o);
	}

}
