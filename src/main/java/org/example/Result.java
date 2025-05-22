package org.example;

sealed public interface Result<T> {
    boolean isError();
    boolean isOk();

    Exception getError();
    T getValue() throws Exception;

    record Ok<T>(T t) implements Result<T> {
        @Override
        public boolean isError() {
            return false;
        }

        @Override
        public boolean isOk() {
            return false;
        }

        @Override
        public Exception getError() {
            return null;
        }

        @Override
        public T getValue() {
            return null;
        }
    }
    record Error<T>(Exception e) implements Result<T> {
        @Override
        public boolean isError() {
            return false;
        }

        @Override
        public boolean isOk() {
            return false;
        }

        @Override
        public Exception getError() {
            return e;
        }

        @Override
        public T getValue() throws Exception {
            throw e;
        }
    }
}