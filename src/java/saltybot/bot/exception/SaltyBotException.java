package saltybot.bot.exception;

public class SaltyBotException extends Exception {

    public SaltyBotException(final String message) {
        super(message);
    }

    public SaltyBotException(final Throwable cause) {
        super(cause);
    }

    public SaltyBotException(final String message,
                             final Throwable cause) {
        super(message, cause);
    }

}
