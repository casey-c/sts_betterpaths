package dag;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapRoomNode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class DAGManager {
    private HashMap<Key, DAGObject> all = new HashMap<>();
    private HashMap<Key, DAGObject> elites = new HashMap<>();
    private HashMap<Key, DAGObject> events = new HashMap<>();
    private HashMap<Key, DAGObject> shops = new HashMap<>();
    private HashMap<Key, DAGObject> monsters = new HashMap<>();
    private HashMap<Key, DAGObject> treasures = new HashMap<>();
    private HashMap<Key, DAGObject> rests = new HashMap<>();

    private HashSet<Key> seeds;

    // private constructor -- use .build()!
    private DAGManager() { }

    private void set(HashMap<Key, DAGObject> input, HashSet<Key> seeds) {
        this.seeds = seeds;

        for (Key k : input.keySet()) {
            insert(input.get(k));
        }
    }

    public void print() {
        System.out.println("DAGManager tracks: " + all.size() + " total nodes");
        System.out.println("\t with elites: " + elites.size());
        System.out.println("\t with events: " + events.size());
        System.out.println("\t with shops: " + shops.size());
        System.out.println("\t with monsters: " + monsters.size());
        System.out.println("\t with treasures: " + treasures.size());
        System.out.println("\t with rests: " + rests.size());
    }

    private void insert(DAGObject obj) {
        if (obj.node == null) {
            System.out.println("OJB: found a key with null node: " + obj.key.x + ", " + obj.key.y);
            return;
        }

        all.put(obj.key, obj);

        if (obj.node.getRoomSymbol(true) == "?")
            events.put(obj.key, obj);
        else if (obj.node.getRoomSymbol(true) == "E")
            elites.put(obj.key, obj);
        else if (obj.node.getRoomSymbol(true) == "$")
            shops.put(obj.key, obj);
        else if (obj.node.getRoomSymbol(true) == "M")
            monsters.put(obj.key, obj);
        else if (obj.node.getRoomSymbol(true) == "T")
            treasures.put(obj.key, obj);
        else if (obj.node.getRoomSymbol(true) == "R")
            rests.put(obj.key, obj);
    }

    // Factory
    public static DAGManager build() {
        ArrayList<ArrayList<MapRoomNode>> mapNodes = CardCrawlGame.dungeon.getMap();
        HashMap<Key, DAGObject> dag = new HashMap<>();
        HashSet<Key> seeds = new HashSet<>();

        int floor = 0;
        for (ArrayList<MapRoomNode> ns : mapNodes) {
            for (MapRoomNode n : ns) {
                ArrayList<MapEdge> edges = n.getEdges();

                if (edges.size() == 0)
                    continue;

                for (MapEdge e : edges) {
                    Key k = new Key(e.srcX, e.srcY);
                    DAGObject obj = dag.containsKey(k) ? dag.get(k) : new DAGObject(n, k);
                    dag.put(k, obj);
                    obj.setNode(n);

                    if (floor == 0)
                        seeds.add(k);

                    Key kDst = new Key(e.dstX, e.dstY);
                    DAGObject dst = dag.containsKey(kDst) ? dag.get(kDst) : new DAGObject(kDst);
                    dag.put(kDst, dst);

                    obj.setTarget(dst, Direction.getDirection(e.srcX, e.dstX));
                    dst.setSource(obj, Direction.getDirection(e.dstX, e.srcX));
                }
            }

            ++floor;
        }

        DAGManager manager = new DAGManager();
        manager.set(dag, seeds);
        manager.print();

        // Debug
        System.out.println("Printing treasures only: ");
        HashMap<Key, DAGObject> treasures = manager.treasures;
        for (DAGObject t : treasures.values()) {
            t.printTargets();
            t.printSources();
        }

        return manager;
    }

    public Collection<DAGObject> getAll() {
        return all.values();
    }

    public ArrayList<DAGObject> getForcedFrom(DAGObject obj) {
        ArrayList<DAGObject> list = new ArrayList<>();

        // TODO
        // SOURCES (DOWN)
        DAGObject next = obj;
        do {
            next = next.getOnlySourceOrNull();
            if (next != null)
                list.add(next);
        } while (next != null);

        // TARGETS (UP)
        next = obj;
        do {
            next = next.getOnlyTargetOrNull();
            if (next != null)
                list.add(next);
        } while (next != null);

        // Add the original center object
        list.add(obj);
        return list;
    }
}
