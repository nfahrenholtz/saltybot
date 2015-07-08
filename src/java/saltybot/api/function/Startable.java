package saltybot.api.function;

@FunctionalInterface
public interface Startable<E extends Throwable> {
    public abstract void start() throws E;
}