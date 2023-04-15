package me.deftware.client.framework.item;

import me.deftware.client.framework.entity.effect.Effect;
import me.deftware.client.framework.math.BlockPosition;
import me.deftware.client.framework.message.Message;
import me.deftware.client.framework.item.types.SwordItem;
import me.deftware.client.framework.item.types.WeaponItem;
import me.deftware.client.framework.nbt.NbtCompound;
import me.deftware.client.framework.nbt.NbtList;
import me.deftware.client.framework.registry.EnchantmentRegistry;
import me.deftware.client.framework.registry.ItemRegistry;
import me.deftware.client.framework.util.types.Pair;
import me.deftware.client.framework.world.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.IRegistry;
import net.minecraft.util.text.ITextComponent;

import java.util.*;

/**
 * @author Deftware
 */
public class ItemStack {

	public static final ItemStack EMPTY = new ItemStack(net.minecraft.item.ItemStack.EMPTY) {
		@Override
		public ItemStack setStack(net.minecraft.item.ItemStack itemStack) {
			if (item != null)
				throw new IllegalStateException("Cannot update reference of global empty stack!");
			return super.setStack(itemStack);
		}
	};

	protected final List<Pair<Enchantment, Integer>> enchantments = new ArrayList<>();

	protected net.minecraft.item.ItemStack itemStack;
	protected Item item;

	public ItemStack(IItem item, int size) {
		this(ItemRegistry.INSTANCE.getItem(item.getAsItem()), size);
	}

	public ItemStack(Block item, int size) {
		this.itemStack = new net.minecraft.item.ItemStack(item.getMinecraftBlock().asItem(), size);
		this.item = ItemRegistry.INSTANCE.getItem(itemStack.getItem());
	}

	public ItemStack(Item item, int size) {
		this.itemStack = new net.minecraft.item.ItemStack(item.getMinecraftItem(), size);
		this.item = item;
	}

	public ItemStack(net.minecraft.item.ItemStack itemStack) {
		setStack(itemStack);
	}

	public ItemStack setStack(net.minecraft.item.ItemStack itemStack) {
		if (itemStack != null) {
			this.itemStack = itemStack;
			this.item = ItemRegistry.INSTANCE.getItem(itemStack.getItem());
		}
		return this;
	}

	public static ItemStack getEmpty() {
		return new ItemStack(net.minecraft.item.ItemStack.EMPTY);
	}

	public static void init(List<net.minecraft.item.ItemStack> original, List<ItemStack> stack) {
		for (int i = 0; i < original.size(); i++)
			stack.set(i, new ItemStack(original.get(i)));
	}

	public static void copyReferences(Iterable<net.minecraft.item.ItemStack> original, List<ItemStack> stack) {
		int index = 0;
		for (net.minecraft.item.ItemStack item : original)
			stack.get(index++).setStack(item);
	}

	public net.minecraft.item.ItemStack getMinecraftItemStack() {
		return itemStack;
	}

	public Item getItem() {
		return item;
	}

	public List<Pair<Enchantment, Integer>> getEnchantments() {
		NBTTagCompound tag = itemStack.getTag();
		if (tag != null && tag.contains("Enchantments", 9)) {
			NBTTagList list = tag.getList("Enchantments", 10);
			if (!list.isEmpty()) {
				// Found active enchantments
				if (enchantments.size() != list.size()) {
					enchantments.clear();
					Map<net.minecraft.enchantment.Enchantment, Integer> stackEnchantments = EnchantmentHelper.getEnchantments(itemStack);
					for (net.minecraft.enchantment.Enchantment enchantment : stackEnchantments.keySet()) {
						EnchantmentRegistry.INSTANCE.find(enchantment.getName()).ifPresent(e ->
								enchantments.add(new Pair<>(e, stackEnchantments.get(enchantment)))
						);
					}
				}
				return enchantments;
			}
		}
		return Collections.emptyList();
	}

	public int getStackProtectionAmount() {
		int protection = EnchantmentHelper.getEnchantmentModifierDamage(Collections.singletonList(itemStack), DamageSource.GENERIC);
		if (item.getMinecraftItem() instanceof ItemArmor) {
			protection += ((ItemArmor) item.getMinecraftItem()).getDamageReduceAmount();
		}
		return protection;
	}

	public float getStackAttackDamage() {
		float damage = EnchantmentHelper.getModifierForCreature(itemStack, CreatureAttribute.UNDEFINED);
		if (item instanceof SwordItem) {
			damage += ((SwordItem) item).getAttackDamage();
		} else if (item instanceof WeaponItem) {
			damage += ((WeaponItem) item).getAttackDamage();
		}
		return damage;
	}

	public boolean isEnchantable() {
		return itemStack.isEnchantable();
	}

	public boolean isDamageable() {
		return itemStack.isDamageable();
	}

