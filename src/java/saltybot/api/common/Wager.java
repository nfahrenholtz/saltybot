package saltybot.api.common;

public class Wager {
    
    private final String player;
    private final Integer wager;

    public Wager(final String player,
                 final Integer wager) {
        this.player = player;
        this.wager = wager;
    }

    public String getPlayer() {
        return player;
    }

    public Integer getWager() {
        return wager;
    }

    @Override
    public String toString() {
        return "Wager{" +
                "player='" + player + '\'' +
                ", wager=" + wager +
                '}';
    }
}
