package saltybot.bot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import saltybot.api.common.Balance;
import saltybot.api.common.Pairing;
import saltybot.api.common.Wager;
import saltybot.bot.common.Favor;
import saltybot.bot.common.Odds;
import saltybot.bot.common.Player;

import java.util.Random;

public class Bettor {

    private static final Logger LOGGER = LogManager.getFormatterLogger(Engine.class);

    private final Registrar registrar;
    private final Random random;
    private final int baseBet;

    public Bettor(final Registrar registrar) {
        this.registrar = registrar;
        random = new Random();
        baseBet = 100;
    }

    public Wager determineWager(final Balance balance,
                                final Pairing pairing) {
        final Player player1 = new Player(pairing.getPlayerOne());
        final Player player2 = new Player(pairing.getPlayerTwo());
        final Odds odds = registrar.determineOdds(player1, player2);

        int wager = 0, percent = 10;
        Player favored =  odds.getFavoredPlayer();
        final double winPercentage = odds.getOdds();


        if (odds.getFavored().equals(Favor.NONE)) {
            favored = random.nextGaussian() > 0 ? player1 : player2;
            percent = 2;
        }

        wager =  (baseBet + (int)Math.floor(balance.getBalance() * (percent / 100d) * winPercentage));

        LOGGER.info(pairing.getPlayerOne() + " vs. " + pairing.getPlayerTwo() + " -- wagered " + wager + " on " + favored.getPlayerName() + ". Salty probability: " + odds.getOdds());

        return new Wager(favored.getPlayerName(), wager);
    }
}
