package eu.filtastisch.stupidwarps.utils.types;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.UUID;

@Getter
@Setter
public class Warp {
    private final String name;
    private final Location location;
    private final long creationTime;
    private final UUID creator;

    private Material icon;
    private String displayName;

    public Warp(String name, Location location, long creationTime, UUID creator) {
        this.name = name;
        this.location = location;
        this.creationTime = creationTime;
        this.creator = creator;
    }
}
