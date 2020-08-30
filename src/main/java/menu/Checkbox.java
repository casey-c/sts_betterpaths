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

import java.util.function.Consumer;

public class Checkbox {
    private boolean visible;
    private float x, y;
    private boolean checked;

    private Hitbox hb;
    private Consumer<Checkbox> onToggle;

    public static final float BUTTON_WIDTH = 32.0f;
    public static final float BUTTON_HEIGHT = 32.0f;

    private static final Texture CHECKBOX_DEFAULT = new Texture("images/checkbox/checkbox.png");
    private static final Texture CHECKBOX_CHECKED = new Texture("images/checkbox/checkbox_checked.png");

    public Checkbox(float x, float y, boolean startChecked, Consumer<Checkbox> onToggle) {
        this.x = x;
        this.y = y;
        this.checked = startChecked;
        this.onToggle = onToggle;

        this.hb = new Hitbox(BUTTON_WIDTH, BUTTON_HEIGHT);

        ClickWatcher.watchHB(hb, this, onClickHandler -> {
            CardCrawlGame.sound.play("UI_CLICK_1");
            toggle();
            onToggle.accept(this);
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

    public void setChecked(boolean val) {
        this.checked = val;
    }

    public boolean isChecked() {
        return checked;
    }

    public void toggle() {
        checked = !checked;
        onToggle.accept(this);
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

        if (checked) {
            sb.draw(CHECKBOX_CHECKED, x, y);
        }
        else {
            sb.draw(CHECKBOX_DEFAULT, x, y);
        }

        hb.render(sb);
    }
}
