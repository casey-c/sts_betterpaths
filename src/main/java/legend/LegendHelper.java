package legend;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.LegendItem;
import highlight.HighlightManager;
import utils.MiscUtils;
import utils.RightClickWatcher;
import utils.SoundHelper;

import java.util.ArrayList;

public class LegendHelper {
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

        RightClickWatcher.watchHbPermanent(legendItems.get(0).hb, this, onRightClick -> {
            eventsOn = !eventsOn;
            manager.setAllOfType(MiscUtils.ROOM_TYPE.EVENT, eventsOn, MiscUtils.isAltPressed());
            SoundHelper.playDeck(eventsOn);
        });
        RightClickWatcher.watchHbPermanent(legendItems.get(1).hb, this, onRightClick -> {
            shopsOn = !shopsOn;
            manager.setAllOfType(MiscUtils.ROOM_TYPE.MERCHANT, shopsOn, MiscUtils.isAltPressed());
            SoundHelper.playDeck(shopsOn);
        });
        RightClickWatcher.watchHbPermanent(legendItems.get(2).hb, this, onRightClick -> {
            treasuresOn = !treasuresOn;
            manager.setAllOfType(MiscUtils.ROOM_TYPE.TREASURE, treasuresOn, MiscUtils.isAltPressed());
            SoundHelper.playDeck(treasuresOn);
        });

        RightClickWatcher.watchHbPermanent(legendItems.get(3).hb, this, onRightClick -> {
            restsOn = !restsOn;
            manager.setAllOfType(MiscUtils.ROOM_TYPE.REST, restsOn, MiscUtils.isAltPressed());
            SoundHelper.playDeck(restsOn);
        });
        RightClickWatcher.watchHbPermanent(legendItems.get(4).hb, this, onRightClick -> {
            monstersOn = !monstersOn;
            manager.setAllOfType(MiscUtils.ROOM_TYPE.MONSTER, monstersOn, MiscUtils.isAltPressed());
            SoundHelper.playDeck(monstersOn);
        });
        RightClickWatcher.watchHbPermanent(legendItems.get(5).hb, this, onRightClick -> {
            elitesOn = !elitesOn;
            manager.setAllOfType(MiscUtils.ROOM_TYPE.ELITE, elitesOn, MiscUtils.isAltPressed());
            SoundHelper.playDeck(elitesOn);
        });
    }

    public void reset() {
        eventsOn = shopsOn = treasuresOn = restsOn = monstersOn = elitesOn = false;
    }
}
