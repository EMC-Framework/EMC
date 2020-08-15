package me.deftware.client.framework.wrappers.world;

import net.minecraft.entity.EntityLiving;
import net.minecraft.world.WorldEntitySpawner;

public class IWorldEntitySpawner {

    public static boolean canCreatureTypeSpawnAtLocation(ISpawnPlacementType placementType, IWorld world, IBlockPos pos, boolean passive) {
        return WorldEntitySpawner.canCreatureTypeSpawnAtLocation(placementType.toMCType(), world.getWorld(), pos.getPos());
    }

    public enum ISpawnPlacementType {
        ON_GROUND,
        IN_WATER;

        public EntityLiving.SpawnPlacementType toMCType() {
            switch(this) {
                case ON_GROUND:
                    return EntityLiving.SpawnPlacementType.ON_GROUND;
                case IN_WATER:
                    return EntityLiving.SpawnPlacementType.IN_WATER;
                default:
                    return null;
            }
        }

    }

}