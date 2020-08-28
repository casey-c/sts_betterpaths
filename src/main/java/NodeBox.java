import basemod.BaseMod;
import basemod.interfaces.PreRenderSubscriber;
import basemod.interfaces.PreRoomRenderSubscriber;
import basemod.interfaces.RenderSubscriber;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;

/*
public class NodeBox implements RenderSubscriber {
    public Hitbox hb;
    private boolean visible = false;
    private int floorNum = -1;

    private final static Texture BACKGROUND = new Texture("images/highlight/background.png");
    private final static Texture COLOR = new Texture("images/highlight/color.png");

    public Color PURPLE_COLOR = new Color(0.914f, 0.478f, 0.937f, 1.0f);

    public NodeBox(Hitbox hb, int floorNum) {
        this.hb = hb;
        this.floorNum = floorNum;
        BaseMod.subscribe(this);
    }

    public void toggle() {
        visible = !visible;

        if (visible)
            CardCrawlGame.sound.play("DECK_OPEN");
        else
            CardCrawlGame.sound.play("DECK_CLOSE");
    }

    @Override
    public void receiveRender(SpriteBatch sb) {

        if (!CardCrawlGame.isInARun() || !visible || AbstractDungeon.screen != AbstractDungeon.CurrentScreen.MAP)
            return;

        // TODO: config option (hide past paths)
        if (this.floorNum < AbstractDungeon.floorNum)
            return;

        // TODO: stencil buffer instead?
        //    need to find a way to render beneath the top bar
//        float alpha = 1.0f;
//        final float THRESHOLD = 921.0f;
//        if (hb.y > THRESHOLD)
//            alpha = (1080 - hb.y)

        // background
        sb.setColor(Color.WHITE);
        sb.draw(BACKGROUND, hb.x - 18.0f * Settings.scale, hb.y - 18.0f * Settings.scale);

        sb.setColor(PURPLE_COLOR);
        sb.draw(COLOR, hb.x - 18.0f * Settings.scale, hb.y - 18.0f * Settings.scale);

        // TODO: custom colors
//        sb.setColor(Color.WHITE);
//        sb.draw(TEX, hb.x - 18.0f * Settings.scale, hb.y - 18.0f * Settings.scale);

    }
}

 */
