package menu;

import basemod.BaseMod;
import basemod.interfaces.RenderSubscriber;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import highlight.HighlightManager;
import legend.LegendHelper;
import menu.ColorPicker;

public class HighlightMenu implements RenderSubscriber {
    private static final Texture SCREEN = new Texture("images/highlight_menu.png");
    private static final float SCREEN_X = 1486.0f;
    private static final float SCREEN_Y = 67.0f;

    private static final float CONTENT_X = 37.0f;

    private static final float TITLE_Y = 209.0f;
    private static final float COLOR_PICKER_Y = 130.0f;
    private static final float UNREACHABLE_Y = 114.0f;

    private ColorPicker colorPicker;

    private HighlightManager manager;
    private LegendHelper legendHelper;

    public HighlightMenu() {
        BaseMod.subscribe(this);

        // Setup the backend stuff
        manager = new HighlightManager();

        legendHelper = new LegendHelper(manager);
        legendHelper.setup();

        // Setup UI
        colorPicker = new ColorPicker(this, SCREEN_X + CONTENT_X, SCREEN_Y + COLOR_PICKER_Y);
    }

    @Override
    public void receiveRender(SpriteBatch sb) {
        if (!CardCrawlGame.isInARun())
            return;

        if (AbstractDungeon.screen != AbstractDungeon.CurrentScreen.MAP) {
            if (colorPicker.isVisible())
                colorPicker.hide();
            return;
        }

        // TODO: scaling

        // background
        sb.setColor(Color.WHITE);
        sb.draw(SCREEN, SCREEN_X, SCREEN_Y);

        // title text
        FontHelper.renderFontLeftTopAligned(sb, FontHelper.tipBodyFont, "Highlight Menu", SCREEN_X + CONTENT_X, SCREEN_Y + TITLE_Y, Settings.GOLD_COLOR);
        FontHelper.renderFontLeftTopAligned(sb, FontHelper.tipBodyFont, "Hide Unreachable:", SCREEN_X + CONTENT_X, SCREEN_Y + UNREACHABLE_Y, Color.GRAY);

        // picker
        if (!colorPicker.isVisible())
            colorPicker.show();

        colorPicker.update();
        colorPicker.render(sb);
    }

    public void setColor(Color color) {
        manager.setColor(color);
    }

}
