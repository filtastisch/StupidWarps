package eu.filtastisch.stupidwarps.storage;

import de.thesourcecoders.capi.config.GenericConfig;
import eu.filtastisch.stupidwarps.StupidWarps;
import eu.filtastisch.stupidwarps.utils.types.Warp;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;

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
            msgWarpNoConfirmationRaw,
            msgEditWarpNameRaw,
            msgEditWarpIconRaw,
            timeFormat;
    private Component prefix,
            msgWarpDelete,
            msgWarpDeleteSuccess,
            msgWarpNotFound,
            msgWarpTeleported,
            msgWarpCreateSuccess,
            msgWarpAlreadyExist,
            msgWarpNoConfirmation,
            msgEditWarpName,
            msgEditWarpIcon;

    public DefaultConfig() {
        this.config = GenericConfig.loadFromResourceConfig("config.yml", StupidWarps.getInstance());
        this.loadValues();
        this.setComponents();
    }

    private void loadValues() {
        this.prefixRaw = config.getString("prefix");

        this.msgWarpDeleteRaw = this.config.getString("messages.delete-warp");
        this.msgWarpDeleteSuccessRaw = this.config.getString("messages.delete-success-warp");
        this.msgWarpNotFoundRaw = this.config.getString("messages.not-found-warp");
        this.msgWarpTeleportedRaw = this.config.getString("messages.teleported-warp");
        this.msgWarpCreateSuccessRaw = this.config.getString("messages.create-warp");
        this.msgWarpAlreadyExistRaw = this.config.getString("messages.already-exists-warp");
        this.msgWarpNoConfirmationRaw = this.config.getString("messages.no-confirmation-warp");
        this.msgEditWarpNameRaw = this.config.getString("messages.edit-name-warp");
        this.msgEditWarpIconRaw = this.config.getString("messages.edit-icon-warp");

        this.timeFormat = this.config.getString("time-format");
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
        this.msgEditWarpName = MiniMessage.miniMessage().deserialize(msgEditWarpNameRaw);
        this.msgEditWarpIcon = MiniMessage.miniMessage().deserialize(msgEditWarpIconRaw);
    }

    public Component getMsgWarpNoConfirmation() {
        return prefix.append(msgWarpNoConfirmation);
    }

    public Component getMsgDeleteWarp(Warp warp) {
        return getComponent(warp, msgWarpDelete);
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
        return getComponent(warp, msgWarpTeleported);
    }

    public Component getMsgWarpCreateSuccess(Warp warp) {
        return getComponent(warp, msgWarpCreateSuccess);
    }

    public Component getMsgEditWarpIcon(Warp warp) {
        return getComponent(warp, msgEditWarpIcon);
    }

    public Component getMsgEditWarpName(Warp warp) {
        return getComponent(warp, msgEditWarpName);
    }

    public Component getMsgWarpAlreadyExist(Warp warp) {
        return getComponent(warp, msgWarpAlreadyExist);
    }

    private Component getComponent(Warp warp, Component message) {
        return prefix.append(message
                .replaceText(msg -> msg.match("%warp_icon%").replacement(
                        Component.text(warp.getIcon().name())
                                .hoverEvent(getHoverMessage(warp))
                                .clickEvent(ClickEvent.runCommand("/warp " + warp.getName()))
                ))
                .replaceText(msg -> msg.match("%warp_name%").replacement(
                        Component.text(warp.getName())
                                .hoverEvent(getHoverMessage(warp))
                                .clickEvent(ClickEvent.runCommand("/warp " + warp.getName()))
                ))
                .replaceText(msg -> msg.match("%warp_display_name%").replacement(
                        LegacyComponentSerializer.legacyAmpersand().deserialize(warp.getName())
                                .hoverEvent(getHoverMessage(warp))
                                .clickEvent(ClickEvent.runCommand("/warp " + warp.getName()))
                ))
        );
    }

    public Component getHoverMessage(Warp warp) {
        int x = warp.getLocation().getBlockX();
        int y = warp.getLocation().getBlockY();
        int z = warp.getLocation().getBlockZ();
        String world = warp.getLocation().getWorld().getName();
        long time = warp.getCreationTime();
        UUID creator = warp.getCreator();
        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern(this.getTimeFormat())
                .withLocale(Locale.GERMAN)
                .withZone(ZoneId.systemDefault());

        Component locationText = LegacyComponentSerializer.legacyAmpersand().deserialize("    §e├ §dLocation: §eX: §a" + x + " §eY: §a" + y + " §eZ: §a" + z);
        Component worldText = LegacyComponentSerializer.legacyAmpersand().deserialize("    §e├ §dWorld: §a" + world);
        Component timeText = LegacyComponentSerializer.legacyAmpersand().deserialize("    §e├ §dDate: §a" + formatter.format(Instant.ofEpochMilli(time)));
        Component playerNameText = LegacyComponentSerializer.legacyAmpersand().deserialize("    §e└ §dCreator: §a" + Bukkit.getOfflinePlayer(creator).getName());

        return Component.text()
                .append(MiniMessage.miniMessage().deserialize("<b><green>%warp_name%:</green></b>")
                        .replaceText(replace -> replace
                                .match("%warp_name%")
                                .replacement(LegacyComponentSerializer
                                        .legacyAmpersand()
                                        .deserialize(warp.getDisplayName())
                                )))
                .appendNewline()
                .append(locationText).appendNewline()
                .append(worldText).appendNewline()
                .append(timeText).appendNewline()
                .append(playerNameText).build();
    }

}
