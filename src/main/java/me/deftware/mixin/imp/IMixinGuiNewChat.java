package me.deftware.mixin.imp;

import me.deftware.client.framework.chat.hud.HudLine;
import net.minecraft.text.Text;

import java.util.List;

public interface IMixinGuiNewChat {

	void setTheChatLine(Text chatComponent, int chatLineId, int updateCounter, boolean displayOnly);

	List<HudLine> getLines();

	void removeMessage(HudLine line);

	void removeLine(int index);

}