	public boolean isDamaged() {
		return itemStack.isDamaged();
	}

	public boolean isEmpty() {
		return item.getID() == 0 || item.isAir();
	}

	public int getMaxSize() {
		return itemStack.getMaxStackSize();
	}

	public int getCount() {
		return itemStack.getCount();
	}

	public void setCount(int count) {
		itemStack.setCount(count);
	}

	public int getDamage() {
		return MathHelper.clamp(getRawDamage(), 0, getMaxDamage());
	}

	public int getRawDamage() {
		return itemStack.getDamage();
	}

	public int getMaxDamage() {
		return Math.max(getRawMaxDamage(), 0);
	}

	public int getRawMaxDamage() {
		return itemStack.getMaxDamage();
	}

	public Message getDisplayName() {
		return (Message) itemStack.getDisplayName();
	}

	public float getStrVsBlock(BlockPosition pos) {
		return itemStack.getDestroySpeed(Objects.requireNonNull(net.minecraft.client.Minecraft.getInstance().world).getBlockState((BlockPos) pos));
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof ItemStack) {
			return net.minecraft.item.ItemStack.areItemsEqualIgnoreDurability(getMinecraftItemStack(), ((ItemStack) object).getMinecraftItemStack());
		}
		return false;
	}

	public boolean isEqualItems(ItemStack stack, boolean ignoreDamage) {
		return ignoreDamage ? getMinecraftItemStack().isItemEqual(stack.getMinecraftItemStack()) : getMinecraftItemStack().isItemEqualIgnoreDurability(stack.getMinecraftItemStack());
	}

	public boolean hasStatusEffect(Effect effect) {
		return PotionUtils.getEffectsFromStack(itemStack).stream()
				.anyMatch(e -> e.getPotion() == (Potion) effect);
	}

	public void setStackDisplayName(Message name) {
		NBTTagCompound nbttagcompound = itemStack.getOrCreateChildTag("display");
		nbttagcompound.putString("Name", ITextComponent.Serializer.toJson((ITextComponent) name));
	}
	
	public void addEnchantment(Enchantment enchantment, int level) {
		NBTTagCompound nbt = itemStack.getOrCreateTag();
		if (!nbt.contains("Enchantments", 9)) {
			nbt.put("Enchantments", new NBTTagList());
		}
		NBTTagList list = nbt.getList("Enchantments", 10);
		NBTTagCompound nbtCompound = new NBTTagCompound();
		nbtCompound.putString("id", String.valueOf(IRegistry.ENCHANTMENT.getKey((net.minecraft.enchantment.Enchantment) enchantment)));
		nbtCompound.putShort("lvl", (short) level);
		list.add(nbtCompound);
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

	private static ItemRenderer getRenderItem() {
		return net.minecraft.client.Minecraft.getInstance().getItemRenderer();
	}

	public static void setRenderZLevel(float z) {
		getRenderItem().zLevel = z;
	}

	public void renderItemIntoGUI(int x, int y) {
		getRenderItem().renderItemIntoGUI(getMinecraftItemStack(), x, y);
		RenderHelper.disableStandardItemLighting();
	}

	public void renderItemOverlays(int x, int y) {
		getRenderItem().renderItemOverlays(Minecraft.getInstance().fontRenderer, getMinecraftItemStack(), x, y);
		RenderHelper.disableStandardItemLighting();
	}

	public void renderItemAndEffectIntoGUI(int x, int y) {
		getRenderItem().renderItemAndEffectIntoGUI(getMinecraftItemStack(), x, y);
		RenderHelper.disableStandardItemLighting();
	}

	public void renderItemOverlayIntoGUI(int x, int y, String text) {
		getRenderItem().renderItemOverlayIntoGUI(net.minecraft.client.Minecraft.getInstance().fontRenderer, getMinecraftItemStack(), x, y, text);
		RenderHelper.disableStandardItemLighting();
	}

	public boolean hasNbt() {
		return itemStack.hasTag();
	}

	public NbtCompound getNbt() {
		return new NbtCompound(itemStack.getTag());
	}

	public void setNbtList(String key, NbtList compound) {
		itemStack.setTagInfo(key, compound.getMinecraftListTag());
	}

	public static ArrayList<ItemStack> loadAllItems(NbtCompound compound, int size) {
		NonNullList<ItemStack> list = NonNullList.withSize(size, ItemStack.EMPTY);
		NBTTagList itemTag = compound.getMinecraftCompound().getList("Items", 10);
		for(int index = 0; index < itemTag.size(); index++) {
			NBTTagCompound item = itemTag.getCompound(index);
			int slotData = item.getByte("Slot") & 255;
			if (slotData < list.size()) {
				list.set(slotData, new ItemStack(net.minecraft.item.ItemStack.read(new NbtCompound(item).getMinecraftCompound())));
			}
		}
		return new ArrayList<>(list);
	}

}
