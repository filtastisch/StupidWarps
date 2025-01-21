package eu.filtastisch.stupidwarps.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.StringArgument;
import eu.filtastisch.stupidwarps.StupidWarps;
import eu.filtastisch.stupidwarps.utils.manager.WarpManager;
import eu.filtastisch.stupidwarps.utils.types.Warp;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class WarpCommand {

    public WarpCommand() {
        StupidWarps plugin = StupidWarps.getInstance();
        new CommandAPICommand("warp")
                .withPermission("stupidwarps.warp")
                .withArguments(new StringArgument("warpName")
                        .replaceSuggestions(ArgumentSuggestions.strings(info -> WarpManager
                                .getWarps(WarpManager.reversedOrder.contains(((Player) info.sender()).getUniqueId()))
                                .stream().map(Warp::getName).toArray(String[]::new))))
                .executesPlayer((player, args) -> {
                    String warpName = (String) args.get("warpName");
                    if (!WarpManager.warpExists(warpName)) {
                        player.sendMessage(plugin.getDefaultConfig().getMsgWarpNotFound(warpName));
                        return;
                    }

                    player.setVelocity(new Vector(0, 0, 0));
                    player.teleportAsync(WarpManager.getWarp(warpName).getLocation());
                    player.sendMessage(plugin.getDefaultConfig().getMsgWarpTeleported(WarpManager.getWarp(warpName)));

                }).register();
    }

}
