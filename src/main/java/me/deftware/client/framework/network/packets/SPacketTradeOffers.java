package me.deftware.client.framework.network.packets;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.deftware.client.framework.network.PacketWrapper;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundMerchantOffersPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import java.util.Map;

public class SPacketTradeOffers extends PacketWrapper {

    public SPacketTradeOffers(Packet<?> packet) {
        super(packet);
    }

    public JsonObject getJson() {
        ClientboundMerchantOffersPacket packet = ((ClientboundMerchantOffersPacket) this.packet);
        JsonObject json = new JsonObject();
        // Villager meta
        json.addProperty("experience", packet.getVillagerXp());
        json.addProperty("level", packet.getVillagerLevel());
        JsonArray trades = new JsonArray();
        for (MerchantOffer offer : packet.getOffers()) {
            JsonObject trade = new JsonObject();
            // Items
            trade.add("first", getItem(offer.getCostA()));
            offer.getItemCostB().ifPresent(tradedItem
                    -> trade.add("second", getItem(tradedItem.itemStack())));
            trade.add("sell", getItem(offer.getResult()));
            // Meta
            trade.addProperty("xp", offer.getXp());
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
        json.addProperty("id", stack.getItem().getDescriptionId());

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
