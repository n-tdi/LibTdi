package world.ntdi.libtdi.UI;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import world.ntdi.libtdi.LibTdi;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class GUI implements Listener {
    public static final ItemStack FILLER;
    private final Inventory inventory;
    private Set<Integer> openSlots;
    private Runnable onDestroy;
    private BiConsumer<InventoryClickEvent, List<Integer>> onClickOpenSlot;
    private Consumer<InventoryDragEvent> onDragOpenSlot;
    private Map<Integer, Button> buttons;
    private boolean returnItems;
    private boolean destroyOnClose;

    public GUI(Inventory inventory) {
        this.openSlots = new LinkedHashSet();
        this.onClickOpenSlot = (e, i) -> {};
        this.onDragOpenSlot = (e) -> {};
        this.buttons = new HashMap();
        this.returnItems = true;
        this.destroyOnClose = true;
        this.inventory = inventory;
        Bukkit.getPluginManager().registerEvents(this, LibTdi.getInstance());
    }

    public GUI(int size, String name) {
        this(Bukkit.createInventory((InventoryHolder)null, size, name));
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public void addButton(Button button, int slot) {
        button.setSlot(slot);
        this.inventory.setItem(slot, button.getItem());
        this.buttons.put(slot, button);
    }

    public void addButton(int slot, Button button) {
        this.addButton(button, slot);
    }

    public void addButton(Button button, int x, int y) {
        int slot = x + y * 9;
        this.addButton(button, slot);
    }

    public void fill(int start, int end, ItemStack item) {
        for(int i = start; i < end; ++i) {
            this.inventory.setItem(i, item == null ? null : item.clone());
        }

    }

    public void removeButton(Button button) {
        this.inventory.setItem(button.getSlot(), new ItemStack(Material.AIR));
        this.buttons.remove(button.getSlot());
    }

    public List<Button> getButtons() {
        return new ArrayList(this.buttons.values());
    }

    public Button getButton(int slot) {
        return (Button)this.buttons.get(slot);
    }

    public void clearSlot(int slot) {
        Button button = (Button)this.buttons.get(slot);
        if (button != null) {
            this.removeButton(button);
        } else {
            this.inventory.setItem(slot, new ItemStack(Material.AIR));
        }
    }

    public void update() {
        Iterator var1 = this.buttons.values().iterator();

        while(var1.hasNext()) {
            Button button = (Button)var1.next();
            this.inventory.setItem(button.getSlot(), button.getItem());
        }

    }

    public void openSlot(int slot) {
        this.openSlots.add(slot);
    }

    public void openSlots(int start, int end) {
        for(int i = start; i < end; ++i) {
            this.openSlots.add(i);
        }

    }

    public void openSlots(int x1, int y1, int x2, int y2) {
        for(int y = y1; y < y2; ++y) {
            for(int x = x1; x < x2; ++x) {
                this.openSlots.add(y * 9 + x);
            }
        }

    }

    public void closeSlot(int slot) {
        this.openSlots.remove(slot);
    }

    public void closeSlots(int start, int end) {
        for(int i = start; i < end; ++i) {
            this.openSlots.remove(i);
        }

    }

    public void closeSlots(int x1, int y1, int x2, int y2) {
        for(int y = y1; y < y2; ++y) {
            for(int x = x1; x < x2; ++x) {
                this.openSlots.remove(y * 9 + x);
            }
        }

    }

    public Set<Integer> getOpenSlots() {
        return this.openSlots;
    }

    public void open(Player player) {
        player.openInventory(this.inventory);
    }

    public boolean returnsItems() {
        return this.returnItems;
    }

    public void setReturnsItems(boolean returnItems) {
        this.returnItems = returnItems;
    }

    public boolean destroysOnClose() {
        return this.destroyOnClose;
    }

    public void setDestroyOnClose(boolean destroyOnClose) {
        this.destroyOnClose = destroyOnClose;
    }

    public void setOnDestroy(Runnable onDestroy) {
        this.onDestroy = onDestroy;
    }

    public void setOnClickOpenSlot(Consumer<InventoryClickEvent> handler) {
        this.onClickOpenSlot = (e, i) -> {
            handler.accept(e);
        };
    }

    public void setOnClickOpenSlot(BiConsumer<InventoryClickEvent, List<Integer>> handler) {
        this.onClickOpenSlot = handler;
    }

    public void destroy(Player lastViewer) {
        if (this.onDestroy != null) {
            this.onDestroy.run();
        }

        HandlerList.unregisterAll(this);
        if (this.returnItems && lastViewer != null) {
            Iterator var2 = this.openSlots.iterator();

            while(var2.hasNext()) {
                int slot = (Integer)var2.next();
                ItemStack item = this.inventory.getItem(slot);
                if (item != null) {
                    lastViewer.getInventory().addItem(new ItemStack[]{item}).values().forEach((i) -> {
                        lastViewer.getWorld().dropItem(lastViewer.getLocation(), i);
                    });
                }
            }
        }

        this.inventory.clear();
        this.buttons.clear();
    }

    public void destroy() {
        this.destroy((Player)null);
    }

    public void clear() {
        this.inventory.clear();
        this.buttons.clear();
    }

    public void setOnDragOpenSlot(Consumer<InventoryDragEvent> onDrag) {
        this.onDragOpenSlot = onDrag;
    }

    @EventHandler
    public void onDrag(InventoryDragEvent e) {
        List<Integer> slots = (List)e.getRawSlots().stream().filter((s) -> {
            return this.getInventory(e.getView(), s).equals(this.inventory);
        }).collect(Collectors.toList());
        if (slots.size() != 0) {
            if (!this.openSlots.containsAll(slots)) {
                e.setCancelled(true);
            } else {
                this.onDragOpenSlot.accept(e);
            }
        }
    }

    private Inventory getInventory(InventoryView view, int rawSlot) {
        return rawSlot < view.getTopInventory().getSize() ? view.getTopInventory() : view.getBottomInventory();
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (this.inventory.equals(e.getView().getTopInventory())) {
            if (e.getAction() == InventoryAction.COLLECT_TO_CURSOR && !e.getClickedInventory().equals(this.inventory)) {
                e.setCancelled(true);
            } else {
                if (!this.inventory.equals(e.getClickedInventory()) && e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                    if (this.openSlots.size() > 0) {
                        Map<Integer, ItemStack> slots = new HashMap();
                        int amount = e.getCurrentItem().getAmount();
                        Iterator var4 = this.openSlots.iterator();

                        while(var4.hasNext()) {
                            int slot = (Integer)var4.next();
                            if (amount <= 0) {
                                break;
                            }

                            ItemStack item = this.inventory.getItem(slot);
                            int max;
                            if (item == null) {
                                max = Math.min(amount, e.getCurrentItem().getType().getMaxStackSize());
                                amount -= max;
                                ItemStack clone = e.getCurrentItem().clone();
                                clone.setAmount(max);
                                slots.put(slot, clone);
                            } else if (e.getCurrentItem().isSimilar(item)) {
                                max = item.getType().getMaxStackSize() - item.getAmount();
                                int diff = Math.min(max, e.getCurrentItem().getAmount());
                                amount -= diff;
                                ItemStack clone = item.clone();
                                clone.setAmount(clone.getAmount() + diff);
                                slots.put(slot, clone);
                            }
                        }

                        if (slots.size() == 0) {
                            return;
                        }

                        this.onClickOpenSlot.accept(e, new ArrayList(slots.keySet()));
                        if (e.isCancelled()) {
                            return;
                        }

                        e.setCancelled(true);
                        ItemStack item = e.getCurrentItem();
                        item.setAmount(amount);
                        e.setCurrentItem(item);
                        Inventory var10001 = this.inventory;
                        Objects.requireNonNull(var10001);
                        slots.forEach(var10001::setItem);
                        Bukkit.getScheduler().scheduleSyncDelayedTask(LibTdi.getInstance(), () -> {
                            ((Player)e.getWhoClicked()).updateInventory();
                        });
                        return;
                    }

                    e.setCancelled(true);
                }

                if (e.getInventory().equals(e.getClickedInventory())) {
                    if (this.openSlots.contains(e.getSlot())) {
                        List<Integer> list = new ArrayList();
                        list.add(e.getSlot());
                        this.onClickOpenSlot.accept(e, list);
                        return;
                    }

                    e.setCancelled(true);
                    Button button = (Button)this.buttons.get(e.getSlot());
                    if (button != null) {
                        button.onClick(e);
                    }
                }

            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if (e.getInventory().equals(this.inventory) && this.destroyOnClose && e.getViewers().size() <= 1) {
            this.destroy((Player)e.getPlayer());
        }

    }

    static {
        FILLER = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
    }
}
