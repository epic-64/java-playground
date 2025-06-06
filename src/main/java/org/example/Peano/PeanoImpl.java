package org.example.Peano;

import org.example.Ordering;
import org.example.Peano.Peano.Succ;
import org.example.Peano.Peano.Zero;
import org.example.Result;
import org.example.Result.Err;
import org.example.Result.Ok;

public class PeanoImpl {
    public static Peano fromInt(int n) {
        if (n == 0) return new Zero();

        return new Succ(fromInt(n - 1));
    }

    public static Result<Peano, PeanoConstructionError> fromIntSafe(int n) {
        if (n < 0) return new Err<>(new PeanoConstructionError.Negative(n));
        if (n > 100) return new Err<>(new PeanoConstructionError.TooLarge(n));
        if (n == 0) return new Ok<>(new Zero());

        Peano intermediate = fromIntSafe(n - 1).getOrThrow("it never throws, trust me");
        return new Ok<>(new Succ(intermediate));
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
            case Succ(Peano previous) -> new Succ(add(previous, p2));
        };
    }

    /** When subtracting a larger number from a smaller one, the result will be Zero. */
    public static Peano sub(Peano minuend, Peano subtrahend) {
        return switch (minuend) {
            case Zero z -> z;
            case Succ(Peano minuend_previous) -> switch (subtrahend) {
                case Zero() -> minuend;
                case Succ(Peano subtrahend_previous) -> sub(minuend_previous, subtrahend_previous);
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
            case Succ(Peano previous) -> compare(previous, ((Succ) p2).previous());
        };
    }

    public static Result<Peano, PeanoDivisionError> div(Peano dividend, Peano divisor) {
        if (divisor instanceof Zero) {
            return new Err<>(new PeanoDivisionError.DivisionByZero());
        }

        if (dividend instanceof Zero) {
            return new Ok<>(new Zero());
        }

        Peano divisions = new Zero();
        Peano remainder = dividend;

        while (!(remainder.compare(divisor) instanceof Ordering.LessThan)) {
            remainder = remainder.sub(divisor);
            divisions = divisions.add(Peano.one());
        }

        return new Ok<>(divisions);
    }
}
