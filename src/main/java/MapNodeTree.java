import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapRoomNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

// A simplified version of the base game's storage of the map
// Goal: easier to understand / work with than the original format
public class MapNodeTree {

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

        static ROOM_TYPE fromString(String id) {
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

    // Connections are null if they don't exist
    private static class MapNodeTreeNode {
        private @NotNull
        MapRoomNode node;
        private int id;
        private int floor;


        // Connections
        private @Nullable
        MapNodeTreeNode downLeft, downCenter, downRight;
        private @Nullable
        MapNodeTreeNode upLeft, upCenter, upRight;

        public MapNodeTreeNode(MapRoomNode node, int id, int floor) {
            this.node = node;
            this.id = id;
            this.floor = floor;
        }

        public boolean onlyOneWayUp() {
            int l = (upLeft != null) ? 1 : 0;
            int c = (upCenter != null) ? 1 : 0;
            int r = (upRight != null) ? 1 : 0;
            return ((l + c + r) == 1);
        }

        public MapNodeTreeNode getOnlyWayUp() {
            assert onlyOneWayUp();
            if (upLeft != null) return upLeft;
            else if (upCenter != null) return upCenter;
            else return upRight;
        }

        public boolean onlyOneWayDown() {
            int l = (downLeft != null) ? 1 : 0;
            int c = (downCenter != null) ? 1 : 0;
            int r = (downRight != null) ? 1 : 0;
            return ((l + c + r) == 1);
        }

        public MapNodeTreeNode getOnlyWayDown() {
            assert onlyOneWayDown();
            if (downLeft != null) return downLeft;
            else if (downCenter != null) return downCenter;
            else return downRight;
        }
    }

    private static class QueueObject {
        public @Nullable MapRoomNode node;
        public Key id;
        public int floor;

        public QueueObject(MapRoomNode node, Key id, int floor) {
            this.node = node;
            this.id = id;
            this.floor = floor;
        }

        public QueueObject(Key id, int floor) {
            this.id = id;
            this.floor = floor;
        }
    }

//    private static ArrayList<ArrayList<QueueObject>> initialLabeling(ArrayList<ArrayList<MapRoomNode>> unlabeled) {
//        ArrayList<ArrayList<QueueObject>> labeledMapNodes = new ArrayList<>();
//        int id = 0;
//        int floor = 0;
//
//        for (ArrayList<MapRoomNode> ns : unlabeled) {
//            ArrayList<QueueObject> labeledNs = new ArrayList<>();
//            for (MapRoomNode n : ns) {
//                // TODO: verify this doesn't miss the top rest row before boss
//                if (n.rightNodeAvailable() || n.leftNodeAvailable() || n.centerNodeAvailable())
//                    labeledNs.add(new QueueObject(n, id++, floor));
//            }
//
//            labeledMapNodes.add(labeledNs);
//            ++floor;
//        }
//        return labeledMapNodes;
//    }
//
//    private static HashMap<MapRoomNode, QueueObject> toMap(ArrayList<ArrayList<QueueObject>> labeled) {
//        HashMap<MapRoomNode, QueueObject> map = new HashMap<>();
//
//        for (ArrayList<QueueObject> ls : labeled) {
//            for (QueueObject o : ls) {
//                map.put(o.node, o);
//            }
//        }
//
//        return map;
//    }

    private enum DIRECTION {
        LEFT,
        CENTER,
        RIGHT,
        NONE
    }

    private static class DAGObject {
        public @Nullable MapRoomNode node;
        public Key id;

        public DAGObject srcLeft, srcCenter, srcRight;
        public DAGObject targetLeft, targetCenter, targetRight;

        public DAGObject(MapRoomNode node, Key id) {
            this.node = node;
            this.id = id;
        }

        public DAGObject(Key id) {
            this.id = id;
        }

        public void printTargets() {
            System.out.println("DAGObject: " + id.x + ", " + id.y);
            if (targetLeft != null)
                System.out.println("\tLeft TARGET: " + targetLeft.id.x + ", " + targetLeft.id.y);
            if (targetCenter != null)
                System.out.println("\tCenter TARGET: " + targetCenter.id.x + ", " + targetCenter.id.y);
            if (targetRight != null)
                System.out.println("\tRight TARGET: " + targetRight.id.x + ", " + targetRight.id.y);
        }

        public void printSources() {
            System.out.println("DAGObject: " + id.x + ", " + id.y);
            if (srcLeft != null)
                System.out.println("\tLeft SOURCE: " + srcLeft.id.x + ", " + srcLeft.id.y);
            if (srcCenter != null)
                System.out.println("\tCenter SOURCE: " + srcCenter.id.x + ", " + srcCenter.id.y);
            if (srcRight != null)
                System.out.println("\tRight SOURCE: " + srcRight.id.x + ", " + srcRight.id.y);
        }

        public void setNode(MapRoomNode node) {
            this.node = node;
        }

        public void setSource(DAGObject src, DIRECTION dir) {
            if (dir == DIRECTION.LEFT)
                srcLeft = src;
            else if (dir == DIRECTION.CENTER)
                srcCenter = src;
            else if (dir == DIRECTION.RIGHT)
                srcRight = src;
        }

        public void setTarget(DAGObject target, DIRECTION dir) {
            if (dir == DIRECTION.LEFT)
                targetLeft = target;
            else if (dir == DIRECTION.CENTER)
                targetCenter = target;
            else if (dir == DIRECTION.RIGHT)
                targetRight = target;
        }
    }

    private static class DAGManager {
        private HashMap<Key, DAGObject> all = new HashMap<>();
        private HashMap<Key, DAGObject> elites = new HashMap<>();
        private HashMap<Key, DAGObject> events = new HashMap<>();
        private HashMap<Key, DAGObject> shops = new HashMap<>();
        private HashMap<Key, DAGObject> monsters = new HashMap<>();
        private HashMap<Key, DAGObject> treasures = new HashMap<>();
        private HashMap<Key, DAGObject> rests = new HashMap<>();

        private HashSet<Key> seeds;

        public void build(HashMap<Key, DAGObject> input, HashSet<Key> seeds) {
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
                System.out.println("OJB: found a key with null node: " + obj.id.x + ", " + obj.id.y);
                return;
            }

            all.put(obj.id, obj);

            if (obj.node.getRoomSymbol(true) == "?")
                events.put(obj.id, obj);
            else if (obj.node.getRoomSymbol(true) == "E")
                elites.put(obj.id, obj);
            else if (obj.node.getRoomSymbol(true) == "$")
                shops.put(obj.id, obj);
            else if (obj.node.getRoomSymbol(true) == "M")
                monsters.put(obj.id, obj);
            else if (obj.node.getRoomSymbol(true) == "T")
                treasures.put(obj.id, obj);
            else if (obj.node.getRoomSymbol(true) == "R")
                rests.put(obj.id, obj);
        }
    }

