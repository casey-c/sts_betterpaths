package highlight;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import utils.Renderable;

public class NodeHighlight implements Renderable {
    private Color color;
    private boolean visible = false;
    public Hitbox hb;
    private boolean showX = false;
    private int floor;

    private final static Texture BACKGROUND = new Texture("images/highlight/background.png");
    private final static Texture COLOR = new Texture("images/highlight/color.png");
    private final static Texture X = new Texture("images/highlight/map_x.png");

    public static final float HIGHLIGHT_WIDTH = 100.0f * Settings.scale;
    public static final float HIGHLIGHT_HEIGHT = 100.0f * Settings.scale;

    public NodeHighlight(Hitbox hb, int floor, Color color) {
        this.color = color;
        this.floor = floor;
        this.hb = hb;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setShowX(boolean val) {
        this.showX = val;
    }

    public void toggleWithColor(Color newColor, boolean forceX) {
        this.showX = forceX;

        // Special case: just update the color to the new one
        if (visible && newColor != this.color) {
            this.color = newColor;
        }
        else {
            visible = !visible;
            this.color = newColor;

            if (visible)
                CardCrawlGame.sound.play("DECK_OPEN");
            else
                CardCrawlGame.sound.play("DECK_CLOSE");
        }
    }


    @Override
    public void render(SpriteBatch sb) {
        if (AbstractDungeon.screen != AbstractDungeon.CurrentScreen.MAP)
            return;

        if (!visible)
            return;

        // TODO
        if (HighlightManager.getHideUnreachable() && (floor < AbstractDungeon.floorNum))
            return;

        sb.setColor(Color.WHITE);
        sb.draw(BACKGROUND, hb.x - 18.0f * Settings.scale, hb.y - 18.0f * Settings.scale, HIGHLIGHT_WIDTH, HIGHLIGHT_HEIGHT);

        sb.setColor(color);
        sb.draw(COLOR, hb.x - 18.0f * Settings.scale, hb.y - 18.0f * Settings.scale, HIGHLIGHT_WIDTH, HIGHLIGHT_HEIGHT);

        if (showX)
            sb.draw(X, hb.x - 18.0f * Settings.scale, hb.y - 18.0f * Settings.scale, HIGHLIGHT_WIDTH, HIGHLIGHT_HEIGHT);
    }

    public void setVisible(boolean val) {
        this.visible = val;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
