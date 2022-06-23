package world.ntdi.libtdi.Items;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

/*
    * https://github.com/Redempt/RedLib/blob/master/src/redempt/redlib/itemutils/ItemBuilder.java
    * Credit to Redempt for the original code.
    * I just wanted to have it in my lib alongside my ItemBuilder.
 */
public class RedBuilder extends ItemStack {
    public RedBuilder(Material material, int amount) {
        super(material, amount);
    }

    public RedBuilder(Material material) {
        super(material);
    }

    public RedBuilder(ItemStack item) {
        super(item);
    }

    public RedBuilder setCount(int amount) {
        this.setAmount(amount);
        return this;
    }

    public RedBuilder addEnchant(Enchantment enchant, int level) {
        ItemUtils.addEnchant(this, enchant, level);
        return this;
    }

    public ItemStack toItemStack() {
        return new ItemStack(this);
    }

    public RedBuilder setLore(String... lore) {
        ItemUtils.setLore(this, lore);
        return this;
    }

    public RedBuilder addLore(String line) {
        ItemUtils.addLore(this, line);
        return this;
    }

    public RedBuilder addLore(Iterable<String> lines) {
        ItemUtils.addLore(this, lines);
        return this;
    }

    public RedBuilder setName(String name) {
        ItemUtils.rename(this, name);
        return this;
    }

    public RedBuilder setDurability(int durability) {
        this.setDurability((short)durability);
        return this;
    }

    public RedBuilder addAttribute(Attribute attribute, AttributeModifier modifier) {
        ItemUtils.addAttribute(this, attribute, modifier);
        return this;
    }

    public RedBuilder addAttribute(Attribute attribute, double amount, AttributeModifier.Operation operation) {
        ItemUtils.addAttribute(this, attribute, amount, operation);
        return this;
    }

    public RedBuilder addAttribute(Attribute attribute, double amount, AttributeModifier.Operation operation, EquipmentSlot slot) {
        ItemUtils.addAttribute(this, attribute, amount, operation, slot);
        return this;
    }

    public RedBuilder addItemFlags(ItemFlag... flags) {
        ItemUtils.addItemFlags(this, flags);
        return this;
    }

    public RedBuilder addDamage(int damage) {
        ItemUtils.damage(this, damage);
        return this;
    }

    public RedBuilder setCustomModelData(int customModelData) {
        ItemUtils.setCustomModelData(this, customModelData);
        return this;
    }

    public <T, Z> RedBuilder addPersistentTag(NamespacedKey key, PersistentDataType<T, Z> type, Z data) {
        ItemUtils.addPersistentTag(this, key, type, data);
        return this;
    }

    public RedBuilder unbreakable() {
        ItemUtils.setUnbreakable(this);
        return this;
    }

    public ItemStack build() {
        return this;
    }
}

