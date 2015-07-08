package saltybot.bot;

import saltybot.api.common.Balance;
import saltybot.api.common.Data;
import saltybot.api.common.Outcome;
import saltybot.api.common.Victor;
import saltybot.api.store.DataStore;
import saltybot.api.store.NullStore;
import saltybot.bot.common.Favor;
import saltybot.bot.common.Odds;
import saltybot.bot.common.Player;
import saltybot.bot.common.Record;
import saltybot.bot.graph.SaltyGraph;

import java.util.Map;

public class Registrar {

    private final SaltyGraph graph = new SaltyGraph();

    public Registrar() {
        this(new NullStore());
    }

    public Registrar(final DataStore dataStore) {
        //ToDo: read graph data from a a data store
    }

    @Deprecated
    public Player determineFavored(final Player a, final Player b) {
        final double ad = graph.findShortestDistance(a, b);
        final double bd = graph.findShortestDistance(b, a);
        return ad > bd ? a : (ad < bd ? b : null);
    }

    public Odds determineOdds(final Player a, final Player b) {
        final double ad = graph.findShortestDistance(a, b);
        final double bd = graph.findShortestDistance(b, a);
        final Favor favored = ad > bd ? Favor.PLAYER_ONE : (ad < bd ? Favor.PLAYER_TWO : Favor.NONE);

        final Record recordA = graph.getRecord(a);
        final Record recordB = graph.getRecord(b);

        double aWinPct = (recordA.getTotalWins() + 1d ) / (recordA.getTotalWins() + recordA.getTotalLosses() + 1d );
        double bWinPct = (recordB.getTotalWins() + 1d ) / (recordB.getTotalWins() + recordB.getTotalLosses() + 1d );

        double odds = favored.equals(Favor.PLAYER_ONE) ? aWinPct / (aWinPct + bWinPct) : (favored.equals(Favor.PLAYER_TWO) ? bWinPct / (aWinPct + bWinPct) : .5d ); //odds based on win percentage for the favored or even odds

        return new Odds(a, b, favored, odds);
    }

    public void registerOutcome(final Balance balance, final Outcome outcome) {
        final Player winner, looser;

        if (outcome.getVictor().equals(Victor.PLAYER_ONE)) {
            winner = new Player(outcome.getPlayerOneName());
            looser = new Player(outcome.getPlayerTwoName());
        } else if (outcome.getVictor().equals(Victor.PLAYER_TWO)) {
            winner = new Player(outcome.getPlayerTwoName());
            looser = new Player(outcome.getPlayerTwoName());
        } else {
            return; //unknown victor, do nothing...
        }

        graph.update(winner, looser);
    }


    public void registerBets(final Map<String, Data> bets) {
        //ToDo: store the list of other users bets and used that to help calculate a wager
    }
}
