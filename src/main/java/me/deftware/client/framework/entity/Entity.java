package me.deftware.client.framework.entity;

import me.deftware.client.framework.chat.ChatMessage;
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
import me.deftware.client.framework.math.box.BoundingBox;
import me.deftware.client.framework.math.position.BlockPosition;
import me.deftware.client.framework.math.position.ChunkBlockPosition;
import me.deftware.client.framework.math.vector.Vector3d;
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
import net.minecraft.entity.EntityList;
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
	protected final BlockPosition blockPosition;
	protected final BoundingBox boundingBox;

	public static Entity newInstance(net.minecraft.entity.Entity entity) {
		if (entity == Minecraft.getMinecraft().thePlayer) {
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
			NBTTagCompound nbt = new NBTTagCompound();
			entity.writeToNBT(nbt);
			if (nbt.hasKey("Owner")) {
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
		if (entity.ridingEntity != null)
			this.vehicle = ClientWorld.getClientWorld().getEntityByReference(entity.ridingEntity);
		/* TODO
		if (entity.getArmorInventoryList() instanceof List) {
			List<net.minecraft.item.ItemStack> defaultedList = (List<net.minecraft.item.ItemStack>) entity.getArmorInventoryList();
			ItemStack.init(defaultedList, this.armourItems = Util.getEmptyStackList(defaultedList.size()));
		}
		 */
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
		if (entity.ridingEntity == null)
			return null;
		if (vehicle == null || vehicle.getMinecraftEntity() != entity.ridingEntity)
			vehicle = ClientWorld.getClientWorld().getEntityByReference(entity.ridingEntity);
		return vehicle;
	}

	public boolean isTouchingWater() {
		return entity.isInWater();
	}

	public ItemStack getEntityHeldItem(boolean offhand) {
		return ItemStack.EMPTY;
	}

	public List<ItemStack> getArmourInventory() {
		/* TODO
		if (!armourItems.isEmpty())
			ItemStack.copyReferences(entity.getArmorInventoryList(), armourItems);
		 */
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
		return entity.isCollidedHorizontally;
	}

	public boolean isCollidedVertically() {
		return entity.isCollidedVertically;
	}

	public int getResponseTime() {
		if (net.minecraft.client.Minecraft.getMinecraft().getNetHandler() != null) {
			NetHandlerPlayClient nethandlerplayclient = net.minecraft.client.Minecraft.getMinecraft().thePlayer.sendQueue;
			return Objects.requireNonNull(nethandlerplayclient.getPlayerInfo(entity.getUniqueID())).getResponseTime();
		}
		return -1;
	}

	public boolean hasNbt() {
		return !getNbt().getMinecraftCompound().hasNoTags();
	}

	public NbtCompound getNbt() {
		NBTTagCompound nbt = new NBTTagCompound();
		entity.writeToNBT(nbt);
		return new NbtCompound(nbt);
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
		return entity == net.minecraft.client.Minecraft.getMinecraft().thePlayer;
	}

	public int getEntityId() {
		return entity.getEntityId();
	}

	public float getHeight() {
		return entity.height;
	}

	public void setGlowing(boolean state) {

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
		return (float) this.entity.getDistanceSqToEntity(entity.getMinecraftEntity());
	}

	public ChatMessage getName() {
		return new ChatMessage().fromText(entity.getDisplayName(), false);
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
		entity.setVelocity(vector3d.getMinecraftVector().xCoord, vector3d.getMinecraftVector().yCoord, vector3d.getMinecraftVector().zCoord);
	}

	public Vector3d getVelocity() {
		return new Vector3d(entity.motionX, entity.motionY, entity.motionZ);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof EntityCapsule) {
			return ((EntityCapsule) o).getTranslationKey().equalsIgnoreCase(getEntityTypeName());
		}
		return super.equals(o);
	}

}
