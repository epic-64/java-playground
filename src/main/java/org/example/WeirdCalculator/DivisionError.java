package org.example.WeirdCalculator;

public sealed interface DivisionError {
    record DivisionByZero() implements DivisionError {}
    record BannedDivisor(int divisor) implements DivisionError {}
}
