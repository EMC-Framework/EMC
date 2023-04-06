package me.deftware.client.framework.item;

import me.deftware.client.framework.message.Message;
import me.deftware.client.framework.item.effect.StatusEffect;
import me.deftware.client.framework.item.enchantment.Enchantment;
import me.deftware.client.framework.item.types.SwordItem;
import me.deftware.client.framework.item.types.WeaponItem;
import me.deftware.client.framework.math.position.BlockPosition;
import me.deftware.client.framework.message.MessageUtils;
import me.deftware.client.framework.nbt.NbtCompound;
import me.deftware.client.framework.nbt.NbtList;
import me.deftware.client.framework.registry.EnchantmentRegistry;
import me.deftware.client.framework.registry.ItemRegistry;
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

	public static final ItemStack EMPTY = new ItemStack(null) {
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
		this.itemStack = new net.minecraft.item.ItemStack(net.minecraft.item.Item.getItemFromBlock(item.getMinecraftBlock()), size);
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
		return new ItemStack(null);
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
		NBTTagCompound tag = itemStack.getTagCompound();
		if (tag != null && tag.hasKey("Enchantments", 9)) {
			NBTTagList list = tag.getTagList("Enchantments", 10);
			if (!list.hasNoTags()) {
				// Found active enchantments
				if (enchantments.size() != list.tagCount()) {
					enchantments.clear();
					Map<Integer, Integer> stackEnchantments = EnchantmentHelper.getEnchantments(itemStack);
					for (Integer enchantment : stackEnchantments.keySet()) {
						EnchantmentRegistry.INSTANCE.enchantments.values().stream()
								.filter(e -> e.getMinecraftEnchantment().effectId == enchantment)
								.findFirst().ifPresent(e -> {
									enchantments.add(new Pair<>(e, stackEnchantments.get(enchantment)));
								});
					}
				}
				return enchantments;
			}
		}
		return Collections.emptyList();
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

	public Message getDisplayName() {
		return MessageUtils.parse(itemStack.getDisplayName());
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

	public boolean isEqualItems(ItemStack stack, boolean ignoreDamage) {
		return ignoreDamage ? getMinecraftItemStack().isItemEqual(stack.getMinecraftItemStack()) : getMinecraftItemStack().getIsItemStackEqual(stack.getMinecraftItemStack());
	}

	public boolean hasStatusEffect(StatusEffect effect) {
		return Items.potionitem.getEffects(itemStack).stream()
				.anyMatch(e -> e.getPotionID() == effect.getMinecraftStatusEffect().getId());
	}

	public void setStackDisplayName(Message name) {
		NBTTagCompound nbttagcompound = itemStack.getSubCompound("display", true);
		nbttagcompound.setString("Name", IChatComponent.Serializer.componentToJson((IChatComponent) name));
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
