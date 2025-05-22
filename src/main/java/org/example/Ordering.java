package org.example;

public sealed interface Ordering {
    record LessThan() implements Ordering {}
    record Equal() implements Ordering {}
    record Greater() implements Ordering {}
}
