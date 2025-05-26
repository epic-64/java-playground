package org.example.Peano;

import org.example.Ordering;
import org.example.Result;

public sealed interface Peano {
    record Zero() implements Peano {}
    record Succ(Peano previous) implements Peano {}

    static Peano one() {
        return new Succ(new Zero());
    }

    static Peano fromInt(int n) {
        return PeanoImpl.fromInt(n);
    }

    static Result<Peano, PeanoConstructionError> fromIntSafe(int n) {
        return PeanoImpl.fromIntSafe(n);
    }

    default int toInt() {
        return PeanoImpl.toInt(this);
    }

    default Peano add(Peano other) {
        return PeanoImpl.add(this, other);
    }

    default Peano sub(Peano other) {
        return PeanoImpl.sub(this, other);
    }

    default Peano mul(Peano other) {
        return PeanoImpl.mul(this, other);
    }

    default Result<Peano, PeanoDivisionError> div(Peano other) {
        return PeanoImpl.div(this, other);
    }

    default Ordering compare(Peano other) {
        return PeanoImpl.compare(this, other);
    }

    default Succ asSucc() throws IllegalArgumentException {
        return switch (this) {
            case Zero z -> throw new IllegalArgumentException("Expected Peano.Succ, but got Peano.Zero: " + z);
            case Succ s -> s;
        };
    }

    default Zero asZero() throws IllegalArgumentException {
        return switch (this) {
            case Zero z -> z;
            case Succ s -> throw new IllegalArgumentException("Expected Peano.Zero, but got Peano.Succ: " + s);
        };
    }
}
