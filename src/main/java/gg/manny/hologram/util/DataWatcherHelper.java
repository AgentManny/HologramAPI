package gg.manny.hologram.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
// Credits: https://github.com/Howaner/FakeMobs/blob/master/src/main/java/de/howaner/FakeMobs/util/DataWatchCreator.java
public enum DataWatcherHelper {

    INVISIBILITY(0, 0),
    CUSTOM_NAME(10, 2),
    CUSTOM_NAME_VISIBLE(11, 3),

    ITEM(-1, 10),

    AGE(12, 12); // INTEGER (Byte won't work)

    private int id_1_7;
    private int id_1_8;

    public int getId() {
        return id_1_8;
    }

}
