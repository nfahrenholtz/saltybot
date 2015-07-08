package saltybot.api.common;


import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import saltybot.api.constant.SaltyConstants;

import java.util.function.Function;

public class Outcome {

    public static Outcome create(final State state) {
        final String status = state.getStatus();
        final Victor victor;

        switch (status) {
            case SaltyConstants.STATUS_PLAYER_ONE:
                victor = Victor.PLAYER_ONE;
                break;
            case SaltyConstants.STATUS_PLAYER_TWO:
                victor = Victor.PLAYER_TWO;
                break;
            default: //todo: maybe include tie? otherwise unknown result
                victor = Victor.UNKNOWN;
                break;
        }

        final Function<String, Double> toDouble = (s) -> {
            return NumberUtils.toDouble(StringUtils.remove(s, ','), Double.NaN);
        };

        return new Outcome(state.getPlayerOneName(),
                           toDouble.apply(state.getPlayerOneBetPool()),
                           state.getPlayerTwoName(),
                           toDouble.apply(state.getPlayerTwoBetPool()),
                           victor);
    }

    private final String playerOneName;
    private final Double playerOneBetPool;
    private final String playerTwoName;
    private final Double playerTwoBetPool;
    private final Victor victor;

    public Outcome(final String playerOneName,
                   final Double playerOneBetPool,
                   final String playerTwoName,
                   final Double playerTwoBetPool,
                   final Victor victor) {
        this.playerOneName = playerOneName;
        this.playerOneBetPool = playerOneBetPool;
        this.playerTwoName = playerTwoName;
        this.playerTwoBetPool = playerTwoBetPool;
        this.victor = victor;
    }

    public String getPlayerOneName() {
        return playerOneName;
    }

    public Double getPlayerOneBetPool() {
        return playerOneBetPool;
    }

    public String getPlayerTwoName() {
        return playerTwoName;
    }

    public Double getPlayerTwoBetPool() {
        return playerTwoBetPool;
    }

    public Victor getVictor() {
        return victor;
    }

    @Override
    public String toString() {
        return "Outcome{" +
                "playerOneName='" + playerOneName + '\'' +
                ", playerOneBetPool=" + playerOneBetPool +
                ", playerTwoName='" + playerTwoName + '\'' +
                ", playerTwoBetPool=" + playerTwoBetPool +
                ", victor=" + victor +
                '}';
    }
}
