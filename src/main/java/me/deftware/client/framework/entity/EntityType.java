package me.deftware.client.framework.entity;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author Deftware
 */
public enum EntityType {

	ENTITY_PLAYER_SP, EntityOtherPlayerMP, ENTITY_PLAYER, EntityAnimal, EntitySlime, EntityGolem, EntityFlying, EntityMob, EntityWaterMob,
	ENTITY_LIVING_BASE, ENTITY_LIVING, Entity_Ageable, EntityAmbientCreature, ENTITY_ITEM, ENTITY_PROJECTILE,
	/*
	 * Passive mobs
	 */
	ENTITY_BAT, ENTITY_CHICKEN, ENTITY_COW, ENTITY_FISH, ENTITY_MOOSHROOM, ENTITY_OCELOT, ENTITY_PIG, ENTITY_POLAR_BEAR,
	ENTITY_RABBIT, ENTITY_SHEEP, ENTITY_SQUID, ENTITY_TURTLE, ENTITY_VILLAGER, ENTITY_DOLPHIN, ENTITY_DONKEY, ENTITY_HORSE,
	ENTITY_MULE, ENTITY_PARROT,
	/*
	 * Hostile mobs
	 */
	ENTITY_ENDERMAN, ENTITY_ZOMBIE_PIGMAN, ENTITY_SPIDER, ENTITY_WITHER_SKELETON, ENTITY_WITHER, ENTITY_DRAGON, ENTITY_PHANTOM, ENTITY_DROWNED,
	ENTITY_EVOKER, ENTITY_STRAY, ENTITY_ELDER_GUARDIAN, ENTITY_CREEPER, ENTITY_VINDICATOR, ENTITY_ILLUSIONER, ENTITY_ZOMBIE, ENTITY_HUSK,
	ENTITY_SKELETON, ENTITY_SHULKER, ENTITY_SLIME, ENTITY_GUARDIAN, ENTITY_VEX, ENTITY_SILVERFISH, ENTITY_WITCH, ENTITY_GIANT, ENTITY_BLAZE,
	ENTITY_ENDERMITE, ENTITY_GHAST, ENTITY_MAGMA_CUBE, ENTITY_CAVE_SPIDER,
	/*
	 * Neutral mobs
	 */
	ENTITY_WOLF, ENTITY_LLAMA, ENTITY_IRON_GOLEM, ENTITY_SNOW_GOLEM, ENTITY_PUFFERFISH;

