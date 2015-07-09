package saltybot.bot.common;

public enum Favor {

    PLAYER_ONE("player1"),
    PLAYER_TWO("player2"),
    NONE("none");

    private final String playerType;

    private Favor(final String playerType) {
        this.playerType = playerType;
    }

    public String getPlayerType() {
        return playerType;
    }

    @Override
    public String toString() {
        return "Favor{" +
                "player='" + playerType + '\'' +
                '}';
    }
}
