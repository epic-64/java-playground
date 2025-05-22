package org.example.Peano;

public sealed interface PeanoFromIntError {
    record Negative(int n) implements PeanoFromIntError {}
    record TooLarge(int n) implements PeanoFromIntError {}
}
