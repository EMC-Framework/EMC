package me.deftware.client.framework.item;

import me.deftware.client.framework.chat.ChatMessage;
import me.deftware.client.framework.conversion.ConvertedList;
import me.deftware.client.framework.item.effect.AppliedStatusEffect;
import me.deftware.client.framework.item.effect.StatusEffect;
import me.deftware.client.framework.item.enchantment.Enchantment;
import me.deftware.client.framework.item.types.SwordItem;
import me.deftware.client.framework.item.types.WeaponItem;
import me.deftware.client.framework.math.position.BlockPosition;
import me.deftware.client.framework.nbt.NbtCompound;
import me.deftware.client.framework.nbt.NbtList;
import me.deftware.client.framework.registry.EnchantmentRegistry;
import me.deftware.client.framework.util.types.Pair;
import me.deftware.client.framework.world.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;

import java.util.*;

/**
 * @author Deftware
 */
public class ItemStack {

	public static final ItemStack EMPTY = new ItemStack(null);

	protected ConvertedList<AppliedStatusEffect, PotionEffect> statusEffects;
	protected final List<Pair<Enchantment, Integer>> enchantments = new ArrayList<>();
	protected final net.minecraft.item.ItemStack itemStack;
	protected Item item;

	public ItemStack(IItem item, int size) {
		this(Item.newInstance(item.getAsItem()), size);
	}

	public ItemStack(Block item, int size) {
		this.itemStack = new net.minecraft.item.ItemStack(net.minecraft.item.Item.getItemFromBlock(item.getMinecraftBlock()), size);
		this.item = Item.newInstance(itemStack.getItem());
		initConversions();
	}

	public ItemStack(Item item, int size) {
		this.itemStack = new net.minecraft.item.ItemStack(item.getMinecraftItem(), size);
		this.item = item;
		initConversions();
	}

	public ItemStack(net.minecraft.item.ItemStack itemStack) {
		this.itemStack = itemStack;
		if (itemStack != null) {
			this.item = Item.newInstance(itemStack.getItem());
			initConversions();
		}
	}

	private void initConversions() {
		this.statusEffects = new ConvertedList<>(() -> Items.potionitem.getEffects(itemStack), null, AppliedStatusEffect::new);
	}

	public net.minecraft.item.ItemStack getMinecraftItemStack() {
		return itemStack;
	}

	public Item getItem() {
		return item;
	}

	public List<Pair<Enchantment, Integer>> getEnchantments() {
		Map<Integer, Integer> stackEnchantments = EnchantmentHelper.getEnchantments(itemStack);
		/*if (enchantments.size() != stackEnchantments.size()) { FIXME
			for (int id : stackEnchantments.keySet()) {
				EnchantmentRegistry.INSTANCE.find(enchantment.getName()).ifPresent(e ->
						enchantments.add(new Pair<>(e, stackEnchantments.get(enchantment)))
				);
			}
		}*/
		return enchantments;
	}

	public int getStackProtectionAmount() {
		int protection = EnchantmentHelper.getEnchantmentModifierDamage(new net.minecraft.item.ItemStack[] {itemStack}, DamageSource.generic);
		if (item.getMinecraftItem() instanceof ItemArmor) {
			protection += ((ItemArmor) item.getMinecraftItem()).damageReduceAmount;
		}
		return protection;
	}

	public float getStackAttackDamage() {
		float damage = EnchantmentHelper.getModifierForCreature(itemStack, EnumCreatureAttribute.UNDEFINED);
		if (item instanceof SwordItem) {
			damage += ((SwordItem) item).getAttackDamage();
		} else if (item instanceof WeaponItem) {
			damage += ((WeaponItem) item).getAttackDamage();
		}
		return damage;
	}

	public boolean isEnchantable() {
		return itemStack.isItemEnchantable();
	}

	public boolean isDamageable() {
		return itemStack.isItemStackDamageable();
	}

	public boolean isDamaged() {
		return itemStack.isItemDamaged();
	}

	public boolean isEmpty() {
		return item == null || item.getID() == 0 || item.isAir();
	}

	public int getMaxSize() {
		return itemStack.getMaxStackSize();
	}

	public int getCount() {
		return itemStack.stackSize;
	}

	public void setCount(int count) {
		itemStack.stackSize = count;
	}

	public int getDamage() {
		return MathHelper.clamp_int(getRawDamage(), 0, getMaxDamage());
	}

	public int getRawDamage() {
		return itemStack.getItemDamage();
	}

	public int getMaxDamage() {
		return Math.max(getRawMaxDamage(), 0);
	}

