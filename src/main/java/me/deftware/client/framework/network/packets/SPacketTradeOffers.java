package me.deftware.client.framework.network.packets;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.deftware.client.framework.network.PacketWrapper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.SetTradeOffersS2CPacket;
import net.minecraft.village.TradeOffer;

import java.util.Map;

public class SPacketTradeOffers extends PacketWrapper {

    public SPacketTradeOffers(Packet<?> packet) {
        super(packet);
    }

    public JsonObject getJson() {
        SetTradeOffersS2CPacket packet = ((SetTradeOffersS2CPacket) this.packet);
        JsonObject json = new JsonObject();
        // Villager meta
        json.addProperty("experience", packet.getExperience());
        json.addProperty("level", packet.getLevelProgress());
        JsonArray trades = new JsonArray();
        for (TradeOffer offer : packet.getOffers()) {
            JsonObject trade = new JsonObject();
            // Items
            trade.add("first", getItem(offer.getDisplayedFirstBuyItem()));
            offer.getSecondBuyItem().ifPresent(tradedItem
                    -> trade.add("second", getItem(tradedItem.itemStack())));
            trade.add("sell", getItem(offer.getSellItem()));
            // Meta
            trade.addProperty("xp", offer.getMerchantExperience());
            trade.addProperty("uses", offer.getUses());
            trade.addProperty("maxUses", offer.getMaxUses());
            trades.add(trade);
        }
        json.add("trades", trades);
        return json;
    }

    private JsonObject getItem(ItemStack stack) {
        JsonObject json = new JsonObject();
        json.addProperty("count", stack.getCount());
        json.addProperty("id", stack.getItem().getTranslationKey());

        JsonArray meta = new JsonArray();
        ((me.deftware.client.framework.item.ItemStack) stack).enchantments((level, enchantment) -> {
            meta.add(enchantment.getName(level).getString());
        });
        if (!meta.isEmpty()) {
            json.add("meta", meta);
        }
        return json;
    }

}
