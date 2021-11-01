package gg.manny.hologram.line;

import gg.manny.hologram.HologramPlugin;
import gg.manny.hologram.entity.DummyEntityHorse;
import net.minecraft.server.v1_8_R3.DataWatcher;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntity;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static gg.manny.hologram.util.DataWatcherHelper.*;

public class HologramTextLine extends HologramLine {

    private final String text;

    public HologramTextLine(Location location, String text) {
        super(location);
        this.text = text;
    }

    @Override
    public List<Packet<?>> getPacketsFor(Player player) {
        List<Packet<?>> packets = new ArrayList<>();
        boolean legacy = HologramPlugin.onLegacyVersion(player);
        if (legacy) {
             packets.add(getSkullPacket(location));
            packets.add(getHorsePacket());
        } else {
            // Add armor stands
            player.sendMessage(ChatColor.RED + "Your Protocol version isn't supported yet.");
        }
        return packets;
    }

    // TODO eventually use reflections
    public PacketPlayOutSpawnEntity getSkullPacket(Location location) {
        DummyEntityHorse entity = new DummyEntityHorse(((CraftWorld) location.getWorld()).getHandle(), text); // LMFAO
        PacketPlayOutSpawnEntity spawnPacket = new PacketPlayOutSpawnEntity(entity.getId(),
                location.getX(),
                location.getY() - 0.13 + 55.0,
                location.getZ(),
                0, 0, 0,
                (int) location.getPitch(),
                (int) location.getYaw(),
                WITHER_SKULL_PROJECTILE_ID, 0
        );
        return spawnPacket;
    }

    private void setHorseToArmorStand() {
        // TODO Set #getSkullPacket to Armor Stand
    }

    public PacketPlayOutSpawnEntityLiving getHorsePacket() {
        DummyEntityHorse entity = new DummyEntityHorse(((CraftWorld) location.getWorld()).getHandle(), text);
        Bukkit.broadcastMessage(ChatColor.YELLOW + "Entity id: " + entity.getId());
        DataWatcher watcher = new DataWatcher(entity);
        watcher.a(INVISIBILITY.getId(), (byte) 0);
        watcher.a(1, (short) 300); // Not sure

        watcher.a(CUSTOM_NAME.getId(), ChatColor.translateAlternateColorCodes('&', text));
        watcher.a(CUSTOM_NAME_VISIBLE.getId(), (byte) 1);

        // This will make the Horse invisible although it does not have
        // a collision box and the location won't be accurate
        // Which is why we use a Wither Projectile to ride the Horse (prevent it from falling)
        // and shouldn't be visible
        watcher.a(AGE.getId(), -1700000);

        //entity.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(entity.getId(), (byte) EntityType.HORSE.getTypeId(), location.getX(), location.getY(), location.getZ(), entity.yaw, entity.pitch, entity.getHeadRotation(), 0, 0, 0, watcher);

//        // Try this
//        entity.locX = location.getX();
//        entity.locY = location.getY();
//        entity.locZ = location.getZ();
//        entity.yaw = location.getYaw();
//        entity.pitch = location.getPitch();
//        entity.getHeadRotation();
//        packet = new PacketPlayOutSpawnEntityLiving(entity);
//        try {
//            ReflectionUtils.setValue(packet, true, "l", watcher);
//        } catch (IllegalAccessException | NoSuchFieldException e) {
//            e.printStackTrace();
//        }
        // TODO Use reflections to manipulate changes
        return packet;
    }
}
