package saltybot.api.excaption;

public class SaltyException extends Exception {

    public SaltyException(final String message) {
        super(message);
    }
    
    public SaltyException(final Throwable cause) {
        super(cause);
    }

    public SaltyException(final String message,
                          final Throwable cause) {
        super(message, cause);
    }
    
}
