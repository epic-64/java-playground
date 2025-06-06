package org.example;

import org.example.Peano.Peano;
import org.example.Peano.Peano.Zero;
import org.example.Peano.PeanoDivisionError;
import org.example.Peano.PeanoConstructionError;
import org.example.Peano.PeanoImpl;
import org.example.Result.Err;
import org.example.Result.Ok;

public class Main {
    public static void main(String[] args) {
        final Peano num1 = Peano.fromInt(5);
        final Peano num2 = Peano.fromInt(3);
        final Peano sum = num1.add(num2);
        System.out.printf("Sum of %s and %s is: %s%n%n", num1.toInt(), num2.toInt(), sum.toInt());

        final Peano num0 = Peano.fromIntSafe(101).getOrThrow("It's the user's fault, I don't care!!!");

        // Explicit error handling. Compiler forces you to handle all possible cases.
        final Peano num7 = switch(Peano.fromIntSafe(7)) {
            case Err(PeanoConstructionError.Negative err) -> throw new RuntimeException("Boo! Negative number");
            case Err(PeanoConstructionError.TooLarge err) -> throw new RuntimeException("Boo! Number too large");
            case Ok(Peano value) -> value;
        };

        // Force extract the value. Will blow up with less control over the error state.
        final Peano num8 = Peano.fromIntSafe(8).getOrThrow("Yolo");

        // Using a default value instead. Should almost *never* be used because it may create new bugs.
        final Peano num9 = Peano.fromIntSafe(9).getOrElse(new Zero());

        final Peano num3 = switch (PeanoImpl.div(num1, num2)) {
            case Err(PeanoDivisionError.DivisionByZero err) -> throw new RuntimeException("Boo! Division by zero");
            case Ok(Peano value) -> value;
        };

        // alternative way to force extract the value
        final Peano num4 = PeanoImpl.div(num1, num2).getOrThrow("I don't care!!!");

        // final var num4 = PeanoImpl.add(num3, num2);
    }
}