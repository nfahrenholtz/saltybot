package saltybot.bot.common;

public class Player {

    private final String playerName;

    public Player(final String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Player player = (Player) o;
        return playerName.equals(player.playerName);

    }

    @Override
    public int hashCode() {
        return playerName.hashCode();
    }
}
