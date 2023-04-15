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
import me.deftware.client.framework.util.Util;
import me.deftware.client.framework.world.ClientWorld;
import me.deftware.client.framework.world.EnumFacing;
import me.deftware.mixin.imp.IMixinAbstractClientPlayer;
import me.deftware.mixin.imp.IMixinEntity;
import me.deftware.mixin.imp.IMixinNetworkPlayerInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author Deftware
 */
public class Entity {

	private List<ItemStack> armourItems = Collections.emptyList();

	private Entity vehicle;

	protected final net.minecraft.entity.Entity entity;

	public static Entity newInstance(net.minecraft.entity.Entity entity) {
		if (entity == Minecraft.getInstance().player) {
			return new MainEntityPlayer((net.minecraft.entity.player.EntityPlayer) entity);
		} else if (entity instanceof net.minecraft.entity.player.EntityPlayer) {
			return new EntityPlayer((net.minecraft.entity.player.EntityPlayer) entity);
		} else if (entity instanceof EntityEnderCrystal) {
			return new EndCrystalEntity(entity);
		} else if (entity instanceof EntityArrow) {
			return new ProjectileEntity(entity);
		} else if (entity instanceof net.minecraft.entity.passive.EntityHorse) {
			return new HorseEntity(entity);
		} else if (entity instanceof EntityBoat) {
			return new BoatEntity(entity);
		} else if (entity instanceof EntityWolf) {
			return new WolfEntity(entity);
		} else if (entity instanceof EntityWaterMob) {
			return new WaterEntity(entity);
		} else if (entity instanceof EntityMob) {
			return new MobEntity(entity);
		} else if (entity instanceof EntityItem) {
			return new ItemEntity(entity);
		} else if (entity instanceof EntityLivingBase) {
			if (entity.writeWithoutTypeId(new NBTTagCompound()).hasUniqueId("Owner")) {
				return new OwnedEntity(entity);
			}
			return new me.deftware.client.framework.entity.types.LivingEntity(entity);
		}
		return new Entity(entity);
	}

	protected Entity(net.minecraft.entity.Entity entity) {
		this.entity = entity;
		if (entity.getRidingEntity() != null)
			this.vehicle = ClientWorld.getClientWorld().getEntityByReference(entity.getRidingEntity());
		if (entity.getArmorInventoryList() instanceof NonNullList) {
			NonNullList<net.minecraft.item.ItemStack> defaultedList = (NonNullList<net.minecraft.item.ItemStack>) entity.getArmorInventoryList();
			ItemStack.init(defaultedList, this.armourItems = Util.getEmptyStackList(defaultedList.size()));
		}
	}

	public EnumFacing getHorizontalFacing() {
		return EnumFacing.fromMinecraft(getMinecraftEntity().getHorizontalFacing());
	}

	public BlockPosition getBlockPosition() {
		return (BlockPosition) getMinecraftEntity().getPosition();
	}

	public net.minecraft.entity.Entity getMinecraftEntity() {
		return entity;
	}

	public BoundingBox getBoundingBox() {
		return (BoundingBox) getMinecraftEntity().getBoundingBox();
	}

	public boolean isSpectating() {
		if (this instanceof EntityPlayer) {
			return ((net.minecraft.entity.player.EntityPlayer) entity).isSpectator();
		}
		return false;
	}

	public boolean isRiding() {
		return entity.isPassenger();
	}

	public boolean isAirBorne() {
		return entity.isAirBorne; // TODO
	}

	public float getFallDistance() {
		return entity.fallDistance;
	}

	public Entity getVehicle() {
		if (entity.getRidingEntity() == null)
			return null;
		if (vehicle == null || vehicle.getMinecraftEntity() != entity.getRidingEntity())
			vehicle = ClientWorld.getClientWorld().getEntityByReference(entity.getRidingEntity());
		return vehicle;
	}

	public boolean isTouchingWater() {
		return entity.isInWater();
	}

	public ItemStack getEntityHeldItem(boolean offhand) {
		return ItemStack.EMPTY;
	}

	public List<ItemStack> getArmourInventory() {
		if (!armourItems.isEmpty())
			ItemStack.copyReferences(entity.getArmorInventoryList(), armourItems);
		return armourItems;
	}

	public void setInPortal(boolean inPortal) {
		((IMixinEntity) entity).setInPortal(inPortal);
	}

	public void reloadSkin() {
		if (entity instanceof AbstractClientPlayer) {
			AbstractClientPlayer abstractEntity = (AbstractClientPlayer) entity;
			if (abstractEntity.hasPlayerInfo()) {
				((IMixinNetworkPlayerInfo) ((IMixinAbstractClientPlayer) abstractEntity).getPlayerNetworkInfo()).reloadTextures();
			}
		}
	}

	public boolean isCollidedHorizontally() {
		return entity.collidedHorizontally;
	}

	public boolean isCollidedVertically() {
		return entity.collidedVertically;
	}

