package gg.manny.hologram.line;

import gg.manny.hologram.HologramPlugin;
import gg.manny.hologram.entity.DummyEntityItem;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static gg.manny.hologram.util.DataWatcherHelper.ITEM;

public class HologramItemLine extends HologramLine {

    private final ItemStack item;

    private final int itemId;
    private final DataWatcher itemData;

    public HologramItemLine(Location location, ItemStack itemStack) {
        super(location);
        this.item = itemStack;

        WorldServer worldServer = ((CraftWorld) location.getWorld()).getHandle();

        DummyEntityItem item = new DummyEntityItem(worldServer, itemStack);
        itemId = item.getId();
        itemData = item.getDataWatcher();
    }

    @Override
    public List<Packet<?>> getPacketsFor(Player player) {
        List<Packet<?>> packets = new ArrayList<>();
        boolean legacy = HologramPlugin.getInstance().onLegacyVersion(player);
        packets.add(getItemPacket(location));
        packets.add(getUpdateItemPacket(item));
        packets.add(legacy ? getSkullPacket(location, 0) : getSpawnPacket(location));
        packets.add(new PacketPlayOutAttachEntity(itemId, legacy ? skullId : armorStandId, false));
        return packets;
    }

    private PacketPlayOutSpawnEntity getItemPacket(Location location) {
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

    private PacketPlayOutEntityMetadata getUpdateItemPacket(ItemStack item) {
        itemData.watch(ITEM.getId(), CraftItemStack.asNMSCopy(item));
        itemData.update(ITEM.getId());
        return new PacketPlayOutEntityMetadata(itemId, itemData, true);
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
