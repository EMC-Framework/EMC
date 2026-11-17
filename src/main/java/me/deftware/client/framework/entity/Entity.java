package me.deftware.client.framework.entity;

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
import me.deftware.client.framework.world.ClientWorld;
import me.deftware.client.framework.world.EnumFacing;
import me.deftware.mixin.imp.IMixinAbstractClientPlayer;
import me.deftware.mixin.imp.IMixinEntity;
import me.deftware.mixin.imp.IMixinEntityLivingBase;
import me.deftware.mixin.imp.IMixinNetworkPlayerInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.animal.chicken.Chicken;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
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

	protected final net.minecraft.world.entity.Entity entity;

	public static Entity newInstance(net.minecraft.world.entity.Entity entity) {
		if (entity == Minecraft.getInstance().player) {
			return new MainEntityPlayer((Player) entity);
		} else if (entity instanceof Player) {
			return new EntityPlayer((Player) entity);
		} else if (entity instanceof net.minecraft.world.entity.projectile.Projectile) {
			return new ProjectileEntity(entity);
		} else if (entity instanceof net.minecraft.world.entity.boss.enderdragon.EndCrystal) {
			return new EndCrystalEntity(entity);
		} else if (entity instanceof net.minecraft.world.entity.animal.equine.Horse) {
			return new HorseEntity(entity);
		} else if (entity instanceof net.minecraft.world.entity.vehicle.boat.AbstractBoat) {
			return new BoatEntity(entity);
		} else if (entity instanceof net.minecraft.world.entity.animal.wolf.Wolf) {
			return new WolfEntity(entity);
		} else if (entity instanceof net.minecraft.world.entity.animal.fish.WaterAnimal) {
			return new WaterEntity(entity);
		} else if (entity instanceof net.minecraft.world.entity.Mob) {
			return new MobEntity(entity);
		} else if (entity instanceof net.minecraft.world.entity.item.ItemEntity) {
			return new ItemEntity(entity);
		} else if (entity instanceof net.minecraft.world.entity.LivingEntity) {
			if (saveEntityNbt(entity).contains("Owner")) {
				return new OwnedEntity(entity);
			}
			return new me.deftware.client.framework.entity.types.LivingEntity(entity);
		}
		return new Entity(entity);
	}

	private static net.minecraft.nbt.CompoundTag saveEntityNbt(net.minecraft.world.entity.Entity target) {
		var output = net.minecraft.world.level.storage.TagValueOutput.createWithContext(
				net.minecraft.util.ProblemReporter.DISCARDING,
				target.registryAccess()
		);
		target.saveWithoutId(output);
		return output.buildResult();
	}

	protected Entity(net.minecraft.world.entity.Entity entity) {
		this.entity = entity;
		if (entity.getVehicle() != null)
			this.vehicle = ClientWorld.getClientWorld().getEntityByReference(entity.getVehicle());
	}

	public EnumFacing getHorizontalFacing() {
		return EnumFacing.fromMinecraft(getMinecraftEntity().getDirection());
	}

	public BlockPosition getBlockPosition() {
		return (BlockPosition) getMinecraftEntity().blockPosition();
	}

	public net.minecraft.world.entity.Entity getMinecraftEntity() {
		return entity;
	}

	public BoundingBox getBoundingBox() {
		return (BoundingBox) getMinecraftEntity().getBoundingBox();
	}

	public boolean isSpectating() {
		return entity.isSpectator();
	}

	public boolean isRiding() {
		return entity.isPassenger();
	}

	public boolean isAirBorne() {
		return entity.needsSync;
	}

	public float getFallDistance() {
		return (float) entity.fallDistance;
	}

	public Entity getVehicle() {
		if (entity.getVehicle() == null)
			return null;
		if (vehicle == null || vehicle.getMinecraftEntity() != entity.getVehicle())
			vehicle = ClientWorld.getClientWorld().getEntityByReference(entity.getVehicle());
		return vehicle;
	}

	public boolean isTouchingWater() {
		return entity.isInWater();
	}

	public ItemStack getEntityHeldItem(boolean offhand) {
		if (entity instanceof LivingEntity e) {
			return (ItemStack) (Object) (offhand ? e.getOffhandItem() : e.getMainHandItem());
		}
		return ItemStack.EMPTY;
	}

	public void armorInventory(Consumer<ItemStack> consumer) {
		if (entity instanceof LivingEntity e) {
			consumer.accept((ItemStack) (Object) e.getItemBySlot(EquipmentSlot.FEET));
			consumer.accept((ItemStack) (Object) e.getItemBySlot(EquipmentSlot.LEGS));
			consumer.accept((ItemStack) (Object) e.getItemBySlot(EquipmentSlot.CHEST));
			consumer.accept((ItemStack) (Object) e.getItemBySlot(EquipmentSlot.HEAD));
		}
	}

	public void setInPortal(boolean inPortal) {
		((IMixinEntity) entity).setInPortal(inPortal);
	}

	public void reloadSkin() {
		if (entity instanceof AbstractClientPlayer abstractEntity) {
			var texture = abstractEntity.getSkin();
			if (texture.elytra() != null) {
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
		if (Minecraft.getInstance().getConnection() != null) {
			ClientPacketListener networkHandler = Minecraft.getInstance().getConnection();
			if (networkHandler.getPlayerInfo(entity.getUUID()) != null) {
				return Objects.requireNonNull(networkHandler.getPlayerInfo(entity.getUUID())).getLatency();
			}
		}
		return -1;
	}

	public boolean hasNbt() {
		var nbt = saveEntityNbt(entity);
		return nbt != null && !nbt.isEmpty();
	}

	public NbtCompound getNbt() {
		return (NbtCompound) saveEntityNbt(entity);
	}

	public int getTicksExisted() {
		return entity.tickCount;
	}

	public boolean isSneaking() {
		return entity.isShiftKeyDown();
	}

	public boolean isInLiquid() {
		return entity.isInWater() || entity.isInLava();
	}

	public boolean isSelf() {
		return entity == Minecraft.getInstance().player;
	}

	public int getEntityId() {
		return entity.getId();
	}

	public float getHeight() {
		return entity.getBbHeight();
	}

	public void setGlowing(boolean state) {
		entity.setGlowingTag(state);
	}

	public boolean isOnGround() {
		return entity.onGround();
	}

	public void setOnGround(boolean flag) {
		entity.setOnGround(flag);
	}

	public boolean isOnFire() {
		return entity.isOnFire();
	}

	public void setOnFire(int seconds) {
		entity.igniteForSeconds(seconds);
	}

	public float getStepHeight() {
		return entity.maxUpStep();
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
		if (entity instanceof Chicken) {
			return ((Chicken) entity).isChickenJockey;
		}
		return entity instanceof Enemy;
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
		return entity.getEyeHeight(Pose.STANDING);
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
		return entity.getType().getDescription().getString();
	}

	public void setNoClip(boolean state) {
		entity.noPhysics = state;
	}

	public void setFallDistance(float distance) {
		entity.fallDistance = distance;
	}

	public String getEntityName() {
		return entity.getStringUUID();
	}

	public double getLastTickPosX() {
		return entity.xOld;
	}

	public double getLastTickPosY() {
		return entity.yOld;
	}

	public double getLastTickPosZ() {
		return entity.zOld;
	}

	public Vector3<Double> getRotationVector() {
		return (Vector3<Double>) getMinecraftEntity().getLookAngle();
	}

	public Vector3<Double> getPosition() {
		return (Vector3<Double>) getMinecraftEntity().position();
	}

	public int getChunkX() {
		return entity.chunkPosition().x();
	}

	public int getChunkY() {
		return 0;
	}

	public int getChunkZ() {
		return entity.chunkPosition().z();
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
		return entity.xo;
	}

	public double getPrevPosY() {
		return entity.yo;
	}

	public double getPrevPosZ() {
		return entity.zo;
	}

	public float getRotationYaw() {
		return entity.getYRot();
	}

	public float getRotationPitch() {
		return entity.getXRot();
	}

	public void setRotationYaw(float yaw) {
		entity.setYRot(yaw);
	}

	public void setRotationPitch(float pitch) {
		entity.setXRot(pitch);
	}

	public void setPosition(double x, double y, double z) {
		entity.absSnapTo(x, y, z);
	}

	public void setPositionAndRotation(double x, double y, double z, float yaw, float pitch) {
		entity.snapTo(x, y, z, yaw, pitch);
	}

	public Vector3<Double> getEyesPos() {
		return Vector3.ofDouble(entity.getX(), entity.getY() + entity.getEyeHeight(entity.getPose()), entity.getZ());
	}

	public boolean getFlag(int id) {
		return ((IMixinEntity) entity).getAFlag(id);
	}

	public void setVelocity(double x, double y, double z) {
		entity.setDeltaMovement(x, y, z);
	}

	public void setVelocity(Vector3<Double> vector3d) {
		entity.setDeltaMovement((Vec3) vector3d);
	}

	public Vector3<Double> getVelocity() {
		return (Vector3<Double>) entity.getDeltaMovement();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof EntityCapsule) {
			return ((EntityCapsule) o).getTranslationKey().equalsIgnoreCase(entity.getType().getDescriptionId());
		}
		return super.equals(o);
	}

}