	public int getResponseTime() {
		if (net.minecraft.client.Minecraft.getInstance().getConnection() != null) {
			NetHandlerPlayClient nethandlerplayclient = net.minecraft.client.Minecraft.getInstance().player.connection;
			return Objects.requireNonNull(nethandlerplayclient.getPlayerInfo(entity.getUniqueID())).getResponseTime();
		}
		return -1;
	}

	public boolean hasNbt() {
		return !entity.writeWithoutTypeId(new NBTTagCompound()).isEmpty();
	}

	public NbtCompound getNbt() {
		return new NbtCompound(entity.writeWithoutTypeId(new NBTTagCompound()));
	}

	public int getTicksExisted() {
		return entity.ticksExisted;
	}

	public boolean isSneaking() {
		return entity.isSneaking();
	}

	public boolean isInLiquid() {
		return entity.isInWater() || entity.isInLava();
	}

	public boolean isSelf() {
		return entity == net.minecraft.client.Minecraft.getInstance().player;
	}

	public int getEntityId() {
		return entity.getEntityId();
	}

	public float getHeight() {
		return entity.height;
	}

	public void setGlowing(boolean state) {
		entity.setGlowing(state);
	}

	public boolean isOnGround() {
		return entity.onGround;
	}

	public void setOnGround(boolean flag) {
		entity.onGround = flag;
	}

	public boolean isOnFire() {
		return entity.isBurning();
	}

	public void setOnFire(int seconds) {
		entity.setFire(seconds);
	}

	public float getStepHeight() {
		return entity.stepHeight;
	}

	public void setStepHeight(float height) {
		entity.stepHeight = height;
	}

	public boolean isWithinChunk(ChunkPosition chunkPos) {
		return getPosX() >= chunkPos.getStartX() && getPosX() <= chunkPos.getEndX() && getPosZ() >= chunkPos.getStartZ() && getPosZ() <= chunkPos.getEndZ();
	}

	public boolean isHostile() {
		if (entity instanceof EntityChicken) {
			return ((EntityChicken) entity).chickenJockey;
		}
		return entity instanceof IMob;
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
		return entity.getEyeHeight();
	}

	public double getStandingEyeHeight() {
		return entity.getEyeHeight();
	}

	public boolean canBeSeenBy(EntityPlayer entity) {
		return !this.entity.isInvisibleToPlayer(entity.getMinecraftEntity());
	}

	public float distanceToEntity(Entity entity) {
		return this.entity.getDistance(entity.getMinecraftEntity());
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
		return entity.getScoreboardName();
	}

	public double getLastTickPosX() {
		return entity.lastTickPosX;
	}

	public double getLastTickPosY() {
		return entity.lastTickPosY;
	}

	public double getLastTickPosZ() {
		return entity.lastTickPosZ;
	}

	public Vector3<Double> getRotationVector() {
		return (Vector3<Double>) getMinecraftEntity().getLookVec();
	}

	public Vector3<Double> getPosition() {
		return Vector3.ofDouble(getPosX(), getPosY() + this.getEyeHeight(), getPosZ());
	}

	public int getChunkX() {
		return entity.chunkCoordX;
	}

	public int getChunkY() {
		return entity.chunkCoordY;
	}

	public int getChunkZ() {
		return entity.chunkCoordZ;
	}

	public double getPosX() {
		return entity.posX;
	}

	public double getPosY() {
		return entity.posY;
	}

	public double getPosZ() {
		return entity.posZ;
	}

	public double getPrevPosX() {
		return entity.prevPosX;
	}

	public double getPrevPosY() {
		return entity.prevPosY;
	}

	public double getPrevPosZ() {
		return entity.prevPosZ;
	}

	public float getRotationYaw() {
		return entity.rotationYaw;
	}

	public float getRotationPitch() {
		return entity.rotationPitch;
	}

	public void setRotationYaw(float yaw) {
		entity.rotationYaw = yaw;
	}

	public void setRotationPitch(float pitch) {
		entity.rotationPitch = pitch;
	}

	public void setPosition(double x, double y, double z) {
		entity.setPosition(x, y, z);
	}

	public void setPositionAndRotation(double x, double y, double z, float yaw, float pitch) {
		entity.setPositionAndRotation(x, y, z, yaw, pitch);
	}

	public Vector3<Double> getEyesPos() {
		return Vector3.ofDouble(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ);
	}

	public boolean getFlag(int id) {
		return ((IMixinEntity) entity).getAFlag(id);
	}

	public void setVelocity(double x, double y, double z) {
		entity.setVelocity(x, y, z);
	}

	public void setVelocity(Vector3<Double> vector3d) {
		entity.setVelocity(vector3d.getX(), vector3d.getY(), vector3d.getZ());
	}

	public Vector3<Double> getVelocity() {
		return Vector3.ofDouble(entity.motionX, entity.motionY, entity.motionZ);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof EntityCapsule) {
			return ((EntityCapsule) o).getTranslationKey().equalsIgnoreCase(entity.getType().getTranslationKey());
		}
		return super.equals(o);
	}

}
