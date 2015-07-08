package saltybot.bot.graph;

import saltybot.graph.Weight;

public class SaltyWeight implements Weight {

    private final int wins, losses;

    public SaltyWeight(final int wins, final int losses) {
        this.wins = wins;
        this.losses = losses;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    @Override
    public double getValue() {
        final double total = wins + losses;
        return losses / total + 1d / (1d+wins);
    }
}
