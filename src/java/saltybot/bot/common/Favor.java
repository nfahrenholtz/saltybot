package saltybot.bot.common;

public enum Favor {

    PLAYER_ONE("player1"),
    PLAYER_TWO("player2"),
    NONE("none");

    private final String player;

    private Favor(final String player) {
        this.player = player;
    }

    @Override
    public String toString() {
        return "Favor{" +
                "player='" + player + '\'' +
                '}';
    }
}
