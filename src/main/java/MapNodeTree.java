import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapRoomNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

// A simplified version of the base game's storage of the map
// Goal: easier to understand / work with than the original format
/*
public class MapNodeTree {


    /*
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

//    private static class QueueObject {
//        public @Nullable MapRoomNode node;
//        public Key id;
//        public int floor;
//
//        public QueueObject(MapRoomNode node, Key id, int floor) {
//            this.node = node;
//            this.id = id;
//            this.floor = floor;
//        }
//
//        public QueueObject(Key id, int floor) {
//            this.id = id;
//            this.floor = floor;
//        }
//    }

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


//    public static DAGManager buildDAG() {
//
//    }
}

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
