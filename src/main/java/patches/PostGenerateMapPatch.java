package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheEnding;
import menu.HighlightMenu;
import utils.PreTopBarRenderHelper;
import utils.RightClickWatcher;

@SpirePatch(
        clz = AbstractDungeon.class,
        method = "generateMap"
)
public class PostGenerateMapPatch {
    @SpirePostfixPatch
    public static void Postfix() {
        System.out.println("****************************************************************************");
        System.out.println("OJB: ABSTRACT DUNGEON HAS FINISHED GENERATING MAP -----------------------------------");
        System.out.println("****************************************************************************");

        if (!CardCrawlGame.isInARun())
            return;

        RightClickWatcher.clearTemporaryHB();
        PreTopBarRenderHelper.clearAll();
        HighlightMenu.getInstance().rebuildManager();
    }
}
