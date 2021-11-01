package gg.manny.hologram;

import lombok.RequiredArgsConstructor;
import net.minecraft.server.v1_8_R3.DataWatcher;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntity;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import static gg.manny.hologram.util.DataWatcherHelper.AGE;
import static gg.manny.hologram.util.DataWatcherHelper.INVISIBILITY;

@RequiredArgsConstructor
public abstract class HologramLine {

    private static int ARMOR_STAND_ID = 30;
    private static int WITHER_SKULL_PROJECTILE_ID = 66;

    private static double HORSE_OFFSET = 0.55;

    /** Returns the Entity Id for armor stands,
     * with legacy versions falling back to wither projectiles
     */
    private final int skullId;

    /** Returns the Horse identifier for legacy versions */
    private final int horseId; // We can manipulate packet to manually change to a Armor Stand (Living Entity)

    // TODO eventually use reflections
    public PacketPlayOutSpawnEntity getSkullPacket(Location location) {
        PacketPlayOutSpawnEntity spawnPacket = new PacketPlayOutSpawnEntity(skullId,
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

    public PacketPlayOutSpawnEntityLiving getHorsePacket(EntityLiving entity) {
        DataWatcher watcher = entity.getDataWatcher();
        watcher.a(INVISIBILITY.getId(), (byte) 0);
        watcher.a(1, (short) 300); // Not sure

        // TODO set Custom Name visibility

        watcher.a(AGE.getId(), -1700000);
        PacketPlayOutSpawnEntityLiving entityPacket = new PacketPlayOutSpawnEntityLiving(entity.getId(),
                (byte) EntityType.HORSE.getTypeId(),
                entity.locX,
                entity.locY + HORSE_OFFSET, // Todo this will have to change if we're setting as armor stand
                entity.locZ,
                (int) entity.pitch,
                (int) entity.yaw,
                entity.getHeadRotation(),
                0, 0, 0,
                watcher
        );
        return entityPacket;
    }

}
