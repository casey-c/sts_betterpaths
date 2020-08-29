import basemod.BaseMod;
import basemod.interfaces.PostInitializeSubscriber;
import basemod.interfaces.PostUpdateSubscriber;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.LegendItem;
import com.megacrit.cardcrawl.map.MapRoomNode;
import dag.DAGManager;
import utils.PreTopBarRenderHelper;

import java.util.ArrayList;

@SpireInitializer
public class BetterPaths implements PostInitializeSubscriber, PostUpdateSubscriber {
    private ArrayList<NodeHighlight> nodes = new ArrayList<>();
    //private boolean initNodes = false;
    private HighlightMenu menu;
    private DAGManager dagManager;

    public BetterPaths() {
        BaseMod.subscribe(this);
    }

    public static void initialize() {
        new BetterPaths();
    }

    @Override
    public void receivePostInitialize() {
        System.out.println("OJB: better paths init");
    }

    @Override
    public void receivePostUpdate() {
        if (!CardCrawlGame.isInARun())
            return;

        if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.MAP) {
            if (menu == null)
                menu = new HighlightMenu();

            // One time setup to get all the nodes of row 1
            //if (!initNodes) {
                //initNodes = true;
            if (dagManager == null) {
                //ArrayList<ArrayList<MapRoomNode>> mapNodes = CardCrawlGame.dungeon.getMap();

                dagManager = DAGManager.build();

//                int floorNum = 0;
//                for (ArrayList<MapRoomNode> ns : mapNodes) {
//                    for (MapRoomNode n : ns) {
//                        if (n.room == null)
//                            continue;
//
//                        NodeHighlight nb = new NodeHighlight(n.hb, new Color(1.0f, 0.0f, 0.0f, 0.5f));
//                        RightClickWatcher.watchHB(nb.hb, this, onRightClick -> {
//                            nb.toggle();
//                        });
//
//                        PreTopBarRenderHelper.addRenderable(nb);
//                        nodes.add(nb);
//                    }
//
//                    ++floorNum;
//                }
//
//                System.out.println("OJB: finished init of " + nodes.size() + " nodes");

                // TODO
                //   Legend
                // event (?), merchant ($), treasure (T), rest (R), monster (M), elite (E)
//                ArrayList<LegendItem> legendItems = AbstractDungeon.dungeonMapScreen.map.legend.items;
//                int itemID = 0;
//                for (LegendItem item : legendItems) {
//                    int finalItemID = itemID++;
//                    RightClickWatcher.watchHB(item.hb, this, onRightClick -> {
//                        System.out.println("legend right clicked: " + finalItemID);
//                    });
//                }
            }

            // TODO
//            if (AbstractDungeon.dungeonMapScreen.map.legend.isIconHovered("E"))
//                System.out.println("Elite legend highlighted?");
//            if (AbstractDungeon.dungeonMapScreen.map.legend.isIconHovered("$"))
//                System.out.println("Store legend highlighted?");
        }
    }
}
