package world.ntdi.libtdi.Items;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import world.ntdi.libtdi.Json.JSONMap;
import world.ntdi.libtdi.Json.JSONParser;
import world.ntdi.libtdi.LibTdi;

import java.util.*;
import java.util.function.BiPredicate;

public class ItemUtils {
    private static Material skullType;

    public ItemUtils() {
    }

    public static ItemStack skull(OfflinePlayer owner) {
        ItemStack base = getBaseSkull();
        SkullMeta meta = (SkullMeta)base.getItemMeta();
        if (LibTdi.midVersion >= 13) {
            meta.setOwningPlayer(owner);
        } else {
            meta.setOwner(owner.getName());
        }

        base.setItemMeta(meta);
        return base;
    }

    private static ItemStack getBaseSkull() {
        return LibTdi.midVersion >= 13 ? new ItemStack(skullType) : new ItemStack(skullType, 1, (short)3);
    }

    public static ItemStack rename(ItemStack item, String name) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack setName(ItemStack item, String name) {
        return rename(item, name);
    }

    public static ItemStack setLore(ItemStack item, String line) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList();
        lore.add(line);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack setLore(ItemStack item, List<String> lore) {
        ItemMeta meta = item.getItemMeta();
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack addLore(ItemStack item, String line) {
        ItemMeta meta = item.getItemMeta();
        List<String> lor = meta.getLore();
        List<String> lore = lor == null ? new ArrayList() : lor;
        ((List)lore).add(line);
        meta.setLore((List)lore);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack addLore(ItemStack item, Iterable<String> lines) {
        ItemMeta meta = item.getItemMeta();
        List<String> lor = meta.getLore();
        List<String> lore = lor == null ? new ArrayList() : lor;
        Objects.requireNonNull(lore);
        lines.forEach(lore::add);
        meta.setLore((List)lore);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack addLore(ItemStack item, String line, int pos) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>(meta.getLore());
        lore.set(pos, line);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack removeLore(ItemStack item, String line) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>(meta.getLore());
        if(!lore.contains(line))return item;
        lore.remove(line);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack removeLore(ItemStack item, int index) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>(meta.getLore());
        if(index<0||index>lore.size())return item;
        lore.remove(index);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack setLore(ItemStack item, String... lore) {
        return setLore(item, Arrays.asList(lore));
    }

    public static ItemStack setUnbreakable(ItemStack item) {
        item = item.clone();
        ItemMeta meta = item.getItemMeta();
        meta.setUnbreakable(true);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack addEnchant(ItemStack item, Enchantment enchant, int level) {
        ItemMeta meta = item.getItemMeta();
        meta.addEnchant(enchant, level, true);
        if (level == 0) {
            meta.removeEnchant(enchant);
        }

        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack addAttribute(ItemStack item, Attribute attribute, AttributeModifier modifier) {
        ItemMeta meta = item.getItemMeta();
        meta.addAttributeModifier(attribute, modifier);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack addAttribute(ItemStack item, Attribute attribute, double amount, AttributeModifier.Operation operation) {
        ItemMeta meta = item.getItemMeta();
        AttributeModifier modifier = new AttributeModifier(attribute.toString(), amount, operation);
        meta.addAttributeModifier(attribute, modifier);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack addItemFlags(ItemStack item, ItemFlag... flags) {
        ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(flags);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack setCustomModelData(ItemStack item, int customModelData) {
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(customModelData);
        item.setItemMeta(meta);
        return item;
    }

    public static <T, Z> ItemStack addPersistentTag(ItemStack item, NamespacedKey key, PersistentDataType<T, Z> type, Z data) {
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(key, type, data);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack addAttribute(ItemStack item, Attribute attribute, double amount, AttributeModifier.Operation operation, EquipmentSlot slot) {
        ItemMeta meta = item.getItemMeta();
        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), attribute.toString(), amount, operation, slot);
        meta.addAttributeModifier(attribute, modifier);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack damage(ItemStack item, int amount) {
        if (LibTdi.midVersion >= 13) {
            ItemMeta meta = item.getItemMeta();
            if (!(meta instanceof Damageable)) {
                throw new IllegalArgumentException("Item must be damageable");
            } else {
                Damageable d = (Damageable)meta;
                d.setDamage(d.getDamage() + amount);
                item.setItemMeta(meta);
                return item;
            }
        } else {
            item.setDurability((short)(item.getDurability() + amount));
            return item;
        }
    }

    public static int count(Inventory inv, ItemStack item, BiPredicate<ItemStack, ItemStack> comparison) {
        int count = 0;
        ListIterator var4 = inv.iterator();

        while(var4.hasNext()) {
            ItemStack i = (ItemStack)var4.next();
            if (comparison.test(item, i)) {
                count += i.getAmount();
            }
        }

        return count;
    }

    public static int count(Inventory inv, ItemStack item) {
        return count(inv, item, ItemStack::isSimilar);
    }


    public static boolean remove(Inventory inv, ItemStack item, int amount, BiPredicate<ItemStack, ItemStack> comparison) {
        ItemStack[] contents = inv.getContents();

        for(int i = 0; i < contents.length && amount > 0; ++i) {
            if (comparison.test(item, contents[i])) {
                if (amount < contents[i].getAmount()) {
                    contents[i].setAmount(contents[i].getAmount() - amount);
                    inv.setContents(contents);
                    return true;
                }

                amount -= contents[i].getAmount();
                contents[i] = null;
                if (amount == 0) {
                    inv.setContents(contents);
                    return true;
                }
            }
        }

        inv.setContents(contents);
        return false;
    }

    public static boolean remove(Inventory inv, ItemStack item, int amount) {
        return remove(inv, item, amount, ItemStack::isSimilar);
    }

    public static int countAndRemove(Inventory inv, ItemStack item, int max, BiPredicate<ItemStack, ItemStack> comparison) {
        int count = count(inv, item, comparison);
        count = Math.min(max, count);
        remove(inv, item, count, comparison);
        return count;
    }

    public static int countAndRemove(Inventory inv, ItemStack item, int max) {
        return countAndRemove(inv, item, max, ItemStack::isSimilar);
    }

    public static int countAndRemove(Inventory inv, ItemStack item) {
        return countAndRemove(inv, item, Integer.MAX_VALUE, ItemStack::isSimilar);
    }

    public static void give(Player player, ItemStack... items) {
        player.getInventory().addItem(items).values().forEach((i) -> {
            player.getWorld().dropItem(player.getLocation(), i);
        });
    }

    public static void give(Player player, ItemStack item, int amount) {
        if (amount < 1) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        } else {
            ItemStack clone;
            for(int stackSize = item.getType().getMaxStackSize(); amount > stackSize; amount -= stackSize) {
                clone = item.clone();
                clone.setAmount(stackSize);
                give(player, clone);
            }

            clone = item.clone();
            clone.setAmount(amount);
            give(player, clone);
        }
    }

    public static void give(Player player, Material type, int amount) {
        give(player, new ItemStack(type), amount);
    }

    public static Inventory cloneInventory(Inventory inv) {
        ItemStack[] contents = new ItemStack[inv.getSize()];

        for(int i = 0; i < inv.getSize(); ++i) {
            ItemStack item = inv.getItem(i);
            if (item != null) {
                contents[i] = item.clone();
            }
        }

        return new MockInventory(contents, inv.getHolder(), inv.getType());
    }

    public static int minimumChestSize(int items) {
        return (int)Math.max(9.0, Math.ceil((double)items / 9.0) * 9.0);
    }

    public static String toString(ItemStack item) {
        return item == null ? null : ItemSerializer.toJSON(item, ItemStack.class).toString();
    }

    public static ItemStack fromString(String json) {
        JSONMap map = JSONParser.parseMap(json);
        return (ItemStack)ItemSerializer.recursiveDeserialize(map);
    }

    static {
        skullType = LibTdi.midVersion >= 13 ? Material.valueOf("PLAYER_HEAD") : Material.valueOf("SKULL_ITEM");
    }
}
