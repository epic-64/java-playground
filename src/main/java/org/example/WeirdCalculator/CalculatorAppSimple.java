package org.example.WeirdCalculator;

import org.example.Result;
import org.example.Result.Err;
import org.example.Result.Ok;

public class CalculatorAppSimple {
    public static void main(String[] args) {
        SafeCalculator calc = new SafeCalculator();

        final Result<Integer, DivisionError> result = calc.someFunction();

        switch (result) {
            case Ok(Integer value) -> System.out.println("Result: " + value);
            case Err(DivisionError error) -> System.out.println(error.toMessage());
        }
    }
}
