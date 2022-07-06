package me.deftware.mixin.imp;

import me.deftware.client.framework.chat.hud.HudLine;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.text.Text;

import java.util.List;

public interface IMixinGuiNewChat {

	void setTheChatLine(Text message, int messageId, MessageIndicator arg, boolean refresh);

	List<HudLine> getLines();

	void removeMessage(HudLine line);

	void removeLine(int index);

}
