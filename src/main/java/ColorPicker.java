import basemod.BaseMod;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;

public class ColorPicker {

    private static class ColorObject {
        private static final Texture TEX_COLOR = new Texture("images/colorpicker/color.png");
        private static final Texture TEX_FRAME = new Texture("images/colorpicker/frame.png");

        public static final float TEX_WIDTH = 50.0f;
        public static final float TEX_HEIGHT = 50.0f;

        private Color color;
        private float x, y;
        private boolean selected = false;
        private boolean visible = false;
        private boolean hovered = false;

        public Hitbox hb;

        public ColorObject(ColorPicker parent, Color color, float x, float y) {
            hb = new Hitbox(TEX_WIDTH, TEX_HEIGHT);
            ClickWatcher.watchHB(hb, this, onClick -> {
                System.out.println("OJB: clicked color object " + x);
                CardCrawlGame.sound.play("UI_CLICK_1");
                parent.select(this);
            });

            this.x = x;
            this.y = y;
            this.color = color;
        }

        public void setSelected(boolean val) {
            this.selected = val;
        }

        public void show() {
            hb.move(x + 0.5f * TEX_WIDTH, y + 0.5f * TEX_HEIGHT);
            visible = true;
        }

        public void hide() {
            hb.move(1000000, 100000);
            visible = false;
        }

        public void update() {
            hb.update();
            if (!hovered && hb.hovered) {
                CardCrawlGame.sound.play("UI_HOVER");
                hovered = true;
            }
            if (hovered && !hb.hovered)
                hovered = false;
        }

        public void render(SpriteBatch sb) {
            // TODO: move below visible -- better for debug now?
            hb.render(sb);

            if (!visible)
                return;


            // Draw the color area
            sb.setColor(color);
            sb.draw(TEX_COLOR, x, y);

            if (selected) {
                sb.setColor(Color.WHITE);
                sb.draw(TEX_FRAME, x, y);
            }
        }
    }

    public static final Color COLOR_PURPLE = new Color(0.914f, 0.478f, 0.937f, 0.8f);
    public static final Color COLOR_GREEN = new Color(0.0f, 1.0f, 0.0f, 0.8f);

    private static final float PADDING = 7.0f;

    private float x, y;

    private ColorObject purple, green;
    private boolean visible = false;

    public ColorPicker(float x, float y) {
        this.x = x;
        this.y = y;

        purple = new ColorObject(this, COLOR_PURPLE, x, y);
        green = new ColorObject(this, COLOR_GREEN, x + ColorObject.TEX_WIDTH + PADDING, y);
    }

    public boolean isVisible() {
        return visible;
    }

    public void show() {
        visible = true;
        purple.show();
        green.show();
    }

    public void hide() {
        visible = false;
        purple.hide();
        green.hide();
    }

    public void update() {
        if (!visible)
            return;

        purple.update();
        green.update();
    }

    public void render(SpriteBatch sb) {
        purple.render(sb);
        green.render(sb);
    }

    public void clearSelection() {
        purple.setSelected(false);
        green.setSelected(false);
    }

    public void select(ColorObject obj) {
        clearSelection();
        obj.setSelected(true);
    }
}
