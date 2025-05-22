package org.example.Peano;

public sealed interface PeanoError {
    record NegativeNumber() implements PeanoError {}
    record CannotSubtractPositiveFromZero() implements PeanoError {}
}
