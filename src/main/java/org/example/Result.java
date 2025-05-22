package org.example;

sealed public interface Result<T> {
    record Ok<T>(T value) implements Result<T> {}
    record Error<T>(Exception exception) implements Result<T> {}
}