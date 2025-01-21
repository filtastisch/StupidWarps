package eu.filtastisch.stupidwarps.utils.gui;

import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import com.samjakob.spigui.menu.SGMenu;
import eu.filtastisch.stupidwarps.StupidWarps;
import eu.filtastisch.stupidwarps.utils.manager.WarpManager;
import eu.filtastisch.stupidwarps.utils.types.Warp;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.UUID;

public class WarpGUI {

    private final static StupidWarps plugin = StupidWarps.getInstance();

    public static void openWarpGUI(Player player) {
        if (!player.hasPermission("stupidwarps.warps")) {
            return;
        }
        SGMenu warpGui = plugin.getSpiGUI().create("§aWarps", 5);
        warpGui.setAutomaticPaginationEnabled(true);
        warpGui.setToolbarBuilder(new WarpGUIToolbar(player));

        WarpManager.getWarps(WarpManager.reversedOrder.contains(player.getUniqueId())).forEach(warp -> {

            ItemStack icon = new ItemBuilder(warp.getIcon()).name("§a" + warp.getDisplayName().replaceAll("&", "§")).lore(getLore(warp)).build();

            SGButton button = new SGButton(icon).withListener(e -> {
                player.teleportAsync(warp.getLocation());
                player.sendMessage(plugin.getDefaultConfig().getMsgWarpTeleported(warp));
            });

            warpGui.addButton(button);
        });
        player.openInventory(warpGui.getInventory());
    }

    public static String[] getLore(Warp warp) {
        int x = warp.getLocation().getBlockX();
        int y = warp.getLocation().getBlockY();
        int z = warp.getLocation().getBlockZ();
        String world = warp.getLocation().getWorld().getName();
        long time = warp.getCreationTime();
        UUID creator = warp.getCreator();
        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern(StupidWarps.getInstance().getDefaultConfig().getTimeFormat())
                .withLocale(Locale.GERMAN)
                .withZone(ZoneId.systemDefault());

        String locationText = "    §e├ §dLocation: §eX: §a" + x + " §eY: §a" + y + " §eZ: §a" + z;
        String worldText = "    §e├ §dWorld: §a" + world;
        String timeText = "    §e├ §dErstellt: §a" + formatter.format(Instant.ofEpochMilli(time));
        String playerNameText = "    §e└ §dCreator: §a" + Bukkit.getOfflinePlayer(creator).getName();

        return new String[]{locationText, worldText, timeText, playerNameText};
    }

}
