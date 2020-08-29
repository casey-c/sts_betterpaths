package utils;

import basemod.BaseMod;
import basemod.interfaces.PreRenderSubscriber;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.input.InputHelper;

import java.util.ArrayList;
import java.util.function.Consumer;

/*
  repurposed from InfoMod
 */
@SpireInitializer
public class RightClickWatcher implements PreRenderSubscriber {
    private static class HitboxMapObject {
        private Hitbox hb;
        private Consumer<Object> onRightClick;
        public Object item;

        public HitboxMapObject(Hitbox hb, Object item, Consumer<Object> onRightClick) {
            this.hb = hb;
            this.onRightClick = onRightClick;
            this.item = item;
        }

        public void rightClick() {
            if (hb.hovered)
                onRightClick.accept(item);
        }
    }

    private ArrayList<HitboxMapObject> trackedHB;
    private boolean mouseDownRight = false;

    // Singleton pattern
    private static class RightClickWatcherHolder {
        private static final RightClickWatcher INSTANCE = new RightClickWatcher();
    }

    private static RightClickWatcher getInstance() {
        return RightClickWatcherHolder.INSTANCE;
    }

    public RightClickWatcher() {
        trackedHB = new ArrayList<>();
        BaseMod.subscribe(this);
    }

    public static void initialize() {
        new RightClickWatcherHolder();
    }

    public static void watchHB(Hitbox hb, Object item, Consumer<Object> onRightClick) {
        RightClickWatcher watcher = getInstance();

        HitboxMapObject obj = new HitboxMapObject(hb, item, onRightClick);
        watcher.trackedHB.add(obj);
    }

    private void rightClickHandler() {
        for (HitboxMapObject obj : trackedHB)
            obj.rightClick();
    }

    @Override
    public void receiveCameraRender(OrthographicCamera orthographicCamera) {
        // This pre render can fire before the dungeon is even made, so it's possible to crash here unless we handle this case
        if (!CardCrawlGame.isInARun() || CardCrawlGame.dungeon == null)
            return;

        if (InputHelper.isMouseDown_R) {
            mouseDownRight = true;
        } else {
            // We already had the mouse down, and now we released, so fire our right click event
            if (mouseDownRight) {
                rightClickHandler();
                mouseDownRight = false;
            }
        }
    }
}

