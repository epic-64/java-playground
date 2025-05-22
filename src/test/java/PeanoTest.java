import org.example.Peano.Peano;
import org.example.Peano.PeanoImpl;
import org.example.Result;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PeanoTest {
    void assertSameResult(Result<Peano> result1, Result<Peano> result2) {
        switch (result1) {
            case Result.Ok<Peano> ok1 -> {
                if (result2 instanceof Result.Ok<Peano> ok2) {
                    assertEquals(ok1.getValue(), ok2.getValue());
                } else {
                    throw new AssertionError("Results are not equal");
                }
            }
            case Result.Error<Peano> error1 -> {
                if (result2 instanceof Result.Error<Peano> error2) {
                    assertEquals(error1.getError(), error2.getError());
                } else {
                    throw new AssertionError("Results are not equal");
                }
            }
        }
    }

    @Test
    void testFromInt() {
        record TestCase(int input, Peano expected) {}

        final TestCase[] testCases = {
                new TestCase(0, new Peano.Zero()),
                new TestCase(1, new Peano.Succ(new Peano.Zero())),
                new TestCase(2, new Peano.Succ(new Peano.Succ(new Peano.Zero()))),
                new TestCase(3, new Peano.Succ(new Peano.Succ(new Peano.Succ(new Peano.Zero())))),
        };

        for (TestCase testCase : testCases) {
            Peano result = PeanoImpl.fromInt(testCase.input);
            assertEquals(result, testCase.expected);
        }
    }

    @Test
    void testToInt() {
        record TestCase(Peano input, int expected) {}

        final TestCase[] testCases = {
                new TestCase(new Peano.Zero(), 0),
                new TestCase(new Peano.Succ(new Peano.Zero()), 1),
                new TestCase(new Peano.Succ(new Peano.Succ(new Peano.Zero())), 2),
                new TestCase(new Peano.Succ(new Peano.Succ(new Peano.Succ(new Peano.Zero()))), 3),
        };

        for (TestCase testCase : testCases) {
            int result = PeanoImpl.toInt(testCase.input);
            assertEquals(result, testCase.expected);
        }
    }

    @Test
    void testAdd() {
        record TestCase(Peano p1, Peano p2, Peano expectedResult) {}

        final TestCase[] testCases = {
                new TestCase(PeanoImpl.fromInt(0), PeanoImpl.fromInt(0), PeanoImpl.fromInt(0)),
                new TestCase(PeanoImpl.fromInt(0), PeanoImpl.fromInt(1), PeanoImpl.fromInt(1)),
                new TestCase(PeanoImpl.fromInt(1), PeanoImpl.fromInt(0), PeanoImpl.fromInt(1)),
                new TestCase(PeanoImpl.fromInt(1), PeanoImpl.fromInt(1), PeanoImpl.fromInt(2)),
                new TestCase(PeanoImpl.fromInt(2), PeanoImpl.fromInt(3), PeanoImpl.fromInt(5)),
                new TestCase(PeanoImpl.fromInt(3), PeanoImpl.fromInt(2), PeanoImpl.fromInt(5)),
        };

        for (TestCase testCase : testCases) {
            final Peano result = PeanoImpl.add(testCase.p1, testCase.p2);
            assertEquals(result, testCase.expectedResult);
        }
    }

    @Test
    void testMultiplication() {
        record TestCase(Peano p1, Peano p2, Peano expectedResult) {}

        final TestCase[] testCases = {
                new TestCase(PeanoImpl.fromInt(0), PeanoImpl.fromInt(0), PeanoImpl.fromInt(0)),
                new TestCase(PeanoImpl.fromInt(0), PeanoImpl.fromInt(1), PeanoImpl.fromInt(0)),
                new TestCase(PeanoImpl.fromInt(1), PeanoImpl.fromInt(0), PeanoImpl.fromInt(0)),
                new TestCase(PeanoImpl.fromInt(1), PeanoImpl.fromInt(1), PeanoImpl.fromInt(1)),
                new TestCase(PeanoImpl.fromInt(2), PeanoImpl.fromInt(3), PeanoImpl.fromInt(6)),
                new TestCase(PeanoImpl.fromInt(3), PeanoImpl.fromInt(2), PeanoImpl.fromInt(6)),
                // new TestCase(PeanoImpl.fromInt(100), PeanoImpl.fromInt(100), PeanoImpl.fromInt(10_000)),
        };

        for (TestCase testCase : testCases) {
            final Peano result = PeanoImpl.mul(testCase.p1, testCase.p2);
            assertEquals(result, testCase.expectedResult);
        }
    }

    @Test
    void testSubtraction() {
        record TestCase(Peano p1, Peano p2, Result<Peano> expectedResult) {}

        final TestCase[] testCases = {
                new TestCase(PeanoImpl.fromInt(0), PeanoImpl.fromInt(0), new Result.Ok<>(PeanoImpl.fromInt(0))),
                new TestCase(PeanoImpl.fromInt(0), PeanoImpl.fromInt(1), new Result.Error<>(new IllegalArgumentException("Cannot subtract a positive number from zero"))),
                new TestCase(PeanoImpl.fromInt(1), PeanoImpl.fromInt(0), new Result.Ok<>(PeanoImpl.fromInt(1))),
                new TestCase(PeanoImpl.fromInt(1), PeanoImpl.fromInt(1), new Result.Ok<>(PeanoImpl.fromInt(0))),
                new TestCase(PeanoImpl.fromInt(2), PeanoImpl.fromInt(1), new Result.Ok<>(PeanoImpl.fromInt(1))),
                new TestCase(PeanoImpl.fromInt(2), PeanoImpl.fromInt(2), new Result.Ok<>(PeanoImpl.fromInt(0))),
        };

        for (TestCase testCase : testCases) {
            final Result<Peano> result = PeanoImpl.sub(testCase.p1, testCase.p2);
            assertSameResult(result, testCase.expectedResult);
        }
    }
}
