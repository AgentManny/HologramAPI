package gg.manny.hologram;

import gg.manny.hologram.line.HologramItemLine;
import gg.manny.hologram.line.HologramLine;
import gg.manny.hologram.line.HologramTextLine;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class HologramBuilder {

    private Location location;

    private List<HologramLine> lines = new ArrayList<>();

    private List<UUID> viewers = new ArrayList<>();

    public HologramBuilder() {

    }

    public HologramBuilder(UUID... viewers) {
        this.viewers.addAll(Arrays.asList(viewers));
    }

    public HologramBuilder location(Location location) {
        this.location = location;
        return this;
    }

    public HologramBuilder addLines(String... lines) {
        for (String text : lines) {
            this.lines.add(new HologramTextLine(text));
        }
        return this;
    }

    public HologramBuilder addLine(ItemStack item) {
        lines.add(new HologramItemLine(item));
        return this;
    }

    public HologramBuilder addViewer(UUID uuid) {
        viewers.add(uuid);
        return this;
    }

    public CraftHologram build() {
        if (location == null) throw new NullPointerException("Location cannot be null");
        CraftHologram hologram = new CraftHologram(location);
        hologram.getLines().addAll(lines);
        hologram.getViewers().addAll(viewers);
        hologram.send();
        return hologram;
    }

}