    public static class Key {
        private final int x;
        private final int y;

        public Key(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Key)) return false;
            Key key = (Key) o;
            return x == key.x && y == key.y;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }
    }

    private static DIRECTION getDirection(int x1, int x2) {
        if (x1 < x2)
            return DIRECTION.RIGHT;
        else if (x1 == x2)
            return DIRECTION.CENTER;
        else
            return DIRECTION.LEFT;
    }

    public static void buildDAG() {
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

                    obj.setTarget(dst, getDirection(e.srcX, e.dstX));
                    dst.setSource(obj, getDirection(e.dstX, e.srcX));
                }
            }

            ++floor;
        }

        DAGManager manager = new DAGManager();
        manager.build(dag, seeds);
        manager.print();

        System.out.println("Printing treasures only: ");
        HashMap<Key, DAGObject> treasures = manager.treasures;
        for (DAGObject t : treasures.values()) {
            t.printTargets();
            t.printSources();
        }
    }
}

    /*
    public static void buildTree() {
        ArrayList<ArrayList<MapRoomNode>> mapNodes = CardCrawlGame.dungeon.getMap();

        // Perform the initial labeling
        ArrayList<ArrayList<QueueObject>> labeled = initialLabeling(mapNodes);
        HashMap<MapRoomNode, QueueObject> labeledMap = toMap(labeled);

        // DFS to construct the DAG objects
        HashSet<Integer> seenIds = new HashSet<>();

        // All parallel queues
        LinkedList<QueueObject> queue = new LinkedList<>();
        LinkedList<Integer> targetID = new LinkedList<>();
        LinkedList<DIRECTION> targetDIR = new LinkedList<>();

        // Init the DFS with the top row of rooms
        ArrayList<QueueObject> topRow = labeled.get(labeled.size() - 1);
        for (QueueObject r : topRow) {
            queue.add(r);
            targetID.add(r.id);
            targetDIR.add(DIRECTION.NONE);
        }

        DAGManager manager = new DAGManager();

        // Perform the DFS
        while (!queue.isEmpty()) {
            QueueObject curr = queue.getFirst();
            Integer currTargID = targetID.getFirst();
            DIRECTION currTargDIR = targetDIR.getFirst();

            queue.pop();
            targetID.pop();
            targetDIR.pop();

            // Add current node to visited set
            seenIds.add(curr.id);

            // Make the DAG object
            DAGObject currObj = new DAGObject(curr.node, curr.id);
            manager.insert(currObj);

            // Update the source object to point here if not already
            DAGObject target = manager.getFromID(currTargID);
            currObj.setTarget(target, currTargDIR);

            // Recurse on all unvisited targets of this node
            MapRoomNode node = curr.node;
            ArrayList<MapRoomNode> parents = node.getParents();
            for (MapRoomNode p : parents) {
                if (labeledMap.containsKey(p)) {
                    QueueObject parent = labeledMap.get(p);

                    if (!seenIds.contains(parent.id)) {
                        queue.addFirst(parent);
                        targetID.addFirst(curr.id);
                    }

                }
            }

            // TODO: reeeeeeeeee who decided to put a DAG into an ArrayList of ArrayLists!?!?
        }



        //todo: im too tired tonight to think through this appropriately
        //   goal: convert the mapNodes nested arraylist into a nicer DAG format with quick access to l/c/r child/parents

//        ArrayList<MapRoomNode> first = mapNodes.get(0);
//        ArrayDeque<QueueObject> queue = new ArrayDeque<>();
//        int id = 0;
//        for (MapRoomNode n : first) {
//            queue.addLast(new QueueObject(n, id++, 0));
//        }
//
//        while (!queue.isEmpty()) {
//            QueueObject curr = queue.getFirst();
//            queue.pop();
//
//            MapRoomNode n = curr.node;
//            if (n.rightNodeAvailable()) {
//
//            }
//        }

//        int id = 0;
//        int floor = 0;
//        for (ArrayList<MapRoomNode> ns : mapNodes) {
//            for (MapRoomNode n : ns) {
//                MapNodeTreeNode tn = new MapNodeTreeNode(n, id, floor);
//                ++id;
//            }
//            ++floor;
//        }

//        System.out.println("OJB: mapNodes.size() = " + mapNodes.size());
//        for (ArrayList<MapRoomNode> ns : mapNodes) {
//            System.out.print("\t");
//            for (MapRoomNode n : ns) {
//                String s = n.getRoomSymbol(true);
//                if (n.room != null) {
//                    ArrayList<MapRoomNode> parents = n.getParents();
//
//                    boolean l = n.leftNodeAvailable();
//                    boolean c = n.centerNodeAvailable();
//                    boolean r = n.rightNodeAvailable();
//
//                    if (l || c || r) {
//                        System.out.print(s + " (" + parents.size() +" parents) -- ");
//                        System.out.print("l: " + n.leftNodeAvailable() + " ");
//                        System.out.print("c: " + n.centerNodeAvailable() + " ");
//                        System.out.print("r: " + n.rightNodeAvailable() + " ");
//                        System.out.print(" | ");
//                    }
//
//                }
//            }
//            System.out.println();
//        }
    }


}

     */
