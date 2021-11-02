package gg.manny.hologram.line;

import gg.manny.hologram.entity.DummyEntityArmorStand;
import gg.manny.hologram.entity.DummyEntityWitherSkull;
import lombok.NonNull;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class HologramLine {

    public static final int ITEM_STACK = 2;
    protected static int ARMOR_STAND_ID = 30;
    protected static int WITHER_SKULL_PROJECTILE_ID = 66;

    protected static double OFFSET_HORSE = 55.0;
    protected static double OFFSET_OTHER = 1.2;

    @NonNull @Setter protected Location location;

    protected int armorStandId;
    protected int skullId;

    protected DataWatcher dataWatcher; // For armor stands

    public HologramLine(Location location) {
        this.location = location;

        // TODO Generate entity id without creating an Entity constructor
        WorldServer worldServer = ((CraftWorld) location.getWorld()).getHandle();
        DummyEntityArmorStand entity = new DummyEntityArmorStand(worldServer);
        armorStandId = entity.getId();
        dataWatcher = entity.getDataWatcher();

        // I should remove Dummy entities and use reflections to add to the entity id
        // instead of wasting garbage collection resources since I'm only sending holograms through
        // packets, unless I plan to have global entities
        DummyEntityWitherSkull witherSkull = new DummyEntityWitherSkull(worldServer);
        skullId = witherSkull.getId();
    }

    public abstract List<Packet<?>> getPacketsFor(Player player);

    public PacketPlayOutSpawnEntityLiving getSpawnPacket(Location location) {
        return new PacketPlayOutSpawnEntityLiving(armorStandId, (byte) HologramLine.ARMOR_STAND_ID,
                location.getX(), location.getY() - 0.13 + HologramLine.OFFSET_OTHER, location.getZ(),
                0, 0, 0,
                0, 0, 0,
                dataWatcher
        );
    }

    public PacketPlayOutSpawnEntity getSkullPacket(Location location, double offset) {
        PacketPlayOutSpawnEntity spawnPacket = new PacketPlayOutSpawnEntity(skullId,
                location.getX(),
                location.getY() - 0.13 + offset,
                location.getZ(),
                0, 0, 0,
                (int) location.getPitch(),
                (int) location.getYaw(),
                WITHER_SKULL_PROJECTILE_ID, 0
        );
        return spawnPacket;
    }
}