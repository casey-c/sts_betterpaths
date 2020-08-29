package highlight;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.map.MapRoomNode;
import utils.MiscUtils;
import utils.PreTopBarRenderHelper;
import utils.RightClickWatcher;

public class HighlightObject {
    private static int globalID = 0;

    private int id;
    private MapRoomNode node;
    private MiscUtils.ROOM_TYPE type;

    private NodeHighlight highlight;

    public HighlightObject(MapRoomNode node, Color color) {
        this.id = globalID++;
        this.node = node;
        this.type = MiscUtils.ROOM_TYPE.fromString(node.getRoomSymbol(true));

        this.highlight = new NodeHighlight(node.hb, color);
    }

    public void makeRightClickable() {
        RightClickWatcher.watchHB(node.hb, this, onRightClick -> {
            highlight.toggle();
            System.out.println("OJB: highlight toggled Node: " + id);
        });
        PreTopBarRenderHelper.addRenderable(highlight);
    }

    public int getID() {
        return id;
    }

    public boolean isType(MiscUtils.ROOM_TYPE type) {
        return this.type == type;
    }

    public void setVisible(boolean val) {
        highlight.setVisible(val);
    }
}
