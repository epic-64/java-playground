package org.example.WeirdCalculator;

import org.example.Result;
import org.example.Result.Err;
import org.example.Result.Ok;

import static java.lang.System.out;


public class CalculatorAppSimple {
    public static void main(String[] args) {
        final SafeCalculator calc = new SafeCalculator();

        do {
            final Result<Integer, CalculatorError> result = calc.divDemo();

            final String output = switch (result) {
                case Ok(Integer value) -> "Result: " + value;
                case Err(CalculatorError e) -> "Calculator error: " + e.toMessage();
            };

            out.println(output);

            out.println("Wanna try again? (y/n)");
            final String choice = new java.util.Scanner(System.in).nextLine().trim().toLowerCase();
            if (!choice.equals("y")) {
                out.println("Goodbye!");
                break;
            }
        } while (true);
    }
}
