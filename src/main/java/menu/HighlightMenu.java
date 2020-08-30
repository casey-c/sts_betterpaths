package menu;

import basemod.BaseMod;
import basemod.interfaces.RenderSubscriber;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import highlight.HighlightManager;
import legend.LegendHelper;
import menu.ColorPicker;

public class HighlightMenu implements RenderSubscriber {
    // Singleton pattern
    private static class HighlightMenuHolder { private static final HighlightMenu INSTANCE = new HighlightMenu(); }
    public static HighlightMenu getInstance() { return HighlightMenuHolder.INSTANCE; }

    private static final Texture SCREEN = new Texture("images/highlight_menu.png");
    private static final float SCREEN_X = 1486.0f;
    private static final float SCREEN_Y = 67.0f;

    private static final float CONTENT_X = 37.0f;
    private static final float CHECKBOX_X = 230.0f;

    private static final float TITLE_Y = 209.0f;
    private static final float COLOR_PICKER_Y = 130.0f;
    private static final float UNREACHABLE_Y = 112.0f;
    private static final float BUTTON_Y = 28.0f;
    private static final float CHECKBOX_Y = UNREACHABLE_Y - Checkbox.BUTTON_HEIGHT + 8.0f;

    private HighlightManager manager;
    //private LegendHelper legendHelper;

    private ColorPicker colorPicker;
    private ClearButton clearButton;
    private Checkbox hideUnreachableCheckbox;

    private boolean visible;

    private HighlightMenu() {
        BaseMod.subscribe(this);

        // Setup the backend stuff
        manager = new HighlightManager();
        rebuildManager();

        // Setup UI
        colorPicker = new ColorPicker(this, SCREEN_X + CONTENT_X, SCREEN_Y + COLOR_PICKER_Y);
        clearButton = new ClearButton(SCREEN_X + CONTENT_X, SCREEN_Y + BUTTON_Y, onClick -> {
            manager.clearAll();
        });

        hideUnreachableCheckbox = new Checkbox(SCREEN_X + CHECKBOX_X, SCREEN_Y + CHECKBOX_Y, false, onToggle -> {
            manager.setHideUnreachable(hideUnreachableCheckbox.isChecked());
        });
    }

    public void rebuildManager() {
        // TODO: may be necessary
        //manager.reset();

        manager.build();

//        legendHelper = new LegendHelper(manager);
//        legendHelper.setup();
    }


    private void hide() {
        visible = false;
        colorPicker.hide();
        clearButton.hide();
        hideUnreachableCheckbox.hide();
    }

    private void show() {
        visible = true;
        colorPicker.show();
        clearButton.show();
        hideUnreachableCheckbox.show();
    }

    @Override
    public void receiveRender(SpriteBatch sb) {
        if (!CardCrawlGame.isInARun())
            return;

        if (AbstractDungeon.screen != AbstractDungeon.CurrentScreen.MAP) {
            if (visible)
                hide();
            return;
        }
        else if (!visible) {
            show();
        }

        // TODO: scaling

        // background
        sb.setColor(Color.WHITE);
        sb.draw(SCREEN, SCREEN_X, SCREEN_Y);

        // title text
        FontHelper.renderFontLeftTopAligned(sb, FontHelper.tipBodyFont, "Highlight Menu", SCREEN_X + CONTENT_X, SCREEN_Y + TITLE_Y, Settings.GOLD_COLOR);
        FontHelper.renderFontLeftTopAligned(sb, FontHelper.tipBodyFont, "Hide Past Floors:", SCREEN_X + CONTENT_X, SCREEN_Y + UNREACHABLE_Y, Color.GRAY);

        colorPicker.update();
        colorPicker.render(sb);

        clearButton.update();
        clearButton.render(sb);

        hideUnreachableCheckbox.update();
        hideUnreachableCheckbox.render(sb);
    }

    public void setColor(Color color) {
        manager.setColor(color);
    }

}
