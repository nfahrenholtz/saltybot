package saltybot.app;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import saltybot.api.common.Credentials;
import saltybot.bot.Engine;
import saltybot.bot.exception.SaltyBotException;

public class Application {

    private static final Logger LOGGER = LogManager.getFormatterLogger(Application.class);


    public static void main(String[] args) {
        final Credentials credentials = new Credentials(args[0], args[1]);
        final Engine engine = new Engine();
        try {
            engine.run(credentials);
        } catch (SaltyBotException e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        }
    }
}
