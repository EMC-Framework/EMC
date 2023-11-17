package me.deftware.client.framework.command;

import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.context.CommandContext;
import me.deftware.client.framework.message.Message;
import me.deftware.client.framework.entity.Entity;
import net.minecraft.command.arguments.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;

/**
 * @author Deftware
 */
@Deprecated
public class CommandResult {

	private final CommandContext<?> context;

	public CommandResult(CommandContext<?> context) {
		this.context = context;
	}

	public String getString(String node) {
		return StringArgumentType.getString(context, node);
	}

	public Entity getEntity(String node) throws Exception {
		return Entity.newInstance(EntityArgumentType.getEntity((CommandContext<ServerCommandSource>) context, node));
	}

	public Message getEntityName(String node) throws Exception {
		return getEntity(node).getName();
	}

	public Object getCustom(String node, Class<?> clazz) {
		return context.getArgument(node, clazz);
	}

	public int getInteger(String node) {
		return IntegerArgumentType.getInteger(context, node);
	}

	public float getFloat(String node) {
		return FloatArgumentType.getFloat(context, node);
	}

	public double getDouble(String node) {
		return DoubleArgumentType.getDouble(context, node);
	}

	public boolean getBoolean(String node) {
		return BoolArgumentType.getBool(context, node);
	}

}
