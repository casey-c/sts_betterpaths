package utils;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;

import java.util.ArrayList;

@SpireInitializer
public class PreTopBarRenderHelper {
    // Singleton pattern
    private static class PreTopBarRenderHelperHolder { private static final PreTopBarRenderHelper INSTANCE = new PreTopBarRenderHelper(); }
    private static PreTopBarRenderHelper getInstance() { return PreTopBarRenderHelperHolder.INSTANCE; }

    public static void initialize() {
        PreTopBarRenderHelperHolder holder = new PreTopBarRenderHelperHolder();
        getInstance();
    }

    private ArrayList<Renderable> renderables;

    public PreTopBarRenderHelper() {
        renderables = new ArrayList<>();
    }

    public static void addRenderable(Renderable r) {
        PreTopBarRenderHelper instance = getInstance();
        instance.renderables.add(r);
    }

    public static void renderAll(SpriteBatch sb) {
        PreTopBarRenderHelper instance = getInstance();
        for(Renderable r :  instance.renderables) {
            r.render(sb);
        }
    }

}
