package gg.manny.hologram.line;

import lombok.RequiredArgsConstructor;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

@RequiredArgsConstructor
public abstract class HologramLine {

    protected static int ARMOR_STAND_ID = 30;
    protected static int WITHER_SKULL_PROJECTILE_ID = 66;

    protected static double OFFSET_HORSE = 58.25;
    protected static double OFFSET_OTHER = 1.2;

    protected final Location location;

    /** Returns the Entity Id for armor stands,
     * with legacy versions falling back to wither projectiles
     */
    private int skullId;

    /** Returns the Horse identifier for legacy versions */
    private int horseId; // We can manipulate packet to manually change to a Armor Stand (Living Entity)

    public abstract List<Packet<?>> getPacketsFor(Player player);

}
