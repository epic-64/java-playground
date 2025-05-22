package org.example.Peano;

public sealed interface PeanoDivisionError {
    record DivisionByZero() implements PeanoDivisionError {}
}
