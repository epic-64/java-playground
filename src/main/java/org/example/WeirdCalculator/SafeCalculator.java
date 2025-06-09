package org.example.WeirdCalculator;

import org.example.Result;
import org.example.Result.Err;
import org.example.Result.Ok;

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
}
