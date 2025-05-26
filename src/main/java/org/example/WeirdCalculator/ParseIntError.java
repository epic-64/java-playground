package org.example.WeirdCalculator;

sealed public interface ParseIntError permits ParseIntError.InvalidNumberFormat {

    record InvalidNumberFormat(String input) implements ParseIntError {
        @Override
        public String toString() {
            return "Invalid number format: '" + input + "'";
        }
    }

}
