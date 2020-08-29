package highlight;

import com.badlogic.gdx.graphics.Color;
import dag.DAGManager;
import dag.DAGObject;
import utils.MiscUtils;
import utils.MyColors;

import java.util.HashMap;

public class HighlightManager {
    //private ArrayList<HighlightObject> highlighted;
    private DAGManager dag;
    private HashMap<Integer, HighlightObject> map = new HashMap<>();

    private Color currentColor = MyColors.BLUE_COLOR;

    public HighlightManager() {
        dag = DAGManager.build();
        setupHighlightObjects();
    }

    private void setupHighlightObjects() {
        for (DAGObject dagObject : dag.getAll()) {
            HighlightObject highlightObject = new HighlightObject(dagObject.node, currentColor);
            map.put(highlightObject.getID(), highlightObject);

            highlightObject.makeRightClickable();
        }
    }

    public void setAllOfType(MiscUtils.ROOM_TYPE type, boolean val) {
        for (HighlightObject obj : map.values()) {
            if (obj.isType(type))
                obj.setVisible(val);
        }
    }
}
