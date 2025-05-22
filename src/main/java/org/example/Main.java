package org.example;

import org.example.Peano.Peano;
import org.example.Peano.PeanoError;
import org.example.Peano.PeanoImpl;
import org.example.Result;
import org.example.Result.Err;
import org.example.Result.Ok;

import static org.example.Peano.PeanoImpl.fromInt;
import static org.example.Peano.PeanoImpl.toInt;
import static org.example.Peano.PeanoImpl.add;

public class Main {
    public static void main(String[] args) {
        final Peano num1 = fromInt(5);
        final Peano num2 = fromInt(3);
        final Peano sum = add(num1, num2);
        System.out.printf("Sum of %s and %s is: %s%n%n", toInt(num1), toInt(num2), toInt(sum));

        final Peano num3 = switch (PeanoImpl.div(num1, num2)) {
            case Err(PeanoError.DivisionByZero err) -> throw new RuntimeException("Boo! Division by zero");
            case Ok(Peano value) -> value;
        };

        // alternative way to force extract the value
        final Peano num4 = PeanoImpl.div(num1, num2).getOrThrow();

        // final var num4 = PeanoImpl.add(num3, num2);
    }
}