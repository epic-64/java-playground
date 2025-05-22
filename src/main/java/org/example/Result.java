package org.example;

@SuppressWarnings("unused")
sealed public interface Result<T, E> {
    record Ok<T, E>(T value) implements Result<T, E> {}
    record Err<T, E>(E error) implements Result<T, E> {}

    default T getOrThrow(String justification) throws RuntimeException {
        return switch (this) {
            case Ok(T value) -> value;
            case Err(E err) -> throw new RuntimeException("\n" + justification + " -> " + err.toString());
        };
    }

    default T getOrElse(T defaultValue) {
        return switch (this) {
            case Ok(T value) -> value;
            case Err(E err) -> defaultValue;
        };
    }
}