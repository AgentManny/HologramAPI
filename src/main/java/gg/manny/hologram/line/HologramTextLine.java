package gg.manny.hologram.line;

import gg.manny.hologram.HologramPlugin;
import gg.manny.hologram.entity.DummyEntityArmorStand;
import gg.manny.hologram.entity.DummyEntityWitherSkull;
import gg.manny.hologram.util.EntityUtils;
import gg.manny.hologram.util.ReflectionUtils;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static gg.manny.hologram.util.DataWatcherHelper.*;

public class HologramTextLine extends HologramLine {

    private final String text;

    private final int armorStandId;
    private final int skullId;
    private final DataWatcher dataWatcher;

    public HologramTextLine(Location location, String text) {
        super(location);
        this.text = text;

        WorldServer worldServer = ((CraftWorld) location.getWorld()).getHandle();

        DummyEntityArmorStand entity = new DummyEntityArmorStand(worldServer);
        armorStandId = entity.getId();
        dataWatcher = entity.getDataWatcher();
        dataWatcher.watch(CUSTOM_NAME.getId(), text);
        dataWatcher.watch(CUSTOM_NAME_VISIBLE.getId(), (byte) 1);


        DummyEntityWitherSkull witherSkull = new DummyEntityWitherSkull(worldServer);
        skullId = witherSkull.getId();
    }

    public PacketPlayOutSpawnEntityLiving getSpawnPacket(Location location) {
        return new PacketPlayOutSpawnEntityLiving(armorStandId, (byte) HologramLine.ARMOR_STAND_ID,
                location.getX(), location.getY(), location.getZ(),
                0, 0, 0,
                0, 0, 0,
                dataWatcher
        );
    }

    public PacketPlayOutSpawnEntity getSkullPacket(Location location) {
        PacketPlayOutSpawnEntity spawnPacket = new PacketPlayOutSpawnEntity(skullId,
                location.getX(),
                (location.getY() - 0.13) + 55.0,
                location.getZ(),
                0, 0, 0,
                (int) location.getPitch(),
                (int) location.getYaw(),
                WITHER_SKULL_PROJECTILE_ID, 0
        );
        return spawnPacket;
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
            ReflectionUtils.setValue(packet, true, "c", MathHelper.floor((location.getY() - 0.13 + HologramLine.OFFSET_HORSE) * 32.0D)); // Update yPos for horse
            ReflectionUtils.setValue(packet, true, "l", dataWatcher);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Packet<?>> getPacketsFor(Player player) {
        List<Packet<?>> packets = new ArrayList<>();
        PacketPlayOutSpawnEntityLiving spawnPacket = getSpawnPacket(location);
        if (HologramPlugin.getInstance().onLegacyVersion(player)) {
            setAsLegacyPacket(spawnPacket); // Sets as horse

            packets.add(getSkullPacket(location));
            // TODO Add Skull Packet
            // TODO Add Attach Packet

        } else {
        }
        packets.add(spawnPacket);
        return packets;
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
