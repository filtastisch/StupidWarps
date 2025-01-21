package eu.filtastisch.lunarieBuildserverAdditions.storage;

import de.thesourcecoders.capi.config.GenericConfig;
import eu.filtastisch.lunarieBuildserverAdditions.LunarieBuildserverAdditions;
import eu.filtastisch.lunarieBuildserverAdditions.utils.types.Warp;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.UUID;

@Getter
public class DefaultConfig {

    private final GenericConfig config;

    private String prefixRaw,
            msgWarpDeleteRaw,
            msgWarpDeleteSuccessRaw,
            msgWarpNotFoundRaw,
            msgWarpTeleportedRaw,
            msgWarpCreateSuccessRaw,
            msgWarpAlreadyExistRaw,
            msgWarpNoConfirmationRaw;
    private Component prefix,
            msgWarpDelete,
            msgWarpDeleteSuccess,
            msgWarpNotFound,
            msgWarpTeleported,
            msgWarpCreateSuccess,
            msgWarpAlreadyExist,
            msgWarpNoConfirmation;

    public DefaultConfig() {
        this.config = GenericConfig.loadFromResourceConfig("config.yml", LunarieBuildserverAdditions.getInstance());
        this.loadValues();
        this.setComponents();
    }

    private void loadValues() {
        this.prefixRaw = config.getString("prefix");

        this.msgWarpDeleteRaw = config.getString("messages.delete-warp");
        this.msgWarpDeleteSuccessRaw = config.getString("messages.delete-success-warp");
        this.msgWarpNotFoundRaw = config.getString("messages.not-found-warp");
        this.msgWarpTeleportedRaw = config.getString("messages.teleported-warp");
        this.msgWarpCreateSuccessRaw = config.getString("messages.create-warp");
        this.msgWarpAlreadyExistRaw = config.getString("messages.already-exists-warp");
        this.msgWarpNoConfirmationRaw = config.getString("messages.no-confirmation-warp");
    }

    public void setComponents() {
        this.prefix = MiniMessage.miniMessage().deserialize(prefixRaw);
        this.msgWarpDelete = MiniMessage.miniMessage().deserialize(msgWarpDeleteRaw);
        this.msgWarpNotFound = MiniMessage.miniMessage().deserialize(msgWarpNotFoundRaw);
        this.msgWarpDeleteSuccess = MiniMessage.miniMessage().deserialize(msgWarpDeleteSuccessRaw);
        this.msgWarpTeleported = MiniMessage.miniMessage().deserialize(msgWarpTeleportedRaw);
        this.msgWarpCreateSuccess = MiniMessage.miniMessage().deserialize(msgWarpCreateSuccessRaw);
        this.msgWarpAlreadyExist = MiniMessage.miniMessage().deserialize(msgWarpAlreadyExistRaw);
        this.msgWarpNoConfirmation = MiniMessage.miniMessage().deserialize(msgWarpNoConfirmationRaw);
    }

    public Component getMsgWarpNoConfirmation() {
        return prefix.append(msgWarpNoConfirmation);
    }

    public Component getMsgDeleteWarp(String warpName) {
        return prefix.append(msgWarpDelete.replaceText(replacement -> {
            replacement.match("%warp_name%")
                    .replacement(warpName);
        }));
    }

    public Component getMsgWarpNotFound(String warpName) {
        return prefix.append(msgWarpNotFound.replaceText(replacement -> {
            replacement.match("%warp_name%")
                    .replacement(warpName);
        }));
    }

    public Component getMsgDeleteWarpSuccess(String warpName) {
        return prefix.append(msgWarpDeleteSuccess.replaceText(replacement -> {
            replacement.match("%warp_name%")
                    .replacement(warpName);
        }));
    }

    public Component getMsgWarpTeleported(Warp warp) {
        return prefix.append(msgWarpTeleported.replaceText(replacement -> {
            replacement.match("%warp_name%")
                    .replacement(Component.text(warp.name()).hoverEvent(getMessage(warp))
                            .clickEvent(ClickEvent.runCommand("/warp " + warp.name())));
        }));
    }

    public Component getMsgWarpCreateSuccess(Warp warp) {
        return prefix.append(msgWarpCreateSuccess.replaceText(replacement -> {
            replacement.match("%warp_name%")
                    .replacement(Component.text(warp.name()).hoverEvent(getMessage(warp))
                            .clickEvent(ClickEvent.runCommand("/warp " + warp.name())));
        }));
    }

    public Component getMsgWarpAlreadyExist(Warp warp) {
        return prefix.append(msgWarpAlreadyExist.replaceText(replacement -> {
            replacement.match("%warp_name%")
                    .replacement(Component.text(warp.name()).hoverEvent(getMessage(warp))
                            .clickEvent(ClickEvent.runCommand("/warp " + warp.name())));
        }));
    }

    public static Component getMessage(Warp warp) {
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

        Component locationText = LegacyComponentSerializer.legacyAmpersand().deserialize("    §e├ §dLocation: §eX: §a" + x + " §eY: §a" + y + " §eZ: §a" + z);
        Component worldText = LegacyComponentSerializer.legacyAmpersand().deserialize("    §e├ §dWorld: §a" + world);
        Component timeText = LegacyComponentSerializer.legacyAmpersand().deserialize("    §e├ §dErstellt: §a" + formatter.format(Instant.ofEpochMilli(time)));
        Component playerNameText = LegacyComponentSerializer.legacyAmpersand().deserialize("    §e└ §dCreator: §a" + Bukkit.getOfflinePlayer(creator).getName());

        return Component.text()
                .append(MiniMessage.miniMessage().deserialize("<b><green>" + warp.name() + ":</green></b>")).appendNewline()
                .append(locationText).appendNewline()
                .append(worldText).appendNewline()
                .append(timeText).appendNewline()
                .append(playerNameText).build();
    }

}
