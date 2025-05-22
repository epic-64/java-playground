package org.example;

import org.example.Peano.Peano;
import org.example.Peano.PeanoError;
import org.example.Peano.PeanoImpl;
import org.example.Result.Err;
import org.example.Result.Ok;

public class Main {
    public static void main(String[] args) {
        final Peano num1 = PeanoImpl.fromInt(5);
        final Peano num2 = PeanoImpl.fromInt(3);

        final Peano num3 = switch (PeanoImpl.div(num1, num2)) {
            case Ok<Peano, PeanoError>(Peano peano) -> peano;
            case Err<Peano, PeanoError>(PeanoError error) -> throw new IllegalArgumentException(error.toString());
        };

        final Peano num4 = PeanoImpl.add(num3, PeanoImpl.fromInt(2));

        System.out.println("Result of addition: " + PeanoImpl.toInt(num4));
    }
}