	public static boolean isInstance(Entity emcEntity, EntityType type) {
		net.minecraft.entity.Entity entity = emcEntity.getMinecraftEntity();
		if (type.equals(EntityType.ENTITY_PLAYER_SP)) {
			return entity instanceof EntityPlayerSP;
		} else if (type.equals(EntityType.EntityOtherPlayerMP)) {
			return entity instanceof net.minecraft.client.entity.EntityOtherPlayerMP;
		} else if (type.equals(EntityType.ENTITY_PLAYER)) {
			return entity instanceof EntityPlayer;
		} else if (type.equals(EntityType.ENTITY_LIVING_BASE)) {
			return entity instanceof EntityLivingBase;
		} else if (type.equals(EntityType.ENTITY_LIVING)) {
			return entity instanceof EntityLiving;
		} else if (type.equals(EntityType.ENTITY_ITEM)) {
			return entity instanceof EntityItem;
		} else if (type.equals(EntityType.ENTITY_PROJECTILE)) {
			return entity instanceof IProjectile;
		} else if (type.equals(EntityType.Entity_Ageable)) {
			return entity instanceof EntityAgeable;
		} else if (type.equals(EntityType.EntityAmbientCreature)) {
			return entity instanceof EntityAmbientCreature;
		} else if (type.equals(EntityType.EntityWaterMob)) {
			return entity instanceof EntityWaterMob;
		} else if (type.equals(EntityType.EntityMob)) {
			return entity instanceof net.minecraft.entity.monster.EntityMob;
		} else if (type.equals(EntityType.EntityAnimal)) {
			return entity instanceof EntityAnimal;
		}
		// Passives
		else if (type.equals(EntityType.ENTITY_BAT)) {
			return entity instanceof EntityBat;
		} else if (type.equals(EntityType.ENTITY_CHICKEN)) {
			return entity instanceof EntityChicken;
		} else if (type.equals(EntityType.ENTITY_COW)) {
			return entity instanceof EntityCow;
		} else if (type.equals(EntityType.ENTITY_MOOSHROOM)) {
			return entity instanceof EntityMooshroom;
		} else if (type.equals(EntityType.ENTITY_OCELOT)) {
			return entity instanceof EntityOcelot;
		} else if (type.equals(EntityType.ENTITY_PIG)) {
			return entity instanceof EntityPig;
		} else if (type.equals(EntityType.ENTITY_RABBIT)) {
			return entity instanceof EntityRabbit;
		} else if (type.equals(EntityType.ENTITY_SHEEP)) {
			return entity instanceof EntitySheep;
		} else if (type.equals(EntityType.ENTITY_SQUID)) {
			return entity instanceof EntitySquid;
		} else if (type.equals(EntityType.ENTITY_VILLAGER)) {
			return entity instanceof EntityVillager;
		} else if (type.equals(EntityType.ENTITY_DONKEY)) {
			return ((EntityHorse)entity).getHorseType() == 1;
		} else if (type.equals(EntityType.ENTITY_MULE)) {
			return ((EntityHorse)entity).getHorseType() == 2;
		} else if (type.equals(EntityType.ENTITY_HORSE)) {
			return entity instanceof EntityHorse;
		}
		// Hostiles
		else if (type.equals(EntityType.EntitySlime) || type.equals(EntityType.ENTITY_SLIME)) {
			return entity instanceof net.minecraft.entity.monster.EntitySlime;
		} else if (type.equals(EntityType.EntityFlying)) {
			return entity instanceof net.minecraft.entity.EntityFlying;
		} else if (type.equals(EntityType.EntityGolem)) {
			return entity instanceof net.minecraft.entity.monster.EntityGolem;
		} else if (type.equals(EntityType.ENTITY_SPIDER)) {
			return entity instanceof EntitySpider;
		} else if (type.equals(EntityType.ENTITY_ZOMBIE_PIGMAN)) {
			return entity instanceof EntityPigZombie;
		} else if (type.equals(EntityType.ENTITY_ENDERMAN)) {
			return entity instanceof EntityEnderman;
		} else if (type.equals(EntityType.ENTITY_WITHER)) {
			return entity instanceof EntityWither;
		} else if (type.equals(EntityType.ENTITY_DRAGON)) {
			return entity instanceof EntityDragon;
		} else if (type.equals(EntityType.ENTITY_CREEPER)) {
			return entity instanceof EntityCreeper;
		} else if (type.equals(EntityType.ENTITY_HUSK)) {
			return entity instanceof EntityZombie;
		} else if (type.equals(EntityType.ENTITY_ZOMBIE)) {
			return entity instanceof EntityZombie;
		} else if (type.equals(EntityType.ENTITY_SKELETON)) {
			return entity instanceof EntitySkeleton;
		} else if (type.equals(EntityType.ENTITY_GUARDIAN)) {
			return entity instanceof EntityGuardian;
		} else if (type.equals(EntityType.ENTITY_SILVERFISH)) {
			return entity instanceof EntitySilverfish;
		} else if (type.equals(EntityType.ENTITY_WITCH)) {
			return entity instanceof EntityWitch;
		} else if (type.equals(EntityType.ENTITY_GIANT)) {
			return entity instanceof EntityGiantZombie;
		} else if (type.equals(EntityType.ENTITY_BLAZE)) {
			return entity instanceof EntityBlaze;
		} else if (type.equals(EntityType.ENTITY_ENDERMITE)) {
			return entity instanceof EntityEndermite;
		} else if (type.equals(EntityType.ENTITY_GHAST)) {
			return entity instanceof EntityGhast;
		} else if (type.equals(EntityType.ENTITY_MAGMA_CUBE)) {
			return entity instanceof EntityMagmaCube;
		} else if (type.equals(EntityType.ENTITY_CAVE_SPIDER)) {
			return entity instanceof EntityCaveSpider;
		}
		// Neutrals
		else if (type.equals(EntityType.ENTITY_WOLF)) {
			return entity instanceof EntityWolf;
		} else if (type.equals(EntityType.ENTITY_IRON_GOLEM)) {
			return entity instanceof EntityIronGolem;
		} else if (type.equals(EntityType.ENTITY_SNOW_GOLEM)) {
			return entity instanceof EntitySnowman;
		}
		return false;
	}

}
