package org.example.Peano;

public sealed interface PeanoError {
    record DivisionByZero() implements PeanoError {}
}
