package menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.Hitbox;
import utils.ClickWatcher;
import utils.MiscUtils;
import utils.MyColors;

import java.util.ArrayList;

public class ColorPicker {

    private static class ColorObject {
        private static final Texture TEX_COLOR = new Texture("images/colorpicker/color_picker_center.png");
        private static final Texture TEX_FRAME = new Texture("images/colorpicker/color_picker_frame.png");
        private static final Texture TEX_X = new Texture("images/colorpicker/x.png");

        public static final float TEX_WIDTH = 40.0f;
        public static final float TEX_HEIGHT = 40.0f;

        private Color color;
        private static final Color BORDER_COLOR_NOT_SELECTED = MyColors.GRAY_BORDER_COLOR;
        private static final Color BORDER_COLOR_SELECTED = new Color(0.0f, 0.0f, 0.0f, 1.0f);

        private float x, y;
        private boolean selected = false;
        private boolean visible = false;
        private boolean hovered = false;

        public Hitbox hb;

        public ColorObject(ColorPicker parent, Color color, float x, float y ) {
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

            if (selected)
                sb.setColor(BORDER_COLOR_SELECTED);
            else
                sb.setColor(BORDER_COLOR_NOT_SELECTED);

            sb.draw(TEX_FRAME, x, y);

            if (MiscUtils.isAltPressed()) {
                sb.setColor(Color.WHITE);
                sb.draw(TEX_X, x, y);
            }

        }
    }

//    public static final Color COLOR_PURPLE = new Color(0.914f, 0.478f, 0.937f, 0.8f);
//    public static final Color COLOR_GREEN = new Color(0.0f, 1.0f, 0.0f, 0.8f);

    private static final float PADDING = 7.0f;

    private float x, y;

    //private ColorObject purple, green;
    private HighlightMenu parent;
    private ColorObject white, green, blue, pink, purple, redx;
    private ArrayList<ColorObject> objects = new ArrayList<>();

    private boolean visible = false;

    public ColorPicker(HighlightMenu parent, float x, float y) {
        this.parent = parent;
        this.x = x;
        this.y = y;

        white = new ColorObject(this, MyColors.LIGHT_GRAY, x, y);
        green = new ColorObject(this, MyColors.DARK_GREEN, x + (ColorObject.TEX_WIDTH + PADDING), y);
        blue = new ColorObject(this, MyColors.LIGHT_BLUE, x + 2*(ColorObject.TEX_WIDTH + PADDING), y);
        pink = new ColorObject(this, MyColors.LIGHT_PINK, x + 3*(ColorObject.TEX_WIDTH + PADDING), y);
        purple = new ColorObject(this, MyColors.DEEP_PURPLE, x + 4*(ColorObject.TEX_WIDTH + PADDING), y);
        redx = new ColorObject(this, MyColors.PASTEL_RED, x + 5*(ColorObject.TEX_WIDTH + PADDING), y);

        objects.add(white);
        objects.add(green);
        objects.add(blue);
        objects.add(pink);
        objects.add(purple);
        objects.add(redx);

        // TODO: load from config?
        select(white);
    }

    public boolean isVisible() {
        return visible;
    }

    public void show() {
        visible = true;
        for (ColorObject o : objects)
            o.show();
    }

    public void hide() {
        visible = false;
        for (ColorObject o : objects)
            o.hide();
    }

    public void update() {
        if (!visible)
            return;

        for (ColorObject o : objects)
            o.update();
    }

    public void render(SpriteBatch sb) {
        for (ColorObject o : objects)
            o.render(sb);
    }

    public void clearSelection() {
        for (ColorObject o : objects)
            o.setSelected(false);
    }

    public void select(ColorObject obj) {
        clearSelection();
        obj.setSelected(true);

        parent.setColor(obj.color);
    }
}
