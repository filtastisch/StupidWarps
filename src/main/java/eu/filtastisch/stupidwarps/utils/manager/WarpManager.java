package eu.filtastisch.lunarieBuildserverAdditions.utils.manager;

import eu.filtastisch.lunarieBuildserverAdditions.utils.types.Warp;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class WarpManager {

    private static final List<Warp> WARPS = new ArrayList<>();
    public static final List<UUID> reversedOrder = new ArrayList<>();

    public static void addWarp(Warp warp) {
        WARPS.add(warp);
    }

    public static void removeWarp(Warp warp) {
        WARPS.remove(warp);
    }

    public static Warp getWarp(String name) {
        return WARPS.stream().filter(warp -> warp.name().equals(name)).findFirst().orElse(null);
    }

    public static boolean warpExists(String name) {
        return WARPS.stream().anyMatch(warp -> warp.name().equals(name));
    }

    public static void updateWarp(Warp warp) {
        WARPS.removeIf(w -> w.name().equals(warp.name()));
        WARPS.add(warp);
    }

    public static List<Warp> getWarps(boolean reverse) {
        if (reverse) {
            return WARPS.stream().sorted(Comparator.comparingLong(Warp::creationTime).reversed()).collect(Collectors.toList());
        }
        return WARPS.stream().sorted(Comparator.comparingLong(Warp::creationTime)).collect(Collectors.toList());
    }
}
