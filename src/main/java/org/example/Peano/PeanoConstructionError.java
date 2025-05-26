package org.example.Peano;

public sealed interface PeanoConstructionError {
    record Negative(int n) implements PeanoConstructionError {}
    record TooLarge(int n) implements PeanoConstructionError {}
}