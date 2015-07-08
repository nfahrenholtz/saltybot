package saltybot.api.store;

import saltybot.api.common.Outcome;

public interface DataStore {

    public void write(final Outcome outcome);

}
