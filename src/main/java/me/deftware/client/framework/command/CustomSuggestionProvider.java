package me.deftware.client.framework.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class CustomSuggestionProvider implements SuggestionProvider {

	@Override
	public CompletableFuture<Suggestions> getSuggestions(CommandContext context, SuggestionsBuilder builder) throws CommandSyntaxException {
		return suggest(new ArrayList<>(), new SuggestionsBuilder("", 0));
	}

	static CompletableFuture<Suggestions> suggest(Iterable<String> p_197005_0_, SuggestionsBuilder p_197005_1_)
	{
		String s = p_197005_1_.getRemaining().toLowerCase(Locale.ROOT);

		for (String s1 : p_197005_0_)
		{
			if (s1.toLowerCase(Locale.ROOT).startsWith(s))
			{
				p_197005_1_.suggest(s1);
			}
		}

		return p_197005_1_.buildFuture();
	}

}
