package saltybot.api.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import saltybot.api.common.*;
import saltybot.api.constant.SaltyConstants;
import saltybot.api.excaption.SaltyException;
import saltybot.api.function.Startable;
import saltybot.api.function.Stoppable;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;


public class AsyncClient {
    private static final Logger LOGGER = LogManager.getFormatterLogger(AsyncClient.class);

    private long pollRate;
    private BiFunction<Balance, Pairing, Wager> betPhaseAction;
    private BiConsumer<Balance, Outcome> matchOutcome;
    private Consumer<Map<String, Data>> update;

    public AsyncClient() {
        betPhaseAction = (a, b) -> null;
        matchOutcome = (a, b) -> {};
        update = (a) -> {};
        pollRate = 25*1000; //default poll rate (25s)
    }

    public void setPollRate(final long pollRate) {
        this.pollRate = pollRate;
    }

    public Stoppable spawn(final Credentials credentials) throws SaltyException {
        final SaltybetClient client = new SaltybetClient();
        if (client.authenticate(credentials)) {
            final StatePollerTask task = new StatePollerTask(client);
            task.start();
            return task;
        }
        throw new SaltyException("Unable to spawn poller thread.");
    }

    public void betPhaseInitiated(final BiFunction<Balance, Pairing, Wager> action) {
        this.betPhaseAction = action;
    }

    public void matchOutcome(final BiConsumer<Balance, Outcome> outcome) {
        this.matchOutcome = outcome;
    }

    public void betsUpdate(final Consumer<Map<String, Data>> update) {
        this.update = update;
    }

    private class StatePollerTask implements Startable, Runnable, Stoppable {
        private final SaltybetClient client;
        private final AtomicBoolean running;
        private final Thread poller;

        private StatePollerTask(final SaltybetClient client) {
            this.running = new AtomicBoolean(false);
            this.poller = new Thread(this);
            this.client = client;
        }

        @Override
        public void start() {
            this.running.set(true);
            poller.start();
        }

        @Override
        public void run() {
            Object currentState = null;
            for (long next = 0; running.get(); ) {
                final long current = System.currentTimeMillis();
                try {
                    LOGGER.debug("next poll in " + (next - current) + "ms");

                    if (Long.compareUnsigned(next, current) < 0) {
                        final State state = client.state();
                        final String status = state.getStatus();

                        LOGGER.debug("polling state: %s", state.toString());

                        if (!status.equals(currentState)) {
                            if (status.equalsIgnoreCase(SaltyConstants.STATUS_OPEN)) {
                                final Balance balance = client.balance();
                                final ZData data = client.zdata();

                                LOGGER.debug("betting open: %s", data.toString());

                                final Wager wager;
                                final Pairing pairing = Pairing.create(state);
                                final Outcome outcome = Outcome.create(data.getState());

                                matchOutcome.accept(balance, outcome); //write to graph

                                wager = betPhaseAction.apply(balance, pairing); //read from graph

                                if (wager != null && wager.getWager() > 0) {
                                    client.bet(wager);
                                }

                            } else if (status.equalsIgnoreCase(SaltyConstants.STATUS_LOCKED)) {
                                final ZData data = client.zdata();

                                update.accept(data.getDataMap());
                            } else {
                                if (status.equals(SaltyConstants.STATUS_PLAYER_ONE) ||
                                    status.equals(SaltyConstants.STATUS_PLAYER_TWO)) {
                                    //this state is not an error, but will be determined within the open state
                                    continue; //skip setting next to force a recheck on state
                                }

                                throw new SaltyException("Invalid state: " + state);
                            }
                        }
                        currentState = status;
                        next = current + pollRate;
                    }
                } catch (final SaltyException e) {
                    LOGGER.error("error while polling client", e);
                    running.set(client.validate());
                    //ToDo: do retry logic if validate fails
                }

                try {
                    Thread.sleep(1000);
                } catch (final InterruptedException e) {
                    LOGGER.debug("poller interrupted");
                }
            }
        }

        @Override
        public void stop() throws Exception {
            running.set(false);
            poller.interrupt();
            poller.join();
        }
    }

}
