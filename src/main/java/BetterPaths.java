import basemod.BaseMod;
import basemod.interfaces.PostInitializeSubscriber;
import basemod.interfaces.PostUpdateSubscriber;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
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

//        if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.MAP) {
//        }
    }
}
