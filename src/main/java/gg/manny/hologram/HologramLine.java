package gg.manny.hologram;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class HologramLine {

    private static int WITHER_SKULL_PROJECTILE_ID = 66;

    /** Returns the Entity Id for armor stands,
     * with legacy versions falling back to wither projectiles
     */
    private final int armorStandId; // We can manipulate packet to manually change to a Wither Skull

    /** Returns the Horse identifier for legacy versions */
    private final int horseId;


}
