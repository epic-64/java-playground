package org.example.Peano;

public sealed interface PeanoError {
    record CannotSubtractPositiveFromZero() implements PeanoError {}
    record DivisionByZero() implements PeanoError {}
}
