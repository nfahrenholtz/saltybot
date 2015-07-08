package saltybot.api.common;

public class Pairing {

    public static Pairing create(final State state) {
        return new Pairing(state.getPlayerOneName(), state.getPlayerTwoName());
    }

    private final String playerOne;
    private final String playerTwo;

    public Pairing(final String playerOne,
                   final String playerTwo) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
    }

    public String getPlayerOne() {
        return playerOne;
    }

    public String getPlayerTwo() {
        return playerTwo;
    }

    @Override
    public String toString() {
        return "Pairing{" +
                "playerOne='" + playerOne + '\'' +
                ", playerTwo='" + playerTwo + '\'' +
                '}';
    }
}
