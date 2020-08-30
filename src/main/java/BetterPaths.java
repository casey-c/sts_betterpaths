import basemod.BaseMod;
import basemod.interfaces.PostInitializeSubscriber;
import basemod.interfaces.PostUpdateSubscriber;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import highlight.HighlightManager;
import legend.LegendHelper;
import menu.HighlightMenu;

@SpireInitializer
public class BetterPaths implements PostInitializeSubscriber, PostUpdateSubscriber {
    private HighlightMenu menu;


    public BetterPaths() {
        BaseMod.subscribe(this);
    }

    public static void initialize() {
        new BetterPaths();
    }

    @Override
    public void receivePostInitialize() {
        System.out.println("OJB: better paths init");
    }

    @Override
    public void receivePostUpdate() {
        if (!CardCrawlGame.isInARun())
            return;

        if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.MAP) {
            if (menu == null)
                menu = new HighlightMenu();

            // TODO: reupdate on map change or character change etc.
//            if (manager == null) {
//                manager = new HighlightManager();
//
//                legendHelper = new LegendHelper(manager);
//                legendHelper.setup();
//
//            }
        }
    }
}
