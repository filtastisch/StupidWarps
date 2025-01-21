package eu.filtastisch.stupidwarps.storage;

import de.thesourcecoders.capi.config.GenericConfig;
import eu.filtastisch.stupidwarps.StupidWarps;
import eu.filtastisch.stupidwarps.utils.manager.WarpManager;
import eu.filtastisch.stupidwarps.utils.types.Warp;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.UUID;

public class WarpConfig {

    private final GenericConfig config;

    public WarpConfig() {
        this.config = GenericConfig.load(StupidWarps.getInstance(), "warps.yml");
        this.loadValues();
    }

    private void loadValues() {
        if (this.config.getConfigurationSection("warps") == null) {
            return;
        }
        for (String warpName : this.config.getConfigurationSection("warps").getKeys(false)) {
            double x = this.config.getDouble("warps." + warpName + ".location.x");
            double y = this.config.getDouble("warps." + warpName + ".location.y");
            double z = this.config.getDouble("warps." + warpName + ".location.z");
            World world = Bukkit.getWorld(this.config.getString("warps." + warpName + ".location.world"));
            UUID creator = this.config.getUUID("warps." + warpName + ".creator");
            Material icon = Material.valueOf(this.config.getString("warps." + warpName + ".icon"));
            long creationTime = this.config.getLong("warps." + warpName + ".creationTime");
            Location location;

            if ((this.config.get("warps." + warpName + ".location.pitch") != null) &&
                    (this.config.get("warps." + warpName + ".location.yaw") != null)){
                float yaw = this.config.getFloat("warps." + warpName + ".yaw");
                float pitch = this.config.getFloat("warps." + warpName + ".pitch");
                location = new Location(world, x, y, z, yaw, pitch);
            } else {
                location = new Location(world, x, y, z);
            }

            String displayName = this.config.getString("warps." + warpName + ".displayName") == null ?
                    warpName : this.config.getString("warps." + warpName + ".displayName");

            Warp warp = new Warp(warpName, location, creationTime, creator);
            warp.setIcon(icon);
            warp.setDisplayName(displayName);
            WarpManager.addWarp(warp);
        }
    }

    public void addWarp(Warp warp) {
        setWarp(warp);
        this.config.save();
    }

    private void setWarp(Warp warp) {
        this.config.set("warps." + warp.getName() + ".location.x", warp.getLocation().getX());
        this.config.set("warps." + warp.getName() + ".location.y", warp.getLocation().getY());
        this.config.set("warps." + warp.getName() + ".location.z", warp.getLocation().getZ());
        this.config.set("warps." + warp.getName() + ".location.yaw", warp.getLocation().getYaw());
        this.config.set("warps." + warp.getName() + ".location.pitch", warp.getLocation().getPitch());
        this.config.set("warps." + warp.getName() + ".location.world", warp.getLocation().getWorld().getName());
        this.config.set("warps." + warp.getName() + ".creator", warp.getCreator().toString());
        this.config.set("warps." + warp.getName() + ".creationTime", warp.getCreationTime());
        this.config.set("warps." + warp.getName() + ".icon", warp.getIcon().name());
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

    public void setWarpIcon(Warp warp){
        this.config.set("warps." + warp.getName() + ".icon", warp.getIcon().name());
        this.config.save();
    }

    public void setWarpDisplayName(Warp warp){
        this.config.set("warps." + warp.getName() + ".displayName", warp.getDisplayName());
        this.config.save();
    }

}
