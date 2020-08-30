package highlight;

import com.badlogic.gdx.graphics.Color;
import dag.DAGManager;
import dag.DAGObject;
import legend.LegendHelper;
import org.jetbrains.annotations.Nullable;
import utils.MiscUtils;
import utils.MyColors;
import utils.SoundHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

public class HighlightManager {

    private static class MapStorageObject {
        private int id;
        private HighlightObject highlightObject;
        private DAGObject dagObject;

        public MapStorageObject(HighlightObject h, DAGObject d) {
            this.id = h.getID();
            this.highlightObject = h;
            this.dagObject = d;
        }
    }

    //private ArrayList<HighlightObject> highlighted;
    private DAGManager dag;
    private LegendHelper legendHelper;
    private HashMap<Integer, MapStorageObject> map = new HashMap<>();

    private Color currentColor = MyColors.LIGHT_BLUE;
    private static boolean hideUnreachable = false; // TODO config?

    public HighlightManager() {
        legendHelper = new LegendHelper(this);
        legendHelper.setup();
    }

    public void build() {
        dag = DAGManager.build();
        setupHighlightObjects();
        legendHelper.reset();
    }

    public void setHideUnreachable(boolean val) {
        this.hideUnreachable = val;
    }

    public static boolean getHideUnreachable() {
        return hideUnreachable;
    }

    private void setupHighlightObjects() {
        for (DAGObject dagObject : dag.getAll()) {
            HighlightObject highlightObject = new HighlightObject(this, dagObject.floor, dagObject.node, currentColor);
            highlightObject.makeRightClickable();

            // Store for later
            MapStorageObject storageObject = new MapStorageObject(highlightObject, dagObject);
            dagObject.storageID = storageObject.id;

            map.put(storageObject.id, storageObject);
        }
    }

    private LinkedList<HighlightObject> getHighlightObjects() {
        Collection<MapStorageObject> sto = map.values();

        LinkedList<HighlightObject> res = new LinkedList<>();
        for (MapStorageObject o : sto)
            res.add(o.highlightObject);

        return res;
    }

    public void setAllOfType(MiscUtils.ROOM_TYPE type, boolean val, boolean forceX) {
        for (HighlightObject obj : getHighlightObjects())
            if (obj.isType(type))
                obj.setVisibleWithColor(val, currentColor, forceX);
    }

    public void setColor(Color color) {
        currentColor = color;
    }

    public Color getColor() {
        return currentColor;
    }

    private @Nullable DAGObject getDAGObjectFromHighlightID(int id) {
        if (map.containsKey(id))
            return map.get(id).dagObject;
        else
            return null;
    }

    public void setAllForcedFrom(int highlightID, boolean wasSet, boolean forceX) {
        DAGObject seed = getDAGObjectFromHighlightID(highlightID);
        if (seed == null)
            return;

        ArrayList<DAGObject> list = dag.getForcedFrom(seed);

        for (DAGObject d : list) {
            int id = d.storageID;
            if (map.containsKey(id)) {
                MapStorageObject storageObject = map.get(id);
                storageObject.highlightObject.setVisibleWithColor(!wasSet, currentColor, forceX);
            }
        }

        SoundHelper.playDeck(!wasSet);
    }

    public void clearAll() {
        for (HighlightObject o : getHighlightObjects()) {
            o.setVisibleWithColor(false, currentColor, false);
        }
        legendHelper.reset();
    }

    public void reset() {
        clearAll();
        map.clear();
    }
}
