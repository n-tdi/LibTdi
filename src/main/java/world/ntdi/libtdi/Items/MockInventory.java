package world.ntdi.libtdi.Items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

class MockInventory implements Inventory {
    private ItemStack[] items;
    private InventoryHolder holder;
    private InventoryType type;

    public MockInventory(ItemStack[] items, InventoryHolder holder, InventoryType type) {
        this.items = items;
        this.holder = holder;
        this.type = type;
    }

    public int getSize() {
        return this.items.length;
    }

    public int getMaxStackSize() {
        return 64;
    }

    public void setMaxStackSize(int i) {
    }

    public ItemStack getItem(int i) {
        return this.items[i];
    }

    public void setItem(int i, ItemStack itemStack) {
        this.items[i] = itemStack;
    }

    public HashMap<Integer, ItemStack> addItem(ItemStack... itemStacks) throws IllegalArgumentException {
        return null;
    }

    public HashMap<Integer, ItemStack> removeItem(ItemStack... itemStacks) throws IllegalArgumentException {
        return null;
    }

    public ItemStack[] getContents() {
        return this.items;
    }

    public void setContents(ItemStack[] itemStacks) throws IllegalArgumentException {
        this.items = itemStacks;
    }

    public ItemStack[] getStorageContents() {
        return this.items;
    }

    public void setStorageContents(ItemStack[] itemStacks) throws IllegalArgumentException {
        this.items = itemStacks;
    }

    public boolean contains(Material material) throws IllegalArgumentException {
        return Arrays.stream(this.items).anyMatch((i) -> {
            return i.getType() == material;
        });
    }

    public boolean contains(ItemStack itemStack) {
        return Arrays.stream(this.items).anyMatch((i) -> {
            return i.equals(itemStack);
        });
    }

    public boolean contains(Material material, int amount) throws IllegalArgumentException {
        return ((IntSummaryStatistics)Arrays.stream(this.items).filter((i) -> {
            return i.getType() == material;
        }).collect(Collectors.summarizingInt(ItemStack::getAmount))).getSum() >= (long)amount;
    }

    public boolean contains(ItemStack itemStack, int amount) {
        return Arrays.stream(this.items).filter((i) -> {
            return i.equals(itemStack);
        }).count() >= (long)amount;
    }

    public boolean containsAtLeast(ItemStack itemStack, int amount) {
        return ((IntSummaryStatistics)Arrays.stream(this.items).filter((i) -> {
            return i.equals(itemStack);
        }).collect(Collectors.summarizingInt(ItemStack::getAmount))).getSum() >= (long)amount;
    }

    public HashMap<Integer, ? extends ItemStack> all(Material material) throws IllegalArgumentException {
        HashMap<Integer, ItemStack> items = new HashMap();

        for(int i = 0; i < this.items.length; ++i) {
            if (this.items[i].getType() == material) {
                items.put(i, this.items[i]);
            }
        }

        return items;
    }

    public HashMap<Integer, ? extends ItemStack> all(ItemStack itemStack) {
        HashMap<Integer, ItemStack> items = new HashMap();

        for(int i = 0; i < this.items.length; ++i) {
            if (this.items[i].equals(itemStack)) {
                items.put(i, this.items[i]);
            }
        }

        return items;
    }

    public int first(Material material) throws IllegalArgumentException {
        for(int i = 0; i < this.items.length; ++i) {
            if (this.items[i].getType() == material) {
                return i;
            }
        }

        return -1;
    }

    public int first(ItemStack itemStack) {
        for(int i = 0; i < this.items.length; ++i) {
            if (this.items[i].equals(itemStack)) {
                return i;
            }
        }

        return -1;
    }

    public int firstEmpty() {
        for(int i = 0; i < this.items.length; ++i) {
            if (this.items[i] == null || this.items[i].getType() == Material.AIR) {
                return i;
            }
        }

        return -1;
    }

    public boolean isEmpty() {
        return this.firstEmpty() == -1;
    }

    public void remove(Material material) throws IllegalArgumentException {
        for(int i = 0; i < this.items.length; ++i) {
            if (this.items[i].getType() == material) {
                this.items[i] = null;
            }
        }

    }

    public void remove(ItemStack itemStack) {
        for(int i = 0; i < this.items.length; ++i) {
            if (this.items[i].equals(itemStack)) {
                this.items[i] = null;
            }
        }

    }

    public void clear(int i) {
        this.items[i] = null;
    }

    public void clear() {
        this.items = new ItemStack[this.items.length];
    }

    public List<HumanEntity> getViewers() {
        return new ArrayList();
    }

    public InventoryType getType() {
        return this.type;
    }

    public InventoryHolder getHolder() {
        return this.holder;
    }

    public ListIterator<ItemStack> iterator() {
        List<ItemStack> itemList = new ArrayList();
        Collections.addAll(itemList, this.items);
        return itemList.listIterator();
    }

    public ListIterator<ItemStack> iterator(int i) {
        List<ItemStack> itemList = new ArrayList();
        Collections.addAll(itemList, this.items);
        return itemList.listIterator(i);
    }

    public Location getLocation() {
        if (this.holder instanceof Entity) {
            return ((Entity)this.holder).getLocation();
        } else {
            return this.holder instanceof BlockState ? ((BlockState)this.holder).getLocation() : null;
        }
    }
}
