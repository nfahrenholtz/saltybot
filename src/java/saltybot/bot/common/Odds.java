package saltybot.bot.common;

public class Odds {

    private final Player playerOne;
    private final Player playerTwo;
    private final Favor favored;
    private final double odds;

    public Odds(final Player playerOne,
                final Player playerTwo,
                final Favor favored,
                final double odds) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.favored = favored;
        this.odds = odds;
    }

    public Player getPlayerOne() {
        return playerOne;
    }

    public Player getPlayerTwo() {
        return playerTwo;
    }

    public Favor getFavored() {
        return favored;
    }

    public Player getFavoredPlayer() {
        return favored.equals(Favor.PLAYER_ONE) ? playerOne : (favored.equals(Favor.PLAYER_TWO) ? playerTwo : null);
    }

    public double getOdds() {
        return odds;
    }

    @Override
    public String toString() {
        return "Odds{" +
                "playerOne=" + playerOne +
                ", playerTwo=" + playerTwo +
                ", favored=" + favored +
                ", odds=" + odds +
                '}';
    }
}
