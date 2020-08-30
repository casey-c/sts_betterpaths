package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheEnding;
import menu.HighlightMenu;
import utils.PreTopBarRenderHelper;
import utils.RightClickWatcher;

@SpirePatch(
        clz = TheEnding.class,
        method = "generateSpecialMap"
)
public class PostTheEndingGenerateMapPatch {
    @SpirePostfixPatch
    public static void Postfix() {
        System.out.println("****************************************************************************");
        System.out.println("OJB: THE ENDING HAS FINISHED GENERATING (SPECIAL) MAP -----------------------------------");
        System.out.println("****************************************************************************");

        if (!CardCrawlGame.isInARun())
            return;

        RightClickWatcher.clearTemporaryHB();
        PreTopBarRenderHelper.clearAll();
        HighlightMenu.getInstance().rebuildManager();
    }
}
