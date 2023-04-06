package me.deftware.client.framework.world.player;

import me.deftware.client.framework.message.Message;
import java.util.UUID;

/**
 * @author Deftware
 */
public interface PlayerEntry {

    /**
     * @return The profile UUID
     */
    UUID _getProfileID();

    /**
     * @return The profile username
     */
    String _getName();

    /**
     * @return The name as shown in the tab view
     */
    Message _getDisplayName();

}
