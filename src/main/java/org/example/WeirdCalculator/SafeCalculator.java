package org.example.WeirdCalculator;

import org.example.Result;
import org.example.Result.Err;
import org.example.Result.Ok;

import java.util.Scanner;

public class SafeCalculator {
    public Result<Integer, ParseIntError> parseInt(String input) {
        try {
            return new Ok<>(Integer.parseInt(input));
        } catch (NumberFormatException e) {
            return new Err<>(new ParseIntError.InvalidNumberFormat(input));
        }
    }

    public Result<Integer, DivisionError> div(Integer dividend, Integer divisor) {
        if (divisor == 0) {
            return new Err<>(new DivisionError.DivisionByZero());
        }

        var bannedDivisors = new int[]{13, 17, 19};
        for (int banned : bannedDivisors) {
            if (divisor == banned) {
                return new Err<>(new DivisionError.BannedDivisor(divisor));
            }
        }

        return new Ok<>(dividend / divisor);
    }

    public Integer add(Integer a, Integer b) {
        return a + b;
    }

    public Result<Integer, CalculatorError> divDemo()
    {
        final Scanner scanner = new Scanner(System.in);
        final Integer a;
        final Integer b;
        final Integer c;

        System.out.println("Let's divide two numbers!");

        System.out.println("Enter first number:");
        switch (parseInt(scanner.nextLine())) {
            case Ok(Integer value) -> a = value;
            case Err(ParseIntError e) -> { return new Err<>(CalculatorError.from(e)); }
        }

        System.out.println("Enter second number:");
        switch (parseInt(scanner.nextLine())) {
            case Ok(Integer value) -> b = value;
            case Err(ParseIntError e) -> { return new Err<>(CalculatorError.from(e)); }
        }

        switch (div(a, b)) {
            case Ok(Integer value) -> c = value;
            case Err(DivisionError e) -> { return new Err<>(CalculatorError.from(e)); }
        }

        return new Ok<>(c);
    }
}
