package world.ntdi.libtdi.UI;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class Button {
    protected ItemStack item;
    private int slot;

    public static Button createBasic(ItemStack item) {
        return new Button(item) {
            public void onClick(InventoryClickEvent e) {
                // Do nothing
            }
        };
    }

    public static Button create(ItemStack item, final Consumer<InventoryClickEvent> listener) {
        return new Button(item) {
            public void onClick(InventoryClickEvent e) {
                listener.accept(e);
            }
        };
    }

    public static Button create(ItemStack item, final BiConsumer<InventoryClickEvent, Button> listener) {
        return new Button(item) {
            public void onClick(InventoryClickEvent e) {
                listener.accept(e, this);
            }
        };
    }

    public Button(ItemStack item) {
        this.item = item;
    }

    public ItemStack getItem() {
        return this.item;
    }

    protected int getSlot() {
        return this.slot;
    }

    protected void setSlot(int slot) {
        this.slot = slot;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public abstract void onClick(InventoryClickEvent var1);
}
