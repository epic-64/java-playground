package org.example.Peano;

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

    public static Result<Peano, PeanoError> sub(Peano p1, Peano p2) {
        return switch (p1) {
            case Zero ignored1 -> switch (p2) {
                case Zero ignored -> new Ok<>(new Zero());
                case Succ ignored -> new Error<>(new PeanoError.CannotSubtractPositiveFromZero());
            };
            case Succ s -> switch (p2) {
                case Zero ignored -> new Ok<>(p1);
                case Succ s2 -> sub(s.previous(), s2.previous()); // StackOverflow!
            };
        };
    }

    public static Peano mul(Peano p1, Peano p2) {
        return switch (p1) {
            case Zero z -> new Zero();
            case Succ s -> add(p2, mul(s.previous(), p2)); // StackOverflow!
        };
    }

    public static Result<Peano, PeanoError> div(Peano p1, Peano p2) {
        if (p2 instanceof Zero) {
            return new Error<>(new PeanoError.DivisionByZero());
        }

        if (p1 instanceof Zero) {
            return new Ok<>(new Zero());
        }

        Peano acc = new Zero();
        Peano current = p1;

        while (current instanceof Succ) {
            current = ((Succ) current).previous();
            acc = add(acc, new Succ(new Zero()));
        }
        return new Ok<>(acc);
    }
}
