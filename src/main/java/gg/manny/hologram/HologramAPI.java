package gg.manny.hologram;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class HologramAPI {

    @Getter private static final Map<String, Hologram> holograms = new HashMap<>();

    public static Optional<Hologram> getHologram(String id) {
        return holograms.values().stream()
                .filter((hologram) -> hologram.getId().equalsIgnoreCase(id))
                .findAny();
    }

    public static void register(Hologram hologram) {
        holograms.put(hologram.getId(), hologram);
    }
}
