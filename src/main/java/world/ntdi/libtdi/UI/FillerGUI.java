package world.ntdi.libtdi.UI;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import world.ntdi.libtdi.Items.RedBuilder;

import javax.annotation.Nullable;
import java.util.*;

public class FillerGUI {
    private GUI gui;
    private Boolean includeNextAndLastBTN;
    private int numSlots;
    private int pagesNeeded;
    private List<Button> buttons;
    private Map<Button, Integer> defaults;

    /**
     * Creates a new FillerGUI.
     * @param gui The GUI to fill
     * @param p The player to fill the GUI for
     * @param nextAndLastBTN Whether to include the next and last buttons
     * @param buttons The buttons to fill the GUI with
     * @param defaults The default buttons to be on every page of the gui. If null no defaults will be used.
     */
    public FillerGUI(GUI gui, Player p, boolean nextAndLastBTN, List<Button> buttons, @Nullable Map<Button, Integer> defaults) {
        if (gui.getInventory().getSize() < 3 * 9) {
            throw new IllegalArgumentException("GUI must have at least 3 rows of 9 slots.");
        }
        this.gui = gui;
        this.includeNextAndLastBTN = nextAndLastBTN;
        this.numSlots = gui.getInventory().getSize() - 26;
        this.pagesNeeded = (int) Math.ceil(buttons.size() / (double) (numSlots));
        this.buttons = buttons;
        this.defaults = defaults;
        pageChange(p, 0);
        gui.update();
        gui.open(p);
    }

    private void defaults(Player p, int page) {
        if (this.defaults != null) {
            for (Map.Entry<Button, Integer> entry : this.defaults.entrySet()) {
                gui.addButton(entry.getKey(), entry.getValue());
            }
        }
        if (includeNextAndLastBTN) {
            ItemStack stack = new RedBuilder(Material.ARROW).setName(ChatColor.GRAY + "yus").setUnbrealable().build();
            if (page != 0) {
                Button lastPage = Button.create(new RedBuilder(Material.ARROW).setName(ChatColor.GREEN + "Last Page"), (e) -> {
                    if((page - 1) * numSlots >= 0) pageChange(p, page - 1);
                });
                gui.addButton(this.buttons.size() - 9, lastPage);
            }

            if (page != pagesNeeded) {
                Button nextPage = Button.create(new RedBuilder(Material.ARROW).setName(ChatColor.GREEN + "Next Page"), (e) -> {
                    if((page + 1) * numSlots < this.buttons.size()) pageChange(p, page + 1);
                });

                gui.addButton(this.buttons.size() - 1, nextPage);
            }
        }
    }
    private void pageChange(Player p, int page) {
        defaults(p, page);

        int displacement = 10;
        for(int i = (page * numSlots); i < Math.min(numSlots + (page*numSlots), this.buttons.size()); i++) {
            gui.addButton(displacement, this.buttons.get(i));

            if((i+1) % 7 == 0) {
                displacement += 2;
            }
            displacement++;
        }

        gui.update();

        ItemStack stack = new RedBuilder(Material.ARROW).build();
    }
}
