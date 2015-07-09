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

/**
 * Bettor calculates a wages based on information provided by the Registrar
 */
public class Bettor {
    private static final Logger LOGGER = LogManager.getFormatterLogger(Bettor.class);

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

        int wager, percent = 10;
        Favor favored =  odds.getFavored();
        final double winPercentage = odds.getOdds();

        if (odds.getFavored().equals(Favor.NONE)) {
            favored = random.nextGaussian() > 0 ? Favor.PLAYER_ONE : Favor.PLAYER_TWO;
            percent = 2;
        }

        wager = (baseBet + (int)Math.floor(balance.getBalance() * (percent / 100d) * winPercentage));
        final String favoredPlayerName = favored.equals(Favor.PLAYER_ONE) ? "player1" : "player2";

        LOGGER.info("Your balance is " + balance.getBalance() + ".");
        LOGGER.info("--- Current Match ---" );
        LOGGER.info(pairing.getPlayerOne() + " vs. " + pairing.getPlayerTwo());
        LOGGER.info("Wagered " + wager + " on " + (favoredPlayerName.equals("player1") ? player1.getPlayerName() : player2.getPlayerName()) + ".");

        return new Wager(favoredPlayerName, wager);
    }
}
