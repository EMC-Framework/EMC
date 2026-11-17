package me.deftware.client.framework.entity;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ambient.AmbientCreature;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.fish.AbstractFish;
import net.minecraft.world.entity.animal.golem.AbstractGolem;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.chicken.Chicken;
import net.minecraft.world.entity.animal.cow.Cow;
import net.minecraft.world.entity.animal.dolphin.Dolphin;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.animal.cow.MushroomCow;
import net.minecraft.world.entity.animal.feline.Ocelot;
import net.minecraft.world.entity.animal.parrot.Parrot;
import net.minecraft.world.entity.animal.pig.Pig;
import net.minecraft.world.entity.animal.polarbear.PolarBear;
import net.minecraft.world.entity.animal.fish.Pufferfish;
import net.minecraft.world.entity.animal.rabbit.Rabbit;
import net.minecraft.world.entity.animal.golem.SnowGolem;
import net.minecraft.world.entity.animal.squid.Squid;
import net.minecraft.world.entity.animal.turtle.Turtle;
import net.minecraft.world.entity.animal.fish.WaterAnimal;
import net.minecraft.world.entity.animal.equine.Donkey;
import net.minecraft.world.entity.animal.equine.Horse;
import net.minecraft.world.entity.animal.equine.Llama;
import net.minecraft.world.entity.animal.equine.Mule;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.spider.CaveSpider;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.zombie.Drowned;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.entity.monster.Enderman;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.monster.illager.Evoker;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.Giant;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.zombie.Husk;
import net.minecraft.world.entity.monster.illager.Illusioner;
import net.minecraft.world.entity.monster.cubemob.MagmaCube;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.entity.monster.skeleton.Skeleton;
import net.minecraft.world.entity.monster.cubemob.Slime;
import net.minecraft.world.entity.monster.spider.Spider;
import net.minecraft.world.entity.monster.skeleton.Stray;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.monster.illager.Vindicator;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.monster.skeleton.WitherSkeleton;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.monster.zombie.ZombifiedPiglin;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;

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
		net.minecraft.world.entity.Entity entity = emcEntity.getMinecraftEntity();
		// Generic types and players
		if (type.equals(EntityType.ENTITY_PLAYER_SP)) {
			return entity instanceof LocalPlayer;
		} else if (type.equals(EntityType.EntityOtherPlayerMP)) {
			return entity instanceof RemotePlayer;
		} else if (type.equals(EntityType.ENTITY_PLAYER)) {
			return entity instanceof Player;
		} else if (type.equals(EntityType.ENTITY_LIVING_BASE)) {
			return entity instanceof LivingEntity;
		} else if (type.equals(EntityType.ENTITY_LIVING)) {
			return entity instanceof LivingEntity;
		} else if (type.equals(EntityType.ENTITY_ITEM)) {
			return entity instanceof ItemEntity;
		} else if (type.equals(EntityType.ENTITY_PROJECTILE)) {
			return entity instanceof Projectile;
		} else if (type.equals(EntityType.Entity_Ageable)) {
			return entity instanceof AgeableMob;
		} else if (type.equals(EntityType.EntityAmbientCreature)) {
			return entity instanceof AmbientCreature;
		} else if (type.equals(EntityType.EntityWaterMob)) {
			return entity instanceof WaterAnimal;
		} else if (type.equals(EntityType.EntityMob)) {
			return entity instanceof Mob;
		} else if (type.equals(EntityType.EntityAnimal)) {
			return entity instanceof Animal;
		}
		// Passives
		else if (type.equals(EntityType.ENTITY_BAT)) {
			return entity instanceof Bat;
		} else if (type.equals(EntityType.ENTITY_CHICKEN)) {
			return entity instanceof Chicken;
		} else if (type.equals(EntityType.ENTITY_COW)) {
			return entity instanceof Cow;
		} else if (type.equals(EntityType.ENTITY_FISH)) {
			return entity instanceof AbstractFish;
		} else if (type.equals(EntityType.ENTITY_MOOSHROOM)) {
			return entity instanceof MushroomCow;
		} else if (type.equals(EntityType.ENTITY_OCELOT)) {
			return entity instanceof Ocelot;
		} else if (type.equals(EntityType.ENTITY_PIG)) {
			return entity instanceof Pig;
		} else if (type.equals(EntityType.ENTITY_POLAR_BEAR)) {
			return entity instanceof PolarBear;
		} else if (type.equals(EntityType.ENTITY_RABBIT)) {
			return entity instanceof Rabbit;
		} else if (type.equals(EntityType.ENTITY_SHEEP)) {
			return entity instanceof Sheep;
		} else if (type.equals(EntityType.ENTITY_SQUID)) {
			return entity instanceof Squid;
		} else if (type.equals(EntityType.ENTITY_TURTLE)) {
			return entity instanceof Turtle;
		} else if (type.equals(EntityType.ENTITY_VILLAGER)) {
			return entity instanceof Villager;
		} else if (type.equals(EntityType.ENTITY_DOLPHIN)) {
			return entity instanceof Dolphin;
		} else if (type.equals(EntityType.ENTITY_DONKEY)) {
			return entity instanceof Donkey;
		} else if (type.equals(EntityType.ENTITY_MULE)) {
			return entity instanceof Mule;
		} else if (type.equals(EntityType.ENTITY_HORSE)) {
			return entity instanceof Horse;
		} else if (type.equals(EntityType.ENTITY_PARROT)) {
			return entity instanceof Parrot;
		}
		// Hostiles
		else if (type.equals(EntityType.EntitySlime) || type.equals(EntityType.ENTITY_SLIME)) {
			return entity instanceof Slime;
		} else if (type.equals(EntityType.EntityFlying)) {
			return entity instanceof Ghast || entity instanceof Phantom;
		} else if (type.equals(EntityType.EntityGolem)) {
			return entity instanceof AbstractGolem;
		} else if (type.equals(EntityType.ENTITY_SPIDER)) {
			return entity instanceof Spider;
		} else if (type.equals(EntityType.ENTITY_ZOMBIE_PIGMAN)) {
			return entity instanceof ZombifiedPiglin;
		} else if (type.equals(EntityType.ENTITY_ENDERMAN)) {
			return entity instanceof Enderman;
		} else if (type.equals(EntityType.ENTITY_WITHER_SKELETON)) {
			return entity instanceof WitherSkeleton;
		} else if (type.equals(EntityType.ENTITY_WITHER)) {
			return entity instanceof WitherBoss;
		} else if (type.equals(EntityType.ENTITY_DRAGON)) {
			return entity instanceof EnderDragon;
		} else if (type.equals(EntityType.ENTITY_PHANTOM)) {
			return entity instanceof Phantom;
		} else if (type.equals(EntityType.ENTITY_DROWNED)) {
			return entity instanceof Drowned;
		} else if (type.equals(EntityType.ENTITY_EVOKER)) {
			return entity instanceof Evoker;
		} else if (type.equals(EntityType.ENTITY_STRAY)) {
			return entity instanceof Stray;
		} else if (type.equals(EntityType.ENTITY_ELDER_GUARDIAN)) {
			return entity instanceof ElderGuardian;
		} else if (type.equals(EntityType.ENTITY_CREEPER)) {
			return entity instanceof Creeper;
		} else if (type.equals(EntityType.ENTITY_VINDICATOR)) {
			return entity instanceof Vindicator;
		} else if (type.equals(EntityType.ENTITY_ILLUSIONER)) {
			return entity instanceof Illusioner;
		} else if (type.equals(EntityType.ENTITY_HUSK)) {
			return entity instanceof Husk;
		} else if (type.equals(EntityType.ENTITY_ZOMBIE)) {
			return entity instanceof Zombie;
		} else if (type.equals(EntityType.ENTITY_SKELETON)) {
			return entity instanceof Skeleton;
		} else if (type.equals(EntityType.ENTITY_SHULKER)) {
			return entity instanceof Shulker;
		} else if (type.equals(EntityType.ENTITY_GUARDIAN)) {
			return entity instanceof Guardian;
		} else if (type.equals(EntityType.ENTITY_VEX)) {
			return entity instanceof Vex;
		} else if (type.equals(EntityType.ENTITY_SILVERFISH)) {
			return entity instanceof Silverfish;
		} else if (type.equals(EntityType.ENTITY_WITCH)) {
			return entity instanceof Witch;
		} else if (type.equals(EntityType.ENTITY_GIANT)) {
			return entity instanceof Giant;
		} else if (type.equals(EntityType.ENTITY_BLAZE)) {
			return entity instanceof Blaze;
		} else if (type.equals(EntityType.ENTITY_ENDERMITE)) {
			return entity instanceof Endermite;
		} else if (type.equals(EntityType.ENTITY_GHAST)) {
			return entity instanceof Ghast;
		} else if (type.equals(EntityType.ENTITY_MAGMA_CUBE)) {
			return entity instanceof MagmaCube;
		} else if (type.equals(EntityType.ENTITY_CAVE_SPIDER)) {
			return entity instanceof CaveSpider;
		}
		// Neutrals
		else if (type.equals(EntityType.ENTITY_WOLF)) {
			return entity instanceof Wolf;
		} else if (type.equals(EntityType.ENTITY_LLAMA)) {
			return entity instanceof Llama;
		} else if (type.equals(EntityType.ENTITY_IRON_GOLEM)) {
			return entity instanceof IronGolem;
		} else if (type.equals(EntityType.ENTITY_SNOW_GOLEM)) {
			return entity instanceof SnowGolem;
		} else if (type.equals(EntityType.ENTITY_PUFFERFISH)) {
			return entity instanceof Pufferfish;
		}
		return false;
	}

}
