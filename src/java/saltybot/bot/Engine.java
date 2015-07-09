package saltybot.bot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import saltybot.api.common.Credentials;
import saltybot.api.excaption.SaltyException;
import saltybot.api.function.Stoppable;
import saltybot.api.web.AsyncClient;
import saltybot.bot.exception.SaltyBotException;

/**
 * Engine provides a run method to start a salty betting bot
 */
public class Engine {
    private static final Logger LOGGER = LogManager.getFormatterLogger(Engine.class);

    public Engine() {

    }

    public Stoppable run(final Credentials credentials) throws SaltyBotException {
        final AsyncClient client = new AsyncClient();
        final Registrar registrar = new Registrar();
        final Bettor bettor = new Bettor(registrar);

        client.betPhaseInitiated(bettor::determineWager);
        client.matchOutcome(registrar::registerOutcome);
        client.betsUpdate(registrar::registerBets);

        try {
            LOGGER.info("Salty Session Started: " + credentials.getEmail());
            final Stoppable stoppable = client.spawn(credentials);
            //thread is not a daemon so the application will not terminate
            return stoppable;
        } catch (final SaltyException e) {
            LOGGER.error("Failed to spawn polling thread.", e);
            throw new SaltyBotException(e);
        }
    }

}
