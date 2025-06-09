package org.example;

@SuppressWarnings("unused")
sealed public interface Result<T, E> {
    record Ok<T, E>(T value) implements Result<T, E> {}
    record Err<T, E>(E error) implements Result<T, E> {}

    default boolean isOk() {
        return this instanceof Ok<T, E>;
    }

    default boolean isErr() {
        return this instanceof Err<T, E>;
    }

    default T expect(String assumption) throws RuntimeException {
        return switch (this) {
            case Ok(T value) -> value;
            case Err(E err) -> throw new RuntimeException(
                "\n" + assumption + " -> Expected Ok, but got Err: " + err.toString()
            );
        };
    }

    default E err() throws RuntimeException {
        return switch (this) {
            case Ok(T value) -> throw new RuntimeException("Expected Err, but got Ok: " + value.toString());
            case Err(E err) -> err;
        };
    }

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