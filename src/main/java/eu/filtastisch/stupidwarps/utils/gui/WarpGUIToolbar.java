package eu.filtastisch.stupidwarps.utils.gui;

import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import com.samjakob.spigui.menu.SGMenu;
import com.samjakob.spigui.toolbar.SGToolbarBuilder;
import com.samjakob.spigui.toolbar.SGToolbarButtonType;
import eu.filtastisch.stupidwarps.utils.manager.WarpManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class WarpGUIToolbar implements SGToolbarBuilder {

    private final Player player;

    public WarpGUIToolbar(Player player) {
        this.player = player;
    }

    @Override
    public SGButton buildToolbarButton(int slot, int page, SGToolbarButtonType type, SGMenu menu) {

        ItemBuilder sortAsc = new ItemBuilder(Material.PAPER).name("§aSortierung").lore("§eAktuelle Sortierung: ", " §7- Aufsteigend");
        ItemBuilder sortDesc = new ItemBuilder(Material.PAPER).name("§aSortierung").lore("§eAktuelle Sortierung: ", " §7- Absteigend");


        SGButton sortButtonAsc = new SGButton(sortAsc.build()).withListener(event -> {
            event.setResult(Event.Result.DENY);
            WarpManager.reversedOrder.add(player.getUniqueId());
            WarpGUI.openWarpGUI(player);
        });

        SGButton sortButtonDesc = new SGButton(sortDesc.build()).withListener(event -> {
            event.setResult(Event.Result.DENY);
            WarpManager.reversedOrder.remove(player.getUniqueId());
            WarpGUI.openWarpGUI(player);
        });

        if (slot == 8){
            if (WarpManager.reversedOrder.contains(player.getUniqueId())) {
                return sortButtonDesc;
            }
            return sortButtonAsc;
        }

        switch (type) {
            case PREV_BUTTON:
                if (menu.getCurrentPage() > 0) return new SGButton(new ItemBuilder(Material.ARROW)
                        .name("&a&l← Vorherige Seite").build()
                ).withListener(event -> {
                    event.setResult(Event.Result.DENY);
                    menu.previousPage(event.getWhoClicked());
                });
                else return null;

            case CURRENT_BUTTON:
                return new SGButton(new ItemBuilder(Material.NAME_TAG)
                        .name("&7&lSeite " + (menu.getCurrentPage() + 1) + " von " + menu.getMaxPage())
                        .build()
                ).withListener(event -> event.setResult(Event.Result.DENY));

            case NEXT_BUTTON:
                if (menu.getCurrentPage() < menu.getMaxPage() - 1) return new SGButton(new ItemBuilder(Material.ARROW)
                        .name("&a&lNächste Seite →")
                        .build()
                ).withListener(event -> {
                    event.setResult(Event.Result.DENY);
                    menu.nextPage(event.getWhoClicked());
                });
                else return null;

            case UNASSIGNED:
            default:
                return null;
        }

    }
}
