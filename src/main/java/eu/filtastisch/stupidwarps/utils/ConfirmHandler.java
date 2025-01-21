package eu.filtastisch.lunarieBuildserverAdditions.utils;

import eu.filtastisch.lunarieBuildserverAdditions.LunarieBuildserverAdditions;
import eu.filtastisch.lunarieBuildserverAdditions.utils.manager.ConfirmManager;
import eu.filtastisch.lunarieBuildserverAdditions.utils.manager.WarpManager;
import eu.filtastisch.lunarieBuildserverAdditions.utils.types.Warp;
import lombok.Getter;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

@Getter
public class ConfirmHandler {

    private BukkitTask bukkitTask;
    private final UUID player;
    private final Warp warp;

    public ConfirmHandler(UUID player, Warp warp) {
        this.player = player;
        this.warp = warp;
        startTimer();
    }

    private void startTimer() {
        bukkitTask = new BukkitRunnable() {
            @Override
            public void run() {
                ConfirmManager.CONFIRM_HANDLERS.remove(player);
            }
        }.runTaskLaterAsynchronously(LunarieBuildserverAdditions.getInstance(), 20*60);
    }

    public void stopConfirm() {
        bukkitTask.cancel();
        ConfirmManager.CONFIRM_HANDLERS.remove(player);
        LunarieBuildserverAdditions.getInstance().getWarpConfig().removeWarp(warp);
    }

}
