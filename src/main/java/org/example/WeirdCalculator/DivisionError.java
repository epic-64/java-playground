package org.example.WeirdCalculator;

public sealed interface DivisionError {
    record DivisionByZero() implements DivisionError {}
    record BannedDivisor(int divisor) implements DivisionError {}

    default String toMessage() {
        return switch (this) {
            case DivisionByZero() -> "Division by zero is not allowed.";
            case BannedDivisor(int divisor) -> "Division by banned divisor: " + divisor;
        };
    }
}
