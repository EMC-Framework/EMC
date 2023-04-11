package me.deftware.client.framework.math;

/**
 * @author Deftware
 */
public interface ChunkPosition {

    int getStartX();

    int getStartZ();

    int getEndX();

    int getEndZ();

    BlockPosition getCenter();

}
