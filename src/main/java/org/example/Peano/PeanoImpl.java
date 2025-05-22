package org.example.Peano;

import org.example.Ordering;
import org.example.Peano.Peano.Succ;
import org.example.Peano.Peano.Zero;
import org.example.Result;
import org.example.Result.Err;
import org.example.Result.Ok;

public class PeanoImpl {
    public static Peano fromInt(int n) {
        if (n < 0) throw new IllegalArgumentException("Negative numbers are not allowed");
        if (n == 0) return new Zero();
        return new Succ(fromInt(n - 1));
    }

    public static int toInt(Peano p) {
        return switch (p) {
            case Zero() -> 0;
            case Succ s -> 1 + toInt(s.previous());
        };
    }

    public static Peano add(Peano p1, Peano p2) {
        return switch (p1) {
            case Zero() -> p2;
            case Succ s -> new Succ(add(s.previous(), p2));
        };
    }

    /** When subtracting a larger number from a smaller one, the result will be Zero. */
    public static Peano sub(Peano minuend, Peano subtrahend) {
        return switch (minuend) {
            case Zero z -> z;
            case Succ(Peano minuend_ante) -> switch (subtrahend) {
                case Zero() -> minuend;
                case Succ(Peano subtrahend_ante) -> sub(minuend_ante, subtrahend_ante);
            };
        };
    }

    public static Peano mul(Peano p1, Peano p2) {
        return switch (p1) {
            case Zero() -> new Zero();
            case Succ s -> add(p2, mul(s.previous(), p2));
        };
    }

    public static Ordering compare(Peano p1, Peano p2) {
        return switch (p1) {
            case Zero() when p2 instanceof Zero -> new Ordering.Equal();
            case Zero() -> new Ordering.LessThan();
            case Succ ignored when p2 instanceof Zero -> new Ordering.Greater();
            case Succ x -> compare(x.previous(), ((Succ) p2).previous());
        };
    }

    public static Result<Peano, PeanoError.DivisionByZero> div(Peano dividend, Peano divisor) {
        if (divisor instanceof Zero) {
            return new Err<>(new PeanoError.DivisionByZero());
        }

        if (dividend instanceof Zero) {
            return new Ok<>(new Zero());
        }

        Peano divisions = new Zero();
        Peano remainder = dividend;

        while (!(compare(remainder, divisor) instanceof Ordering.LessThan)) {
            remainder = sub(remainder, divisor);
            divisions = add(divisions, PeanoImpl.fromInt(1));
        }

        return new Ok<>(divisions);
    }
}
