package legend;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.LegendItem;
import highlight.HighlightManager;
import utils.MiscUtils;
import utils.RightClickWatcher;
import utils.SoundHelper;

import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class LegendHelper {
    // ?, $, T, R, M, E
    private boolean eventsOn, shopsOn, treasuresOn, restsOn, monstersOn, elitesOn;
    private HighlightManager manager;

    public LegendHelper(HighlightManager manager) {
        this.manager = manager;
    }

    public void setup() {
        // ?, $, T, R, M, E
        ArrayList<LegendItem> legendItems = AbstractDungeon.dungeonMapScreen.map.legend.items;
        if (legendItems.size() != 6) {
            System.out.println("OJB: warning -- invalid legend size (not enabling)");
            return;
        }

        RightClickWatcher.watchHB(legendItems.get(0).hb, this, onRightClick -> {
            eventsOn = !eventsOn;
            manager.setAllOfType(MiscUtils.ROOM_TYPE.EVENT, eventsOn);
            SoundHelper.playDeck(eventsOn);
        });
        RightClickWatcher.watchHB(legendItems.get(1).hb, this, onRightClick -> {
            shopsOn = !shopsOn;
            manager.setAllOfType(MiscUtils.ROOM_TYPE.MERCHANT, shopsOn);
            SoundHelper.playDeck(shopsOn);
        });
        RightClickWatcher.watchHB(legendItems.get(2).hb, this, onRightClick -> {
            treasuresOn = !treasuresOn;
            manager.setAllOfType(MiscUtils.ROOM_TYPE.TREASURE, treasuresOn);
            SoundHelper.playDeck(treasuresOn);
        });

        RightClickWatcher.watchHB(legendItems.get(3).hb, this, onRightClick -> {
            restsOn = !restsOn;
            manager.setAllOfType(MiscUtils.ROOM_TYPE.REST, restsOn);
            SoundHelper.playDeck(restsOn);
        });
        RightClickWatcher.watchHB(legendItems.get(4).hb, this, onRightClick -> {
            monstersOn = !monstersOn;
            manager.setAllOfType(MiscUtils.ROOM_TYPE.MONSTER, monstersOn);
            SoundHelper.playDeck(monstersOn);
        });
        RightClickWatcher.watchHB(legendItems.get(5).hb, this, onRightClick -> {
            elitesOn = !elitesOn;
            manager.setAllOfType(MiscUtils.ROOM_TYPE.ELITE, elitesOn);
            SoundHelper.playDeck(elitesOn);
        });

//        RightClickWatcher.watchHB(legendItems.get(0).hb, this, onRightClick -> { manager.highlightAllOfType(MiscUtils.ROOM_TYPE.EVENT); });
//        RightClickWatcher.watchHB(legendItems.get(1).hb, this, onRightClick -> { manager.highlightAllOfType(MiscUtils.ROOM_TYPE.MERCHANT); });
//        RightClickWatcher.watchHB(legendItems.get(2).hb, this, onRightClick -> { manager.highlightAllOfType(MiscUtils.ROOM_TYPE.TREASURE); });
//        RightClickWatcher.watchHB(legendItems.get(3).hb, this, onRightClick -> { manager.highlightAllOfType(MiscUtils.ROOM_TYPE.REST); });
//        RightClickWatcher.watchHB(legendItems.get(4).hb, this, onRightClick -> { manager.highlightAllOfType(MiscUtils.ROOM_TYPE.MONSTER); });
//        RightClickWatcher.watchHB(legendItems.get(5).hb, this, onRightClick -> { manager.highlightAllOfType(MiscUtils.ROOM_TYPE.ELITE); });

//        ArrayList<LegendItem> legendItems = AbstractDungeon.dungeonMapScreen.map.legend.items;
//        System.out.println("OJB: legend size is: " + legendItems.size());
//        int itemID = 0;
//        for (LegendItem item : legendItems) {
//            int finalItemID = itemID++;
//            RightClickWatcher.watchHB(item.hb, this, onRightClick -> {
//                System.out.println("legend right clicked: " + finalItemID);
//                // TODO
//            });
//        }
    }
}
