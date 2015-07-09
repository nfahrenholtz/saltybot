package saltybot.bot.graph;

import saltybot.bot.common.Player;
import saltybot.bot.common.Record;
import saltybot.graph.DirectedGraph;
import saltybot.graph.Edge;

import java.util.List;

public class SaltyGraph {

    private final DirectedGraph<Player, SaltyWeight> graph;

    public SaltyGraph() {
        graph = new DirectedGraph<>(() -> new SaltyWeight(0, 0));
    }

    public SaltyGraph update(final Player winner, final Player loser) {

        //update winning edge
        graph.update(winner, loser, (weight) -> {
            return new SaltyWeight(weight.getWins() + 1, weight.getLosses());
        });

        //update losing edge
        graph.update(loser, winner, (weight) -> {
            return new SaltyWeight(weight.getWins(), weight.getLosses() + 1);
        });

        return this;
    }

    public double findShortestDistance(final Player start, final Player finish) {
        final List<Edge<Player, SaltyWeight>> path = graph.findShortestPath(start, finish);

        double distance = path.size() > 0 ? 0d : Double.POSITIVE_INFINITY;
        for (final Edge<Player, SaltyWeight> edge: path) {
            distance += edge.getWeightValue();
        }

        return distance;
    }

    public Record getRecord(final Player player) {
        double totalWins = 0d, totalLosses = 0d;

        for (final Edge<Player, SaltyWeight> edge: graph.retrieve(player)) {
            totalLosses += edge.getWeight().getLosses();
            totalWins += edge.getWeight().getWins();
        }

        return new Record(totalWins, totalLosses);
    }
}
