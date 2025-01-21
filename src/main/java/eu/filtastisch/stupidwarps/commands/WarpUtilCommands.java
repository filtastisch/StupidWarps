package eu.filtastisch.stupidwarps.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.*;
import eu.filtastisch.stupidwarps.StupidWarps;
import eu.filtastisch.stupidwarps.utils.ConfirmHandler;
import eu.filtastisch.stupidwarps.utils.gui.WarpGUI;
import eu.filtastisch.stupidwarps.utils.manager.ConfirmManager;
import eu.filtastisch.stupidwarps.utils.manager.WarpManager;
import eu.filtastisch.stupidwarps.utils.types.Warp;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class WarpUtilCommands {

    private final StupidWarps plugin;

    public WarpUtilCommands() {
        plugin = StupidWarps.getInstance();
        this.registerSetWarpCommand();
        this.registerDeleteWarpCommand();
        this.registerListWarpsCommand();
        this.registerEditWarp();
    }

    public void registerSetWarpCommand() {
        new CommandAPICommand("setwarp")
                .withPermission("stupidwarps.setwarp")
                .withArguments(new StringArgument("warpname"))
                .executesPlayer((player, args) -> {
                    String warpName = (String) args.get("warpname");

                    if (WarpManager.warpExists(warpName)) {
                        player.sendMessage(plugin.getDefaultConfig().getMsgWarpAlreadyExist(WarpManager.getWarp(warpName)));
                        return;
                    }

                    Warp warp = new Warp(warpName, player.getLocation(), System.currentTimeMillis(), player.getUniqueId());
                    warp.setIcon(player.getInventory().getItemInMainHand().getType() == Material.AIR ?
                            Material.GRASS_BLOCK : player.getInventory().getItemInMainHand().getType());
                    warp.setDisplayName(warpName);
                    WarpManager.addWarp(warp);
                    plugin.getWarpConfig().addWarp(warp);
                    player.sendMessage(plugin.getDefaultConfig().getMsgWarpCreateSuccess(warp));
                }).register();
    }

    public void registerDeleteWarpCommand() {
        new CommandTree("delwarp")
                .withPermission("stupidwarps.delwarp")
                .then(new StringArgument("warpname")
                        .replaceSuggestions(ArgumentSuggestions.strings(info -> WarpManager
                                .getWarps(WarpManager.reversedOrder.contains(((Player) info.sender()).getUniqueId()))
                                .stream().map(Warp::getName).toArray(String[]::new)))
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
                        .withPermission("stupidwarps.delwarp.confirm")
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
                            String warpName = confirmHandler.getWarp().getName();
                            confirmHandler.stopConfirm();
                            player.sendMessage(plugin.getDefaultConfig().getMsgDeleteWarpSuccess(warpName));
                        }))
                .register();
    }

    public void registerListWarpsCommand() {
        new CommandAPICommand("warps")
                .withPermission("stupidwarps.warps")
                .executesPlayer((player, args) -> {
                    WarpGUI.openWarpGUI(player);
                }).register();
    }

    public void registerEditWarp() {
        new CommandAPICommand("editwarp")
                .withPermission("stupidwarps.editwarp")
                .withSubcommand(this.getEditIconCommand())
                .withSubcommand(this.getEditDisplayNameCommand())
                .register();
    }

    public CommandAPICommand getEditIconCommand() {
        return new CommandAPICommand("icon")
                .withArguments(new StringArgument("warpname")
                        .replaceSuggestions(ArgumentSuggestions.strings(info -> WarpManager
                                .getWarps(WarpManager.reversedOrder.contains(((Player) info.sender()).getUniqueId()))
                                .stream().map(Warp::getName).toArray(String[]::new))))
                .withOptionalArguments(new ItemStackArgument("item"))
                .executesPlayer((player, args) -> {
                    player.getItemOnCursor().getType();
                    String warpName = (String) args.get("warpname");
                    ItemStack icon = (ItemStack) args.get("item");
                    Warp warp = WarpManager.getWarp(warpName);
                    if (icon != null) {
                        warp.setIcon(icon.getType());
                    } else {
                        warp.setIcon(player.getInventory().getItemInMainHand().getType() == Material.AIR ?
                                Material.GRASS_BLOCK : player.getInventory().getItemInMainHand().getType());
                    }
                    WarpManager.updateWarp(warp);
                    plugin.getWarpConfig().setWarpIcon(warp);
                    player.sendMessage(plugin.getDefaultConfig().getMsgEditWarpIcon(warp));
                });
    }

    public CommandAPICommand getEditDisplayNameCommand(){
        return new CommandAPICommand("display-name")
                .withArguments(new StringArgument("warpname")
                        .replaceSuggestions(ArgumentSuggestions.strings(info -> WarpManager
                        .getWarps(WarpManager.reversedOrder.contains(((Player) info.sender()).getUniqueId()))
                        .stream().map(Warp::getName).toArray(String[]::new))))
                .withArguments(new GreedyStringArgument("display"))
                .executesPlayer((player, args) -> {
                    String warpName = (String) args.get("warpname");
                    String displayName = (String) args.get("display");
                    Warp warp = WarpManager.getWarp(warpName);
                    warp.setDisplayName(displayName);
                    WarpManager.updateWarp(warp);
                    plugin.getWarpConfig().setWarpDisplayName(warp);
                    player.sendMessage(plugin.getDefaultConfig().getMsgEditWarpName(warp));
                });
    }

}
