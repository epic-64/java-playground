package org.example.Peano;

public sealed interface Peano {
    record Zero() implements Peano {}
    record Succ(Peano previous) implements Peano {}
}
