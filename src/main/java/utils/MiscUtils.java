package utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class MiscUtils {
    // event (?), merchant ($), treasure (T), rest (R), monster (M), elite (E)
    public enum ROOM_TYPE {
        EVENT("?", 0),
        MERCHANT("$", 1),
        TREASURE("T", 2),
        REST("R", 3),
        MONSTER("M", 4),
        ELITE("E", 5),
        NULL("*", 6);

        private String id;
        private int val;

        ROOM_TYPE(String id, int val) {
            this.id = id;
            this.val = val;
        }

        public static ROOM_TYPE fromString(String id) {
            if (id == "?") return ROOM_TYPE.EVENT;
            else if (id == "$") return ROOM_TYPE.MERCHANT;
            else if (id == "T") return ROOM_TYPE.TREASURE;
            else if (id == "R") return ROOM_TYPE.REST;
            else if (id == "M") return ROOM_TYPE.MONSTER;
            else if (id == "E") return ROOM_TYPE.ELITE;
            else return ROOM_TYPE.NULL;
        }

        public String toString() {
            return id;
        }

        public int toInt() {
            return val;
        }
    }

    public static boolean isShiftPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT);
    }

    public static boolean isControlPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT);
    }

    public static boolean isAltPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.ALT_RIGHT); // yuck
    }

}
