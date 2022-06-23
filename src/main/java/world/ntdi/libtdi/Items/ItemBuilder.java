package world.ntdi.libtdi.Items;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ItemBuilder {
    private ItemStack is;
    public ItemBuilder(Material m){
        this(m, 1);
    }
    public ItemBuilder(ItemStack is){
        this.is=is;
    }
    public ItemBuilder(Material m, int amount){
        is= new ItemStack(m, amount);
    }
    public ItemBuilder(Material m, int amount, short durability){
        is = new ItemStack(m, amount);
        is.setDurability(durability);
    }
    public ItemBuilder clone(){
        return new ItemBuilder(is);
    }
    public ItemBuilder setDurability(short dur){
        is.setDurability(dur);
        return this;
    }
    public ItemBuilder setName(String name){
        ItemUtils.rename(is, name);
        return this;
    }
    public ItemBuilder addUnsafeEnchantment(Enchantment ench, int level){
        is.addUnsafeEnchantment(ench, level);
        return this;
    }
    public ItemBuilder removeEnchantment(Enchantment ench){
        is.removeEnchantment(ench);
        return this;
    }
    public ItemBuilder addEnchant(Enchantment ench, int level){
        ItemUtils.addEnchant(is, ench, level);
        return this;
    }
    public ItemBuilder addEnchantments(Map<Enchantment, Integer> enchantments){
        is.addEnchantments(enchantments);
        return this;
    }
    public ItemBuilder setUnbreakable(){
        ItemUtils.setUnbreakable(is);
        return this;
    }
    public ItemBuilder setLore(String... lore){
        ItemUtils.setLore(is, lore);
        return this;
    }
    public ItemBuilder setLore(List<String> lore) {
        ItemUtils.setLore(is, lore);
        return this;
    }
    public ItemBuilder removeLoreLine(String line){
        ItemUtils.removeLore(is, line);
        return this;
    }
    public ItemBuilder removeLoreLine(int index){
        ItemUtils.removeLore(is, index);
        return this;
    }
    public ItemBuilder addLoreLine(String line){
        ItemUtils.addLore(is, line);
        return this;
    }
    public ItemBuilder addLoreLine(String line, int pos){
        ItemUtils.addLore(is, line, pos);
        return this;
    }
    @SuppressWarnings("deprecation")
    public ItemBuilder setDyeColor(DyeColor color){
        this.is.setDurability(color.getDyeData());
        return this;
    }
    @Deprecated
    public ItemBuilder setWoolColor(DyeColor color){
        if(!is.getType().equals(Material.LEGACY_WOOL))return this;
        this.is.setDurability(color.getWoolData());
        return this;
    }
    public ItemBuilder setLeatherArmorColor(Color color){
        try{
            LeatherArmorMeta im = (LeatherArmorMeta)is.getItemMeta();
            im.setColor(color);
            is.setItemMeta(im);
        }catch(ClassCastException expected){}
        return this;
    }
    public ItemBuilder addAttribute(Attribute attribute, AttributeModifier modifier) {
        ItemUtils.addAttribute(is, attribute, modifier);
        return this;
    }

    public ItemBuilder addAttribute(Attribute attribute, double amount, AttributeModifier.Operation operation) {
        ItemUtils.addAttribute(is, attribute, amount, operation);
        return this;
    }

    public ItemBuilder addAttribute(Attribute attribute, double amount, AttributeModifier.Operation operation, EquipmentSlot slot) {
        ItemUtils.addAttribute(is, attribute, amount, operation, slot);
        return this;
    }

    public ItemBuilder addItemFlags(ItemFlag... flags) {
        ItemUtils.addItemFlags(is, flags);
        return this;
    }

    public ItemBuilder addDamage(int damage) {
        ItemUtils.damage(is, damage);
        return this;
    }

    public ItemBuilder setCustomModelData(int customModelData) {
        ItemUtils.setCustomModelData(is, customModelData);
        return this;
    }

    public <T, Z> ItemBuilder addPersistentTag(NamespacedKey key, PersistentDataType<T, Z> type, Z data) {
        ItemUtils.addPersistentTag(is, key, type, data);
        return this;
    }


    public ItemStack build(){
        return is;
    }
}
