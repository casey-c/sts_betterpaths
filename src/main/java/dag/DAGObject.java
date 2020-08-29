package dag;

import com.megacrit.cardcrawl.map.MapRoomNode;
import org.jetbrains.annotations.Nullable;
import utils.MiscUtils;

public class DAGObject {
    public @Nullable
    MapRoomNode node;
    public Key key;

    public DAGObject srcLeft, srcCenter, srcRight;
    public DAGObject targetLeft, targetCenter, targetRight;

    public DAGObject(MapRoomNode node, Key key) {
        this.node = node;
        this.key = key;
    }

    public DAGObject(Key key) {
        this.key = key;
    }

    public void printTargets() {
        System.out.println("DAGObject: " + key.x + ", " + key.y);
        if (targetLeft != null)
            System.out.println("\tLeft TARGET: " + targetLeft.key.x + ", " + targetLeft.key.y);
        if (targetCenter != null)
            System.out.println("\tCenter TARGET: " + targetCenter.key.x + ", " + targetCenter.key.y);
        if (targetRight != null)
            System.out.println("\tRight TARGET: " + targetRight.key.x + ", " + targetRight.key.y);
    }

    public void printSources() {
        System.out.println("DAGObject: " + key.x + ", " + key.y);
        if (srcLeft != null)
            System.out.println("\tLeft SOURCE: " + srcLeft.key.x + ", " + srcLeft.key.y);
        if (srcCenter != null)
            System.out.println("\tCenter SOURCE: " + srcCenter.key.x + ", " + srcCenter.key.y);
        if (srcRight != null)
            System.out.println("\tRight SOURCE: " + srcRight.key.x + ", " + srcRight.key.y);
    }

    public void setNode(MapRoomNode node) {
        this.node = node;
    }

    public void setSource(DAGObject src, Direction dir) {
        if (dir == Direction.LEFT)
            srcLeft = src;
        else if (dir == Direction.CENTER)
            srcCenter = src;
        else if (dir == Direction.RIGHT)
            srcRight = src;
    }

    public void setTarget(DAGObject target, Direction dir) {
        if (dir == Direction.LEFT)
            targetLeft = target;
        else if (dir == Direction.CENTER)
            targetCenter = target;
        else if (dir == Direction.RIGHT)
            targetRight = target;
    }
}
