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

    private final static Texture BACKGROUND = new Texture("images/highlight/background.png");
    private final static Texture COLOR = new Texture("images/highlight/color.png");

    public NodeHighlight(Hitbox hb, Color color) {
        this.color = color;
        this.hb = hb;
    }

    public void toggle() {
        visible = !visible;

        if (visible)
            CardCrawlGame.sound.play("DECK_OPEN");
        else
            CardCrawlGame.sound.play("DECK_CLOSE");
    }


    @Override
    public void render(SpriteBatch sb) {
        if (AbstractDungeon.screen != AbstractDungeon.CurrentScreen.MAP)
            return;

        if (!visible)
            return;

        sb.setColor(Color.WHITE);
        sb.draw(BACKGROUND, hb.x - 18.0f * Settings.scale, hb.y - 18.0f * Settings.scale);

        sb.setColor(color);
        sb.draw(COLOR, hb.x - 18.0f * Settings.scale, hb.y - 18.0f * Settings.scale);
    }
}
