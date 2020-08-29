package dag;

public enum Direction {
    LEFT,
    CENTER,
    RIGHT;

    public static Direction getDirection(int x1, int x2) {
        if (x1 < x2)
            return Direction.RIGHT;
        else if (x1 == x2)
            return Direction.CENTER;
        else
            return Direction.LEFT;
    }
}

