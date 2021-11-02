package gg.manny.hologram;

import gg.manny.hologram.line.HologramItemLine;
import gg.manny.hologram.line.HologramLine;
import gg.manny.hologram.line.HologramTextLine;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class CraftHologram implements Hologram {

    public static final double DISTANCE = 0.23; // Credits: filoghost

    private final UUID id;
    @NonNull private String name;

    private List<HologramLine> lines = new ArrayList<>();

    private List<UUID> viewers = new ArrayList<>(); // todo Change to concurrent? It'll be removing adding a lot

    @NonNull private Location location;

    @Override
    public void setLocation(Location location) {
        // Update location
        this.location = location;
    }

    @Override
    public void send() {

    }

    @Override
    public void remove() {

    }

    @Override
    public void addLines(String... lines) {
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            Location location = this.location.clone().add(0.0, this.lines.size() * i * DISTANCE, 0.0);
            this.lines.add(new HologramTextLine(location, line));
        }
    }

    @Override
    public void setLine(int id, String line) {

    }

    @Override
    public void addItem(ItemStack item) {
        this.lines.add(new HologramItemLine(location, item));
    }

    @Override
    public void setItem(int id, ItemStack item) {

    }

    @Override
    public List<String> getLines() {
        return null;
    }

    @Override
    public void sendTo(Player player) {
        if (!viewers.contains(player.getUniqueId())) {
            viewers.add(player.getUniqueId());
            for (HologramLine line : lines) {
                line.getPacketsFor(player).forEach(packet -> sendPacket(player, packet));
            }
        }
    }

    private void sendPacket(Player player, Packet<?> packet) {
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
    }
}