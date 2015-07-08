package saltybot.api.common;

import org.json.JSONObject;

import java.util.function.Function;

public class Data {

    public static Data create(final JSONObject object) {
        final Function<String, String> get = (k) -> {
            return object.has(k) ? object.getString(k) : null;
        };

        return new Data(get.apply("n"),
                        get.apply("b"),
                        get.apply("p"),
                        get.apply("w"),
                        get.apply("r"),
                        get.apply("g"));
    }

    private final String userName;
    private final String bidAmount;
    private final String playerName;
    private final String w;
    private final String r;
    private final String g;

    public Data(final String userName,
                final String bidAmount,
                final String playerName,
                final String w,
                final String r,
                final String g) {

        this.userName = userName;
        this.bidAmount = bidAmount;
        this.playerName = playerName;
        this.w = w;
        this.r = r;
        this.g = g;
    }

    public String getUserName() {
        return userName;
    }

    public String getBidAmount() {
        return bidAmount;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getW() {
        return w;
    }

    public String getR() {
        return r;
    }

    public String getG() {
        return g;
    }

    @Override
    public String toString() {
        return "Data{" +
                "userName='" + userName + '\'' +
                ", bidAmount='" + bidAmount + '\'' +
                ", playerName='" + playerName + '\'' +
                ", w='" + w + '\'' +
                ", r='" + r + '\'' +
                ", g='" + g + '\'' +
                '}';
    }
}
