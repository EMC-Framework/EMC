package me.deftware.mixin.imp;

import me.deftware.client.framework.chat.hud.ChatHudLine;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

public interface IMixinGuiNewChat {

	void setTheChatLine(ITextComponent chatComponent, int chatLineId, int updateCounter, boolean displayOnly);

	List<ChatHudLine> getLines();

	void removeMessage(ChatHudLine line);

	void removeLine(int index);

}
