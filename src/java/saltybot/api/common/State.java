package saltybot.api.common;

import org.json.JSONObject;

public class State {

    public static State create(final JSONObject object) {
        return new State(object.getString("p1name"),
                         object.getString("p1total"),
                         object.getString("p2name"),
                         object.getString("p2total"),
                         object.getString("remaining"),
                         object.getString("status"),
                         object.getString("alert"),
                         object.getInt("x"));
    }

    private final String playerOneName;
    private final String playerOneBetPool;
    private final String playerTwoName;
    private final String playerTwoBetPool;
    private final String remaining;
    private final String status;
    private final String alert;
    private final Integer x;

    public State(final String playerOneName,
                 final String playerOneBetPool,
                 final String playerTwoName,
                 final String playerTwoBetPool,
                 final String remaining,
                 final String status,
                 final String alert,
                 final Integer x) {

        this.playerOneName = playerOneName;
        this.playerOneBetPool = playerOneBetPool;
        this.playerTwoName = playerTwoName;
        this.playerTwoBetPool = playerTwoBetPool;
        this.remaining = remaining;
        this.status = status;
        this.alert = alert;
        this.x = x;
    }

    public String getPlayerOneName() {
        return playerOneName;
    }

    public String getPlayerOneBetPool() {
        return playerOneBetPool;
    }

    public String getPlayerTwoName() {
        return playerTwoName;
    }

    public String getPlayerTwoBetPool() {
        return playerTwoBetPool;
    }

    public String getRemaining() {
        return remaining;
    }

    public String getStatus() {
        return status;
    }

    public String getAlert() {
        return alert;
    }

    public Integer getX() {
        return x;
    }

    @Override
    public String toString() {
        return "State{" +
                "playerOneName='" + playerOneName + '\'' +
                ", playerOneBetPool='" + playerOneBetPool + '\'' +
                ", playerTwoName='" + playerTwoName + '\'' +
                ", playerTwoBetPool='" + playerTwoBetPool + '\'' +
                ", remaining='" + remaining + '\'' +
                ", status='" + status + '\'' +
                ", alert='" + alert + '\'' +
                ", x=" + x +
                '}';
    }
}
