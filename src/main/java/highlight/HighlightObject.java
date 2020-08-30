package highlight;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.map.MapRoomNode;
import dag.DAGObject;
import utils.MiscUtils;
import utils.PreTopBarRenderHelper;
import utils.RightClickWatcher;

public class HighlightObject {
    private static int globalID = 0;

    private int id;
    private MapRoomNode node;
    private MiscUtils.ROOM_TYPE type;

    private HighlightManager parent;
    private NodeHighlight highlight;

    public HighlightObject(HighlightManager parent, MapRoomNode node, Color color) {
        this.parent = parent;
        this.id = globalID++;
        this.node = node;

        this.type = MiscUtils.ROOM_TYPE.fromString(node.getRoomSymbol(true));

        this.highlight = new NodeHighlight(node.hb, color);
    }

    public void makeRightClickable() {
        RightClickWatcher.watchHB(node.hb, this, onRightClick -> {
            // TODO:
            boolean forceX = MiscUtils.isAltPressed();

            if (MiscUtils.isShiftPressed()) {
                parent.setAllForcedFrom(id, highlight.isVisible(), forceX);
            }
            else {
                highlight.toggleWithColor(parent.getColor(), forceX);
            }
        });
        PreTopBarRenderHelper.addRenderable(highlight);
    }

    public int getID() {
        return id;
    }

    public boolean isType(MiscUtils.ROOM_TYPE type) {
        return this.type == type;
    }

    public void setVisibleWithColor(boolean val, Color color, boolean forceX) {
        highlight.setVisible(val);
        highlight.setColor(color);
        highlight.setShowX(forceX);
    }
}
