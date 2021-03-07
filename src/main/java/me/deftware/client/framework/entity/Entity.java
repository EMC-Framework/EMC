package me.deftware.client.framework.entity;

import com.google.common.collect.Iterables;
import me.deftware.client.framework.chat.ChatMessage;
import me.deftware.client.framework.conversion.ComparedConversion;
import me.deftware.client.framework.conversion.ConvertedList;
import me.deftware.client.framework.entity.types.EntityPlayer;
import me.deftware.client.framework.entity.types.OwnedEntity;
import me.deftware.client.framework.entity.types.animals.HorseEntity;
import me.deftware.client.framework.entity.types.animals.MobEntity;
import me.deftware.client.framework.entity.types.animals.WaterEntity;
import me.deftware.client.framework.entity.types.animals.WolfEntity;
import me.deftware.client.framework.entity.types.objects.BoatEntity;
import me.deftware.client.framework.entity.types.objects.EndCrystalEntity;
import me.deftware.client.framework.entity.types.objects.ItemEntity;
import me.deftware.client.framework.entity.types.objects.ProjectileEntity;
import me.deftware.client.framework.item.ItemStack;
import me.deftware.client.framework.math.box.BoundingBox;
import me.deftware.client.framework.math.position.BlockPosition;
import me.deftware.client.framework.math.position.ChunkBlockPosition;
import me.deftware.client.framework.math.vector.Vector3d;
import me.deftware.client.framework.nbt.NbtCompound;
import me.deftware.client.framework.world.EnumFacing;
import me.deftware.mixin.imp.IMixinAbstractClientPlayer;
import me.deftware.mixin.imp.IMixinEntity;
import me.deftware.mixin.imp.IMixinNetworkPlayerInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.EntityList;
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

import java.util.List;
import java.util.Objects;

/**
 * @author Deftware
 */
public class Entity {

	protected final ConvertedList<ItemStack, net.minecraft.item.ItemStack> armourItems;
	protected final ConvertedList<ItemStack, net.minecraft.item.ItemStack> heldItems;
	protected final ComparedConversion<net.minecraft.entity.Entity, Entity> vehicle;
	protected final net.minecraft.entity.Entity entity;
	protected final BlockPosition blockPosition;
	protected final BoundingBox boundingBox;

	public static Entity newInstance(net.minecraft.entity.Entity entity) {
		if (entity instanceof net.minecraft.entity.player.EntityPlayer) {
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
			if (entity.writeToNBT(new NBTTagCompound()).hasUniqueId("Owner")) {
				return new OwnedEntity(entity);
			}
			return new me.deftware.client.framework.entity.types.LivingEntity(entity);
		}
		return new Entity(entity);
	}

	protected Entity(net.minecraft.entity.Entity entity) {
		this.entity = entity;
		this.boundingBox = new BoundingBox(entity);
		this.blockPosition = new BlockPosition(entity);
		this.vehicle = new ComparedConversion<>(entity::getRidingEntity, Entity::newInstance);
		this.heldItems = new ConvertedList<>(entity::getHeldEquipment, pair ->
				pair.getLeft().getMinecraftItemStack() == Iterables.get(entity.getHeldEquipment(), pair.getRight())
				, ItemStack::new);
		this.armourItems = new ConvertedList<>(entity::getArmorInventoryList, pair ->
				pair.getLeft().getMinecraftItemStack() == Iterables.get(entity.getArmorInventoryList(), pair.getRight())
				, ItemStack::new);
	}

	public EnumFacing getHorizontalFacing() {
		return EnumFacing.fromMinecraft(getMinecraftEntity().getHorizontalFacing());
	}

	public BlockPosition getBlockPosition() {
		return blockPosition;
	}

	public net.minecraft.entity.Entity getMinecraftEntity() {
		return entity;
	}

	public BoundingBox getBoundingBox() {
		return boundingBox;
	}

	public boolean isSpectating() {
		if (this instanceof EntityPlayer) {
			return ((net.minecraft.entity.player.EntityPlayer) entity).isSpectator();
		}
		return false;
	}

	public boolean isRiding() {
		return entity.isRiding();
	}

	public boolean isAirBorne() {
		return entity.isAirBorne; // TODO
	}

	public float getFallDistance() {
		return entity.fallDistance;
	}

	public Entity getVehicle() {
		return vehicle.get();
	}

	public boolean isTouchingWater() {
		return entity.isInWater();
	}

	public me.deftware.client.framework.item.ItemStack getEntityHeldItem(boolean offhand) {
		List<me.deftware.client.framework.item.ItemStack> stackList = heldItems.poll();
		if (!stackList.isEmpty()) {
			if (!offhand) return stackList.get(0);
			else if (stackList.size() > 1) return stackList.get(1);
		}
		return me.deftware.client.framework.item.ItemStack.EMPTY;
	}

	public List<ItemStack> getArmourInventory() {
		return armourItems.poll();
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
		if (net.minecraft.client.Minecraft.getMinecraft().getConnection() != null) {
			NetHandlerPlayClient nethandlerplayclient = net.minecraft.client.Minecraft.getMinecraft().player.connection;
			return Objects.requireNonNull(nethandlerplayclient.getPlayerInfo(entity.getUniqueID())).getResponseTime();
		}
		return -1;
	}

	public boolean hasNbt() {
		return !entity.writeToNBT(new NBTTagCompound()).isEmpty();
	}

	public NbtCompound getNbt() {
		return new NbtCompound(entity.writeToNBT(new NBTTagCompound()));
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
		return entity == net.minecraft.client.Minecraft.getMinecraft().player;
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

	public boolean isWithinChunk(ChunkBlockPosition chunkPos) {
		return getPosX() >= chunkPos.getStartX() && getPosX() <= chunkPos.getEndX() && getPosZ() >= chunkPos.getStartZ() && getPosZ() <= chunkPos.getEndZ();
	}

	public boolean isHostile() {
		if (entity instanceof EntityChicken) {
			return ((EntityChicken) entity).chickenJockey;
		}
		return entity instanceof IMob;
	}

	public boolean isAlive() {
		return !entity.isDead;
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

	public ChatMessage getName() {
		return new ChatMessage().fromText(entity.getDisplayName());
	}

	public String getEntityTypeName() {
		String s = EntityList.getEntityString(entity);
		if (s == null) s = "generic";
		return s;
	}

	public void setNoClip(boolean state) {
		entity.noClip = state;
	}

	public void setFallDistance(float distance) {
		entity.fallDistance = distance;
	}

	public String getEntityName() {
		return entity.getName();
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

	public Vector3d getRotationVector() {
		return new Vector3d(getMinecraftEntity().getLookVec());
	}

	public Vector3d getPosition() {
		return new Vector3d(getPosX(), getPosY() + this.getEyeHeight(), getPosZ());
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

	public Vector3d getEyesPos() {
		return new Vector3d(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ);
	}

	public boolean getFlag(int id) {
		return ((IMixinEntity) entity).getAFlag(id);
	}

	public void setVelocity(double x, double y, double z) {
		entity.setVelocity(x, y, z);
	}

	public void setVelocity(Vector3d vector3d) {
		entity.setVelocity(vector3d.getMinecraftVector().x, vector3d.getMinecraftVector().y, vector3d.getMinecraftVector().z);
	}

	public Vector3d getVelocity() {
		return new Vector3d(entity.motionX, entity.motionY, entity.motionZ);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof EntityCapsule) {
			return ((EntityCapsule) o).getTranslationKey().equalsIgnoreCase(EntityList.getEntityString(entity));
		}
		return super.equals(o);
	}

}
