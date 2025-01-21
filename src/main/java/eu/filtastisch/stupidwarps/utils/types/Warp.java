package eu.filtastisch.lunarieBuildserverAdditions.utils.types;

import org.bukkit.Location;
import org.bukkit.Material;

import java.util.UUID;

public record Warp(String name, Location location, Material icon, long creationTime, UUID creator) {
}
