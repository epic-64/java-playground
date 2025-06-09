package org.example.WeirdCalculator;

import org.example.Result;
import org.example.Result.Err;
import org.example.Result.Ok;

import java.util.Scanner;

import static java.lang.System.in;
import static java.lang.System.out;

public class CalculatorAppSimple {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(in);
        final Demo demo = new Demo(new SafeCalculator(), scanner);

        do {
            final Result<Integer, CalculatorError> result1 = demo.performDivision();

            out.println(switch (result1) {
                case Ok(Integer value) -> "Result: " + value;
                case Err(CalculatorError e) -> "Error: " + e.toMessage();
            });

            out.println("Would you like to try again? (y/n)");
            if (!scanner.nextLine().trim().equalsIgnoreCase("y")) {
                out.println("Goodbye!");
                break;
            }
        } while (true);
    }
}

class Demo {
    private final SafeCalculator calc;
    private final Scanner scanner;

    public Demo (SafeCalculator calc, Scanner scanner) {
        this.calc = calc;
        this.scanner = scanner;
    }

    public Result<Integer, CalculatorError> performDivision()
    {
        final Integer a;
        final Integer b;
        final Integer c;

        out.println("Let's divide two numbers!");

        out.println("Enter first number:");
        switch (calc.parseInt(scanner.nextLine())) {
            case Ok(Integer value) -> a = value;
            case Err(ParseIntError e) -> { return new Err<>(CalculatorError.from(e)); }
        }

        out.println("Enter second number:");
        switch (calc.parseInt(scanner.nextLine())) {
            case Ok(Integer value) -> b = value;
            case Err(ParseIntError e) -> { return new Err<>(CalculatorError.from(e)); }
        }

        switch (calc.div(a, b)) {
            case Ok(Integer value) -> c = value;
            case Err(DivisionError e) -> { return new Err<>(CalculatorError.from(e)); }
        }

        return new Ok<>(c);
    }
}