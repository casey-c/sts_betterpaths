package utils;

import com.megacrit.cardcrawl.core.CardCrawlGame;

public class SoundHelper {
    // plays deck sound either on or off
    public static void playDeck(boolean on) {
        if (on)
            CardCrawlGame.sound.play("DECK_OPEN");
        else
            CardCrawlGame.sound.play("DECK_CLOSE");
    }
}
