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

@SpireInitializer
public class ClickWatcher implements PreRenderSubscriber {
    private static class HitboxMapObject {
        private Hitbox hb;
        private Consumer<Object> onClick;
        public Object item;

        public HitboxMapObject(Hitbox hb, Object item, Consumer<Object> onClick) {
            this.hb = hb;
            this.onClick = onClick;
            this.item = item;
        }

        public void click() {
            if (hb.hovered)
                onClick.accept(item);
        }
    }

    private ArrayList<HitboxMapObject> trackedHB;
    private boolean mouseDown = false;

    // Singleton pattern
    private static class ClickWatcherHolder { private static final ClickWatcher INSTANCE = new ClickWatcher(); }
    private static ClickWatcher getInstance() { return ClickWatcherHolder.INSTANCE; }

    public ClickWatcher() {
        trackedHB = new ArrayList<>();
        BaseMod.subscribe(this);
    }

    public static void initialize() { new ClickWatcherHolder(); }

    public static void watchHB(Hitbox hb, Object item, Consumer<Object> onClick) {
        ClickWatcher watcher = getInstance();

        HitboxMapObject obj = new HitboxMapObject(hb, item, onClick);
        watcher.trackedHB.add(obj);
    }

    private void clickHandler() {
        for (HitboxMapObject obj : trackedHB)
            obj.click();
    }

    @Override
    public void receiveCameraRender(OrthographicCamera orthographicCamera) {
        // This pre render can fire before the dungeon is even made, so it's possible to crash here unless we handle this case
        if (!CardCrawlGame.isInARun() || CardCrawlGame.dungeon == null)
            return;

        if (InputHelper.isMouseDown) {
            mouseDown = true;
        } else {
            // We already had the mouse down, and now we released, so fire our right click event
            if (mouseDown) {
                clickHandler();
                mouseDown = false;
            }
        }
    }
}

