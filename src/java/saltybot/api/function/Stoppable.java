package saltybot.api.function;

@FunctionalInterface
public interface Stoppable<E extends Throwable> {
    public abstract void stop() throws E;
}
