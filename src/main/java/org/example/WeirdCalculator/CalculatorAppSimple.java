package org.example.WeirdCalculator;

import org.example.Result;
import org.example.Result.Err;
import org.example.Result.Ok;

import java.util.Scanner;

import static java.lang.System.out;


public class CalculatorAppSimple {
    public static void main(String[] args) {
        final Demo demo = new Demo(new SafeCalculator(), new Scanner(System.in));

        do {
            demo.demo();

            if (!demo.tryAgain()) {
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

    public void demo() {
        final Result<Integer, CalculatorError> result1 = performDivision();

        final String output = switch (result1) {
            case Ok(Integer value) -> "Result: " + value;
            case Err(CalculatorError e) -> "Calculator error: " + e.toMessage();
        };

        out.println(output);
    }

    public boolean tryAgain() {
        out.println("Would you like to try again? (y/n)");
        final String choice = new java.util.Scanner(System.in).nextLine().trim().toLowerCase();

        return choice.equals("y");
    }

    public Result<Integer, CalculatorError> performDivision()
    {
        final Integer a;
        final Integer b;
        final Integer c;

        System.out.println("Let's divide two numbers!");

        System.out.println("Enter first number:");
        switch (calc.parseInt(scanner.nextLine())) {
            case Ok(Integer value) -> a = value;
            case Err(ParseIntError e) -> { return new Err<>(CalculatorError.from(e)); }
        }

        System.out.println("Enter second number:");
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