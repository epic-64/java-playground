import org.example.Ordering;
import org.example.Peano.Peano;
import org.example.Peano.Peano.Succ;
import org.example.Peano.Peano.Zero;
import org.example.Peano.PeanoDivisionError;
import org.example.Peano.PeanoConstructionError;
import org.example.Peano.PeanoImpl;
import org.example.Result;
import org.example.Result.Ok;
import org.example.Result.Err;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class PeanoTest {
    @Test
    void testFromInt() {
        record TestData(int input, Peano expected) {}

        final TestData[] testCases = {
                new TestData(0, new Zero()),
                new TestData(1, new Succ(new Zero())),
                new TestData(2, new Succ(new Succ(new Zero()))),
                new TestData(3, new Succ(new Succ(new Succ(new Zero())))),
                new TestData(100, new Succ(Peano.fromInt(99))),
        };

        for (TestData t : testCases) {
            Peano result = Peano.fromInt(t.input);
            assertEquals(result, t.expected, "Failed for %d".formatted(t.input));
        }
    }

    @Test
    void testFromIntSafe() {
        record TestData(int input, Result<Peano, PeanoConstructionError> expected) {}

        final TestData[] testCases = {
                new TestData(0, new Ok<>(new Zero())),
                new TestData(1, new Ok<>(new Succ(new Zero()))),
                new TestData(2, new Ok<>(new Succ(new Succ(new Zero())))),
                new TestData(3, new Ok<>(new Succ(new Succ(new Succ(new Zero()))))),
                new TestData(100, new Ok<>(new Succ(Peano.fromInt(99)))),
                new TestData(-1, new Err<>(new PeanoConstructionError.Negative(-1))),
                new TestData(101, new Err<>(new PeanoConstructionError.TooLarge(101))),
        };

        for (TestData t : testCases) {
            Result<Peano, PeanoConstructionError> result = Peano.fromIntSafe(t.input);
            assertEquals(result, t.expected, "Failed for %d".formatted(t.input));
        }
    }

    @Test
    void testToInt() {
        record TestData(Peano input, int expected) {}

        final TestData[] testCases = {
                new TestData(new Zero(), 0),
                new TestData(new Succ(new Zero()), 1),
                new TestData(new Succ(new Succ(new Zero())), 2),
                new TestData(new Succ(new Succ(new Succ(new Zero()))), 3),
                new TestData(new Succ(Peano.fromInt(99)), 100),
        };

        for (TestData t : testCases) {
            int result = t.input.toInt();
            assertEquals(result, t.expected, "Failed for %s".formatted(t.input));
        }
    }

    @Test
    void testAddition() {
        record TestData(Peano p1, Peano p2, Peano expectedResult) {}

        final TestData[] testCases = {
                new TestData(Peano.fromInt(0), Peano.fromInt(0), Peano.fromInt(0)),
                new TestData(Peano.fromInt(0), Peano.fromInt(1), Peano.fromInt(1)),
                new TestData(Peano.fromInt(1), Peano.fromInt(0), Peano.fromInt(1)),
                new TestData(Peano.fromInt(1), Peano.fromInt(1), Peano.fromInt(2)),
                new TestData(Peano.fromInt(2), Peano.fromInt(3), Peano.fromInt(5)),
                new TestData(Peano.fromInt(3), Peano.fromInt(2), Peano.fromInt(5)),
        };

        for (TestData t : testCases) {
            final Peano result = PeanoImpl.add(t.p1, t.p2);
            assertEquals(result, t.expectedResult, "Failed for %s + %s".formatted(t.p1, t.p2));
        }
    }

    @Test
    void testMultiplication() {
        record TestData(Peano p1, Peano p2, Peano expectedResult) {}

        final TestData[] testCases = {
                new TestData(Peano.fromInt(0), Peano.fromInt(0), Peano.fromInt(0)),
                new TestData(Peano.fromInt(0), Peano.fromInt(1), Peano.fromInt(0)),
                new TestData(Peano.fromInt(1), Peano.fromInt(0), Peano.fromInt(0)),
                new TestData(Peano.fromInt(1), Peano.fromInt(1), Peano.fromInt(1)),
                new TestData(Peano.fromInt(2), Peano.fromInt(3), Peano.fromInt(6)),
                new TestData(Peano.fromInt(3), Peano.fromInt(2), Peano.fromInt(6)),
        };

        for (TestData t : testCases) {
            final Peano result = PeanoImpl.mul(t.p1, t.p2);
            assertEquals(result, t.expectedResult, "Failed for %s * %s".formatted(t.p1, t.p2));
        }
    }

    @Test
    void testSubtraction() {
        record TestData(Peano p1, Peano p2, Peano expectedResult) {}

        final TestData[] testCases = {
                new TestData(Peano.fromInt(0), Peano.fromInt(1), Peano.fromInt(0)),
                new TestData(Peano.fromInt(1), Peano.fromInt(2), Peano.fromInt(0)),
                new TestData(Peano.fromInt(0), Peano.fromInt(0), Peano.fromInt(0)),
                new TestData(Peano.fromInt(1), Peano.fromInt(0), Peano.fromInt(1)),
                new TestData(Peano.fromInt(2), Peano.fromInt(1), Peano.fromInt(1)),
                new TestData(Peano.fromInt(3), Peano.fromInt(2), Peano.fromInt(1)),
        };

        for (TestData t : testCases) {
            final Peano result = PeanoImpl.sub(t.p1, t.p2);
            assertEquals(t.expectedResult, result, "Failed for %s - %s".formatted(t.p1, t.p2));
        }
    }

    @Test
    void testDivision() {
        record TestData(Peano p1, Peano p2, Result<Peano, PeanoDivisionError> expectedResult) {}

        final TestData[] testCases = {
                // division by 0
                new TestData(Peano.fromInt(0), Peano.fromInt(0), new Err<>(new PeanoDivisionError.DivisionByZero())),
                new TestData(Peano.fromInt(1), Peano.fromInt(0), new Err<>(new PeanoDivisionError.DivisionByZero())),

                // division by 1
                new TestData(Peano.fromInt(0), Peano.fromInt(1), new Ok<>(Peano.fromInt(0))),
                new TestData(Peano.fromInt(1), Peano.fromInt(1), new Ok<>(Peano.fromInt(1))),
                new TestData(Peano.fromInt(2), Peano.fromInt(1), new Ok<>(Peano.fromInt(2))),
                new TestData(Peano.fromInt(3), Peano.fromInt(1), new Ok<>(Peano.fromInt(3))),

                // division by 2, even and odd
                new TestData(Peano.fromInt(4), Peano.fromInt(2), new Ok<>(Peano.fromInt(2))),
                new TestData(Peano.fromInt(9), Peano.fromInt(4), new Ok<>(Peano.fromInt(2))),

                // divisor larger than dividend
                new TestData(Peano.fromInt(1), Peano.fromInt(2), new Ok<>(Peano.fromInt(0))),
                new TestData(Peano.fromInt(4), Peano.fromInt(5), new Ok<>(Peano.fromInt(0))),
        };

        for (TestData t : testCases) {
            final Result<Peano, PeanoDivisionError> result = PeanoImpl.div(t.p1, t.p2);
            assertEquals(t.expectedResult, result, "Failed for %s / %s".formatted(t.p1, t.p2));
        }
    }

    @Test
    void testComparison() {
        record TestData(Peano p1, Peano p2, Ordering expectedResult) {}

        final TestData[] testCases = {
                new TestData(Peano.fromInt(0), Peano.fromInt(0), new Ordering.Equal()),
                new TestData(Peano.fromInt(0), Peano.fromInt(1), new Ordering.LessThan()),
                new TestData(Peano.fromInt(1), Peano.fromInt(0), new Ordering.Greater()),
                new TestData(Peano.fromInt(1), Peano.fromInt(1), new Ordering.Equal()),
                new TestData(Peano.fromInt(2), Peano.fromInt(3), new Ordering.LessThan()),
                new TestData(Peano.fromInt(3), Peano.fromInt(2), new Ordering.Greater()),
        };

        for (TestData t : testCases) {
            final Ordering result = t.p1.compare(t.p2);
            assertEquals(t.expectedResult, result, "Failed for %s <=> %s".formatted(t.p1, t.p2));
        }
    }

    @Test
    void testMethodChaining() {
        final Peano one = Peano.fromInt(1);
        final Peano two = Peano.fromInt(2);
        final Peano three = Peano.fromInt(3);

        final int result =
            Peano.fromIntSafe(5).getOrThrow("dont care") // 5
            .add(one)                                    // 6
            .add(one)                                    // 7
            .div(two).getOrThrow("dont care")            // 3
            .mul(three)                                  // 9
            .sub(one)                                    // 8
            .toInt()
        ;

        assertEquals(8, result);
    }

    @Test
    void testDivideThenSum() {
        Result<Peano, PeanoDivisionError> divResult = PeanoImpl.div(Peano.fromInt(9), Peano.fromInt(2));

        final Peano divided = switch (divResult) {
            case Err(PeanoDivisionError.DivisionByZero error) -> fail("Unexpected error type: " + error);
            case Ok(Peano peano) -> peano;
        };
        assertEquals(4, divided.toInt());

        final Peano result = PeanoImpl.add(divided, Peano.fromInt(2));
        assertEquals(6, result.toInt());
    }
}
