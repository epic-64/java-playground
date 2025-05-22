package org.example.Peano;

import org.example.Result;

public class PeanoImpl {
    public static Peano fromInt(int n) {
        if (n < 0) throw new IllegalArgumentException("Negative numbers are not allowed");
        if (n == 0) return new Peano.Zero();
        return new Peano.Succ(fromInt(n - 1)); // StackOverflow!
    }

    public static int toInt(Peano p) {
        return switch (p) {
            case Peano.Zero z -> 0;
            case Peano.Succ s -> 1 + toInt(s.previous()); // StackOverflow!
        };
    }

    public static Peano add(Peano p1, Peano p2) {
        return switch (p1) {
            case Peano.Zero z -> p2;
            case Peano.Succ s -> new Peano.Succ(add(s.previous(), p2)); // StackOverflow!
        };
    }

    public static Result<Peano> sub(Peano p1, Peano p2) {
        return switch (p1) {
            case Peano.Zero ignored1 -> switch (p2) {
                case Peano.Zero ignored -> new Result.Ok<>(new Peano.Zero());
                case Peano.Succ ignored -> new Result.Error<>(new IllegalArgumentException("Cannot subtract a positive number from zero"));
            };
            case Peano.Succ s -> switch (p2) {
                case Peano.Zero ignored -> new Result.Ok<>(p1);
                case Peano.Succ s2 -> sub(s.previous(), s2.previous()); // StackOverflow!
            };
        };
    }

    public static Peano mul(Peano p1, Peano p2) {
        return switch (p1) {
            case Peano.Zero z -> new Peano.Zero();
            case Peano.Succ s -> add(p2, mul(s.previous(), p2)); // StackOverflow!
        };
    }
}
