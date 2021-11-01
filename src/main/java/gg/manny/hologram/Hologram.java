package gg.manny.hologram;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public interface Hologram {

    String getName();

    /**
     * Returns the identifier of a hologram
     * @return Unique identifier of the hologram
     */
    UUID getId();

    /**
     * Returns the location of a hologram
     * @return Location of hologram
     */
    Location getLocation();

    void setLocation(Location location);

    /**
     * Sends a hologram to viewers
     */
    void send();

    /**
     * Removes a hologram (sends destroy packet)
     */
    void remove();

    /**
     * Add lines to a hologram
     * @param lines Lines to add
     */
    void addLines(String... lines);

    /**
     * Set a specific line in a hologram
     * @param id Line Index
     * @param line Line to change
     */
    void setLine(int id, String line);

    void addItem(ItemStack item);

    void setItem(int id, ItemStack item);

    /**
     * @return All lines in a hologram
     */
    List<String> getLines();

    List<UUID> getViewers();

    void sendTo(Player player);

}
