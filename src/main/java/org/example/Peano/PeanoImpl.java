package org.example.Peano;

import org.example.Ordering;
import org.example.Peano.Peano.Succ;
import org.example.Peano.Peano.Zero;
import org.example.Result;
import org.example.Result.Ok;
import org.example.Result.Error;

public class PeanoImpl {
    public static Peano fromInt(int n) {
        if (n < 0) throw new IllegalArgumentException("Negative numbers are not allowed");
        if (n == 0) return new Zero();
        return new Succ(fromInt(n - 1)); // StackOverflow!
    }

    public static int toInt(Peano p) {
        return switch (p) {
            case Zero z -> 0;
            case Succ s -> 1 + toInt(s.previous()); // StackOverflow!
        };
    }

    public static Peano add(Peano p1, Peano p2) {
        return switch (p1) {
            case Zero z -> p2;
            case Succ s -> new Succ(add(s.previous(), p2)); // StackOverflow!
        };
    }

    public static Peano sub(Peano p1, Peano p2) {
        return switch (p1) {
            case Zero z -> z;
            case Succ(Peano previous) -> switch (p2) {
                case Zero ignored -> p1;
                case Succ s2 -> sub(previous, s2.previous()); // StackOverflow!
            };
        };
    }

    public static Peano mul(Peano p1, Peano p2) {
        return switch (p1) {
            case Zero z -> new Zero();
            case Succ s -> add(p2, mul(s.previous(), p2)); // StackOverflow!
        };
    }

    public static Ordering compare(Peano p1, Peano p2) {
        if (p1 instanceof Zero && p2 instanceof Zero) {
            return new Ordering.Equal();
        } else if (p1 instanceof Zero) {
            return new Ordering.LessThan();
        } else if (p2 instanceof Zero) {
            return new Ordering.Greater();
        } else {
            return compare(((Succ) p1).previous(), ((Succ) p2).previous());
        }
    }

    public static Result<Peano, PeanoError> div(Peano dividend, Peano divisor) {
        if (divisor instanceof Zero) {
            return new Error<>(new PeanoError.DivisionByZero());
        }

        if (dividend instanceof Zero) {
            return new Ok<>(new Zero());
        }

        Peano divisions = new Zero();
        Peano remainder = dividend;

        while (true) {
            // subtract p2 from our number and extract the value
            remainder = sub(remainder, divisor);

            // add 1 to our division counter every time we can subtract p2
            divisions = add(divisions, new Succ(new Zero()));

            if (compare(remainder, divisor) instanceof Ordering.LessThan) {
                break;
            }
        }

        return new Ok<>(divisions);
    }
}
