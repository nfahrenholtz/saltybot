package saltybot.api.function;

import java.util.Objects;
import java.util.function.Function;

@FunctionalInterface
public interface CheckedFunction<T, R> {

    public R apply(T t) throws Throwable;

    public default <V> CheckedFunction<V, R> compose(CheckedFunction<? super V, ? extends T> before) {
        Objects.requireNonNull(before);
        return (V v) -> apply(before.apply(v));
    }

    public default <V> CheckedFunction<T, V> andThen(CheckedFunction<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (T t) -> after.apply(apply(t));
    }

    public static <T> Function<T, T> identity() {
        return t -> t;
    }
}
