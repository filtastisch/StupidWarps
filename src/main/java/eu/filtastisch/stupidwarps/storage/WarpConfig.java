package eu.filtastisch.lunarieBuildserverAdditions.storage;

import de.thesourcecoders.capi.config.GenericConfig;
import eu.filtastisch.lunarieBuildserverAdditions.LunarieBuildserverAdditions;
import eu.filtastisch.lunarieBuildserverAdditions.utils.manager.WarpManager;
import eu.filtastisch.lunarieBuildserverAdditions.utils.types.Warp;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.UUID;

public class WarpConfig {

    private final GenericConfig config;

    public WarpConfig() {
        this.config = GenericConfig.load(LunarieBuildserverAdditions.getInstance(), "warps.yml");
        this.loadValues();
    }

    private void loadValues() {
        if (this.config.getConfigurationSection("warps") == null) {
            return;
        }
        for (String warpName : this.config.getConfigurationSection("warps").getKeys(false)) {
            int x = this.config.getInt("warps." + warpName + ".x");
            int y = this.config.getInt("warps." + warpName + ".y");
            int z = this.config.getInt("warps." + warpName + ".z");
            World world = Bukkit.getWorld(this.config.getString("warps." + warpName + ".world"));
            UUID creator = this.config.getUUID("warps." + warpName + ".creator");
            Material icon = Material.valueOf(this.config.getString("warps." + warpName + ".icon"));
            long creationTime = this.config.getLong("warps." + warpName + ".creationTime");
            Location location = new Location(world, x, y, z);

            Warp warp = new Warp(warpName, location, icon, creationTime, creator);
            WarpManager.addWarp(warp);
        }
    }

    public void addWarp(Warp warp) {
        setWarp(warp);
        this.config.save();
    }

    private void setWarp(Warp warp) {
        this.config.set("warps." + warp.name() + ".x", warp.location().getBlockX());
        this.config.set("warps." + warp.name() + ".y", warp.location().getBlockY());
        this.config.set("warps." + warp.name() + ".z", warp.location().getBlockZ());
        this.config.set("warps." + warp.name() + ".world", warp.location().getWorld().getName());
        this.config.set("warps." + warp.name() + ".creator", warp.creator().toString());
        this.config.set("warps." + warp.name() + ".creationTime", warp.creationTime());
        this.config.set("warps." + warp.name() + ".icon", warp.icon().name());
    }

    private void setWarps(){
        for (Warp warp : WarpManager.getWarps(false)){
            this.setWarp(warp);
        }
        this.config.save();
    }

    public void removeWarp(Warp warp) {
        this.config.set("warps", null);
        this.config.save();
        WarpManager.removeWarp(warp);
        this.setWarps();
    }

}
