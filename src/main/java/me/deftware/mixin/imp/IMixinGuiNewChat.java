package me.deftware.mixin.imp;

import net.minecraft.util.IChatComponent;
import me.deftware.client.framework.chat.hud.HudLine;

import java.util.List;

public interface IMixinGuiNewChat {

	void setTheChatLine(IChatComponent chatComponent, int chatLineId, int updateCounter, boolean displayOnly);

	List<HudLine> getLines();

	void removeMessage(HudLine line);

	void removeLine(int index);

}