	public int getRawMaxDamage() {
		return itemStack.getMaxDamage();
	}

	public ChatMessage getDisplayName() {
		return new ChatMessage().fromString(itemStack.getDisplayName());
	}

	public float getStrVsBlock(BlockPosition pos) {
		return itemStack.getStrVsBlock(Objects.requireNonNull(net.minecraft.client.Minecraft.getMinecraft().theWorld).getBlockState(pos.getMinecraftBlockPos()).getBlock());
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof ItemStack) {
			return net.minecraft.item.ItemStack.areItemsEqual(getMinecraftItemStack(), ((ItemStack) object).getMinecraftItemStack());
		}
		return false;
	}

	public List<AppliedStatusEffect> getAppliedStatusEffects() {
		return statusEffects.poll();
	}

	public boolean isEqualItems(ItemStack stack, boolean ignoreDamage) {
		return ignoreDamage ? getMinecraftItemStack().isItemEqual(stack.getMinecraftItemStack()) : getMinecraftItemStack().getIsItemStackEqual(stack.getMinecraftItemStack());
	}

	public boolean hasStatusEffect(StatusEffect effect) {
		return Items.potionitem.getEffects(itemStack).stream()
				.anyMatch(e -> e.getPotionID() == effect.getMinecraftStatusEffect().getId());
	}

	public void setStackDisplayName(String name) {
		NBTTagCompound nbttagcompound = itemStack.getSubCompound("display", true);
		nbttagcompound.setString("Name", IChatComponent.Serializer.componentToJson(new ChatComponentText(name)));
	}
	
	public void addEnchantment(Enchantment enchantment, int level) {
		itemStack.addEnchantment(enchantment.getMinecraftEnchantment(), level);
	}

	public int getRarity() {
		if (itemStack.getRarity() == EnumRarity.COMMON) {
			return 0;
		} else if (itemStack.getRarity() == EnumRarity.UNCOMMON) {
			return 1;
		} else if (itemStack.getRarity() == EnumRarity.RARE) {
			return 2;
		} else if (itemStack.getRarity() == EnumRarity.EPIC) {
			return 3;
		}
		return 0;
	}

	private static RenderItem getRenderItem() {
		return net.minecraft.client.Minecraft.getMinecraft().getRenderItem();
	}

	public static void setRenderZLevel(float z) {
		getRenderItem().zLevel = z;
	}

	public void renderItemIntoGUI(int x, int y) {
		getRenderItem().renderItemIntoGUI(getMinecraftItemStack(), x, y);
		RenderHelper.disableStandardItemLighting();
	}

	public void renderItemOverlays(int x, int y) {
		getRenderItem().renderItemOverlays(Minecraft.getMinecraft().fontRendererObj, getMinecraftItemStack(), x, y);
		RenderHelper.disableStandardItemLighting();
	}

	public void renderItemAndEffectIntoGUI(int x, int y) {
		getRenderItem().renderItemAndEffectIntoGUI(getMinecraftItemStack(), x, y);
		RenderHelper.disableStandardItemLighting();
	}

	public void renderItemOverlayIntoGUI(int x, int y, String text) {
		getRenderItem().renderItemOverlayIntoGUI(net.minecraft.client.Minecraft.getMinecraft().fontRendererObj, getMinecraftItemStack(), x, y, text);
		RenderHelper.disableStandardItemLighting();
	}

	public boolean hasNbt() {
		return itemStack.hasTagCompound();
	}

	public NbtCompound getNbt() {
		return new NbtCompound(itemStack.getTagCompound());
	}

	public void setNbtList(String key, NbtList compound) {
		itemStack.setTagInfo(key, compound.getMinecraftListTag());
	}

	public static ArrayList<ItemStack> loadAllItems(NbtCompound compound, int size) {
		ItemStack[] objects = new ItemStack[size];
		Arrays.fill(objects, ItemStack.EMPTY);
		List<ItemStack> list = Arrays.asList(objects);
		NBTTagList itemTag = compound.getMinecraftCompound().getTagList("Items", 10);
		for(int index = 0; index < itemTag.tagCount(); index++) {
			NBTTagCompound item = itemTag.getCompoundTagAt(index);
			int slotData = item.getByte("Slot") & 255;
			if (slotData < list.size()) {
				list.set(slotData, new ItemStack(net.minecraft.item.ItemStack.loadItemStackFromNBT(new NbtCompound(item).getMinecraftCompound())));
			}
		}
		return new ArrayList<>(list);
	}

}
