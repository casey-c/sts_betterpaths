import basemod.BaseMod;
import basemod.interfaces.PostInitializeSubscriber;
import basemod.interfaces.PostUpdateSubscriber;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.LegendItem;
import highlight.HighlightManager;
import legend.LegendHelper;
import utils.MiscUtils;
import utils.RightClickWatcher;

import java.util.ArrayList;

@SpireInitializer
public class BetterPaths implements PostInitializeSubscriber, PostUpdateSubscriber {
    private HighlightMenu menu;
    //private DAGManager dagManager;
    private HighlightManager manager;
    private LegendHelper legendHelper;

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
            if (manager == null) {
                //ArrayList<ArrayList<MapRoomNode>> mapNodes = CardCrawlGame.dungeon.getMap();

                manager = new HighlightManager();

                legendHelper = new LegendHelper(manager);
                legendHelper.setup();

//                int floorNum = 0;
//                for (ArrayList<MapRoomNode> ns : mapNodes) {
//                    for (MapRoomNode n : ns) {
//                        if (n.room == null)
//                            continue;
//
//                        highlight.NodeHighlight nb = new highlight.NodeHighlight(n.hb, new Color(1.0f, 0.0f, 0.0f, 0.5f));
//                        utils.RightClickWatcher.watchHB(nb.hb, this, onRightClick -> {
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

            }

            // TODO
//            if (AbstractDungeon.dungeonMapScreen.map.legend.isIconHovered("E"))
//                System.out.println("Elite legend highlighted?");
//            if (AbstractDungeon.dungeonMapScreen.map.legend.isIconHovered("$"))
//                System.out.println("Store legend highlighted?");
        }
    }
}
