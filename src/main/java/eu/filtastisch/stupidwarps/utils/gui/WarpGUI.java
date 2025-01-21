package eu.filtastisch.lunarieBuildserverAdditions.utils.gui;

import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import com.samjakob.spigui.menu.SGMenu;
import eu.filtastisch.lunarieBuildserverAdditions.LunarieBuildserverAdditions;
import eu.filtastisch.lunarieBuildserverAdditions.utils.manager.WarpManager;
import eu.filtastisch.lunarieBuildserverAdditions.utils.types.Warp;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.swing.text.DateFormatter;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.UUID;

public class WarpGUI {

    private final static LunarieBuildserverAdditions plugin = LunarieBuildserverAdditions.getInstance();

    public static void openWarpGUI(Player player) {
        if (!player.hasPermission("lunarie.build.warps")) {
            return;
        }
        SGMenu warpGui = plugin.getSpiGUI().create("§aWarps", 5);
        warpGui.setAutomaticPaginationEnabled(true);
        warpGui.setToolbarBuilder(new WarpGUIToolbar(player));

        WarpManager.getWarps(WarpManager.reversedOrder.contains(player.getUniqueId())).forEach(warp -> {

            ItemStack icon = new ItemBuilder(warp.icon()).name("§a" + warp.name()).lore(getLore(warp)).build();

            SGButton button = new SGButton(icon).withListener(e -> {
                player.teleportAsync(warp.location());
                player.sendMessage(plugin.getDefaultConfig().getMsgWarpTeleported(warp));
            });

            warpGui.addButton(button);
        });
        player.openInventory(warpGui.getInventory());
    }

    public static String[] getLore(Warp warp) {
        int x = warp.location().getBlockX();
        int y = warp.location().getBlockY();
        int z = warp.location().getBlockZ();
        String world = warp.location().getWorld().getName();
        long time = warp.creationTime();
        UUID creator = warp.creator();
        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("'§a'dd'§7'.'§a'MM'§7'.'§a'yyyy '§a'HH'§7':'§a'mm '§dUhr'")
                .withLocale(Locale.GERMAN)
                .withZone(ZoneId.systemDefault());

        String locationText = "    §e├ §dLocation: §eX: §a" + x + " §eY: §a" + y + " §eZ: §a" + z;
        String worldText = "    §e├ §dWorld: §a" + world;
        String timeText = "    §e├ §dErstellt: §a" + formatter.format(Instant.ofEpochMilli(time));
        String playerNameText = "    §e└ §dCreator: §a" + Bukkit.getOfflinePlayer(creator).getName();

        return new String[]{locationText, worldText, timeText, playerNameText};
    }

}
