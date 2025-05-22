package org.example;

@SuppressWarnings("unused")
sealed public interface Result<T, E> {
    T getOrThrow() throws RuntimeException;

    record Ok<T, E>(T value) implements Result<T, E> {
        @Override
        public T getOrThrow() {
            return value;
        }
    }

    record Err<T, E>(E error) implements Result<T, E> {
        @Override
        public T getOrThrow() throws RuntimeException {
            throw new RuntimeException(error.toString());
        }
    }
}