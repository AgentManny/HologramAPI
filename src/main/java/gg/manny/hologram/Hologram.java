package gg.manny.hologram;

import javax.xml.stream.Location;
import java.util.List;
import java.util.UUID;

public interface Hologram {

    /**
     * Returns the identifier of a hologram
     * @return Unique identifier of the hologram
     */
    UUID id();

    /**
     * Returns the location of a hologram
     * @return Location of hologram
     */
    Location getLocation();

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

    /**
     * @return All lines in a hologram
     */
    List<String> getLines();

}
