package gg.manny.hologram.line;

import gg.manny.hologram.HologramPlugin;
import gg.manny.hologram.entity.DummyEntityArmorStand;
import gg.manny.hologram.entity.DummyEntityItem;
import gg.manny.hologram.entity.DummyEntityWitherSkull;
import gg.manny.hologram.util.EntityUtils;
import gg.manny.hologram.util.ReflectionUtils;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static gg.manny.hologram.util.DataWatcherHelper.*;

public class HologramItemLine extends HologramLine {

    private final ItemStack item;

    private final int itemId;
    private final DataWatcher itemDatawatcher;
    private final int armorStandId;
    private final int skullId;
    private final DataWatcher dataWatcher;

    public HologramItemLine(Location location, ItemStack itemStack) {
        super(location);
        this.item = itemStack;

        WorldServer worldServer = ((CraftWorld) location.getWorld()).getHandle();

        DummyEntityItem item = new DummyEntityItem(worldServer, itemStack);
        itemId = item.getId();
        itemDatawatcher = item.getDataWatcher();

        DummyEntityArmorStand entity = new DummyEntityArmorStand(worldServer);
        armorStandId = entity.getId();
        dataWatcher = entity.getDataWatcher();
        dataWatcher.watch(CUSTOM_NAME_VISIBLE.getId(), (byte) 0);

        // I should remove Dummy entities and use reflections to add to the entity id
        // instead of wasting garbage collection resources since I'm only sending holograms through
        // packets, unless I plan to have global entities
        DummyEntityWitherSkull witherSkull = new DummyEntityWitherSkull(worldServer);
        skullId = witherSkull.getId();
    }

    public PacketPlayOutSpawnEntityLiving getSpawnPacket(Location location) {
        return new PacketPlayOutSpawnEntityLiving(armorStandId, (byte) HologramLine.ARMOR_STAND_ID,
                location.getX(), location.getY() - 0.13 + HologramLine.OFFSET_OTHER, location.getZ(),
                0, 0, 0,
                0, 0, 0,
                dataWatcher
        );
    }

    public PacketPlayOutSpawnEntity getItemPacket(Location location) {
        PacketPlayOutSpawnEntity spawnPacket = new PacketPlayOutSpawnEntity(itemId,
                location.getX(),
                location.getY() - 0.13 + HologramLine.OFFSET_OTHER,
                location.getZ(),
                0, 0, 0,
                (int) location.getPitch(),
                (int) location.getYaw(),
                HologramLine.ITEM_STACK, 0
        );
        return spawnPacket;
    }

    public PacketPlayOutSpawnEntity getSkullPacket(Location location) {
        PacketPlayOutSpawnEntity spawnPacket = new PacketPlayOutSpawnEntity(skullId,
                location.getX(),
                location.getY() - 0.13,
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
        dataWatcher.a(CUSTOM_NAME_VISIBLE.getId(), (byte) 0);

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
        boolean legacy = HologramPlugin.getInstance().onLegacyVersion(player);
        packets.add(getItemPacket(location));
        packets.add(updateItemPacket(item));
        packets.add(legacy ? getSkullPacket(location) : getSpawnPacket(location));
        packets.add(new PacketPlayOutAttachEntity(itemId, legacy ? skullId : armorStandId, false));
        return packets;
    }

    // TODO Send destroy packet
    public void destroy() {

    }

    // TODO send update metadata packet
    public void update() {

    }

    public PacketPlayOutEntityMetadata updateItemPacket(ItemStack item) {
        itemDatawatcher.watch(ITEM.getId(), CraftItemStack.asNMSCopy(item));
        itemDatawatcher.update(ITEM.getId());
        return new PacketPlayOutEntityMetadata(itemId, itemDatawatcher, true);
    }

    // TODO send teleport packet
    public void teleport(Location location) {

    }

    public void setLocation(Location location) {
//        this.location = location;
        teleport(location);
    }
}
