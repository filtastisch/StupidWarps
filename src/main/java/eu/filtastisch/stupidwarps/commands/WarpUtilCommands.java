package eu.filtastisch.lunarieBuildserverAdditions.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import eu.filtastisch.lunarieBuildserverAdditions.LunarieBuildserverAdditions;
import eu.filtastisch.lunarieBuildserverAdditions.utils.ConfirmHandler;
import eu.filtastisch.lunarieBuildserverAdditions.utils.gui.WarpGUI;
import eu.filtastisch.lunarieBuildserverAdditions.utils.manager.ConfirmManager;
import eu.filtastisch.lunarieBuildserverAdditions.utils.manager.WarpManager;
import eu.filtastisch.lunarieBuildserverAdditions.utils.types.Warp;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class WarpUtilCommands {

    private final LunarieBuildserverAdditions plugin;

    public WarpUtilCommands() {
        plugin = LunarieBuildserverAdditions.getInstance();
        this.registerSetWarpCommand();
        this.registerDeleteWarpCommand();
        this.registerListWarpsCommand();
    }

    public void registerSetWarpCommand() {
        new CommandAPICommand("setwarp")
                .withPermission("lunarie.build.setwarp")
                .withArguments(new StringArgument("warpname"))
                .executesPlayer((player, args) -> {
                    String warpName = (String) args.get("warpname");

                    if (WarpManager.warpExists(warpName)) {
                        player.sendMessage(plugin.getDefaultConfig().getMsgWarpAlreadyExist(WarpManager.getWarp(warpName)));
                        return;
                    }

                    Warp warp = new Warp(warpName, player.getLocation(), Material.GRASS_BLOCK, System.currentTimeMillis(), player.getUniqueId());
                    WarpManager.addWarp(warp);
                    plugin.getWarpConfig().addWarp(warp);
                    player.sendMessage(plugin.getDefaultConfig().getMsgWarpCreateSuccess(warp));
                }).register("lunarie");
    }

    public void registerDeleteWarpCommand() {
        new CommandTree("delwarp")
                .withPermission("lunarie.build.delwarp")
                .then(new StringArgument("warpname")
                        .replaceSuggestions(ArgumentSuggestions.strings(info -> WarpManager
                                .getWarps(WarpManager.reversedOrder.contains(((Player) info.sender()).getUniqueId()))
                                .stream().map(Warp::name).toArray(String[]::new)))
                        .executesPlayer((player, args) -> {
                            String warpName = (String) args.get("warpname");
                            if (!WarpManager.warpExists(warpName)) {
                                player.sendMessage(plugin.getDefaultConfig().getMsgWarpNotFound(warpName));
                                return;
                            }
                            ConfirmManager.CONFIRM_HANDLERS.put(player.getUniqueId(),
                                    new ConfirmHandler(player.getUniqueId(), WarpManager.getWarp(warpName)));
                            player.sendMessage(plugin.getDefaultConfig().getMsgWarpDelete());
                        }))
                .then(new LiteralArgument("confirm")
                        .withPermission("lunarie.build.delwarp.confirm")
                        .executesPlayer((player, args) -> {
                            ConfirmHandler confirmHandler = ConfirmManager.CONFIRM_HANDLERS.get(player.getUniqueId());
                            if (confirmHandler == null) {
                                player.sendMessage(plugin.getDefaultConfig().getMsgWarpNoConfirmation());
                                return;
                            }
                            if (confirmHandler.getWarp() == null) {
                                player.sendMessage(plugin.getDefaultConfig().getMsgWarpNoConfirmation());
                                return;
                            }
                            String warpName = confirmHandler.getWarp().name();
                            confirmHandler.stopConfirm();
                            player.sendMessage(plugin.getDefaultConfig().getMsgDeleteWarpSuccess(warpName));
                        }))
                .register("lunarie");
    }

    public void registerListWarpsCommand() {
        new CommandAPICommand("warps")
                .withPermission("lunarie.build.warps")
                .executesPlayer((player, args) -> {
                    WarpGUI.openWarpGUI(player);
                }).register("lunarie");
    }

}
