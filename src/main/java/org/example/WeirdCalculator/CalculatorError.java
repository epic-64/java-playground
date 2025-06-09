package org.example.WeirdCalculator;

public sealed interface CalculatorError {
    record Division(DivisionError divisionError) implements CalculatorError {}
    record ParseInt(ParseIntError parseIntError) implements CalculatorError {}

    static CalculatorError from(DivisionError divisionError) {
        return new Division(divisionError);
    }

    static CalculatorError from(ParseIntError parseIntError) {
        return new ParseInt(parseIntError);
    }

    default String toMessage() {
        return switch (this) {
            case Division(DivisionError divisionError) -> divisionError.toMessage();
            case ParseInt(ParseIntError parseIntError) -> parseIntError.toString();
        };
    }
}
