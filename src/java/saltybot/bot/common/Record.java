package saltybot.bot.common;

public class Record {

    private final double totalWins;
    private final double totalLosses;

    public Record(final double totalWins, final double totalLosses) {
        this.totalWins = totalWins;
        this.totalLosses = totalLosses;
    }

    public double getTotalWins() {
        return totalWins;
    }
    public double getTotalLosses() {
        return totalLosses;
    }

    @Override
    public String toString() {
        return "Record{" +
                "totalWins=" + totalWins +
                ", totalLosses=" + totalLosses +
                '}';
    }
}
