package menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import utils.ClickWatcher;
import utils.SoundHelper;

import java.util.function.Consumer;

public class ClearButton {
    private boolean visible;
    private float x, y;

    private Hitbox hb;

    private static final float BUTTON_WIDTH = 250.0f * Settings.scale;
    private static final float BUTTON_HEIGHT = 50.0f * Settings.scale;

    private static final Texture BUTTON_DEFAULT = new Texture("images/button/button_default.png");
    private static final Texture BUTTON_HOVER = new Texture("images/button/button_hover.png");
    private static final Texture BUTTON_CLICK = new Texture("images/button/button_click.png");

    public ClearButton(float x, float y, Consumer<ClearButton> onClick) {
        this.x = x;
        this.y = y;

        this.hb = new Hitbox(BUTTON_WIDTH, BUTTON_HEIGHT);

        ClickWatcher.watchHB(hb, this, onClickHandler -> {
            CardCrawlGame.sound.play("UI_CLICK_1");
            onClick.accept(this);
        });
    }

    public void show() {
        this.visible =  true;
        hb.move(x + 0.5f * BUTTON_WIDTH, y + 0.5f * BUTTON_HEIGHT);
    }

    public void hide() {
        this.visible = false;
        hb.move(100000, 100000);
    }

    public void update() {
        if (!visible)
            return;

        //boolean oldHover = hb.hovered;
        hb.update();
        if (hb.justHovered)
            CardCrawlGame.sound.play("UI_HOVER");
    }

    public void render(SpriteBatch sb) {
        if (!visible)
            return;

        sb.setColor(Color.WHITE);

        if (hb.hovered) {
            if (InputHelper.isMouseDown) {
                sb.draw(BUTTON_CLICK, x, y, BUTTON_WIDTH, BUTTON_HEIGHT);
            }
            else {
                sb.draw(BUTTON_HOVER, x, y, BUTTON_WIDTH, BUTTON_HEIGHT);
            }
        }
        else {
            sb.draw(BUTTON_DEFAULT, x, y, BUTTON_WIDTH, BUTTON_HEIGHT);
        }

        // text
        FontHelper.renderFontCenteredHeight(sb, FontHelper.tipBodyFont, "Clear", x, y + BUTTON_HEIGHT / 2.0f, BUTTON_WIDTH, Settings.CREAM_COLOR);

        hb.render(sb);
    }
}
