package org.example.Peano;

import org.example.Ordering;
import org.example.Result;

public sealed interface Peano {
    record Zero() implements Peano {}
    record Succ(Peano previous) implements Peano {}

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
}
