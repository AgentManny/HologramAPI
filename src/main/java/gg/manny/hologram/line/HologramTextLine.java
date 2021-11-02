package gg.manny.hologram.line;

import gg.manny.hologram.HologramPlugin;
import gg.manny.hologram.util.EntityUtils;
import gg.manny.hologram.util.ReflectionUtils;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
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
        dataWatcher.watch(CUSTOM_NAME.getId(), text);
        dataWatcher.watch(CUSTOM_NAME_VISIBLE.getId(), (byte) 1);

    }

    @Override
    public List<Packet<?>> getPacketsFor(Player player) {
        List<Packet<?>> packets = new ArrayList<>();
        PacketPlayOutSpawnEntityLiving spawnPacket = getSpawnPacket(location);
        boolean legacy = HologramPlugin.getInstance().onLegacyVersion(player);
        if (legacy) {
            setAsLegacyPacket(spawnPacket); // Sets as horse
            packets.add(getSkullPacket(location, OFFSET_HORSE));
            packets.add(new PacketPlayOutAttachEntity(armorStandId, skullId, false));
        }
        packets.add(0, spawnPacket);
        return packets;
    }

    private void setAsLegacyPacket(PacketPlayOutSpawnEntityLiving packet) {
        DataWatcher dataWatcher = EntityUtils.getDataWatcher();
        dataWatcher.a(INVISIBILITY.getId(), (byte) 0);
        dataWatcher.a(1, (short) 300); // Not sure
        dataWatcher.a(CUSTOM_NAME.getId(), text);
        dataWatcher.a(CUSTOM_NAME_VISIBLE.getId(), (byte) 1);

        // This will make the Horse invisible, although it does not have
        // a collision box and the location won't be accurate
        // Which is why we use a Wither Projectile to ride the Horse (prevent it from falling)
        // and shouldn't be visible
        dataWatcher.a(AGE.getId(), -1700000);
        try {
            ReflectionUtils.setValue(packet, true, "b", (int) EntityType.HORSE.getTypeId());
            ReflectionUtils.setValue(packet, true, "c", MathHelper.floor((location.getY() - 0.13 + OFFSET_HORSE) * 32.0D)); // Update yPos for horse
            ReflectionUtils.setValue(packet, true, "l", dataWatcher);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    // TODO Send destroy packet
    public void destroy() {

    }

    // TODO send update metadata packet
    public void update() {

    }

    // TODO send teleport packet
    public void teleport(Location location) {

    }

    public void setLocation(Location location) {
//        this.location = location;
        teleport(location);
    }
}