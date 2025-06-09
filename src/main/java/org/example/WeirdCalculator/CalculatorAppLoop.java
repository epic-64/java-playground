package org.example.WeirdCalculator;

import org.example.Result;

import java.util.Scanner;

public class CalculatorAppLoop {
    public static void main(String[] args) {
        SafeCalculator calc = new SafeCalculator();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Menu:");
            System.out.println("[1] Divide two numbers");
            System.out.println("[q] quit");
            System.out.println("---------------------");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine();
            if (choice.equals("q") || choice.isBlank()) {
                System.out.println("Exiting...");
                break;
            }

            if (choice.equals("1")) {
                System.out.print("Enter the first number (dividend): ");
                String inputDividend = scanner.nextLine();

                Result<Integer, ParseIntError> parsedDividend = calc.parseInt(inputDividend);
                if (parsedDividend.isErr()) {
                    System.out.println("Error: " + parsedDividend.err().toString());
                    continue;
                }

                Integer dividend = parsedDividend.ok();

                System.out.print("Enter the second number (divisor): ");
                String inputDivisor = scanner.nextLine();

                if (inputDivisor.equals("exit") || inputDivisor.isBlank()) {
                    System.out.println("Exiting...");
                    break;
                }

                Result<Integer, ParseIntError> parsedDivisor = calc.parseInt(inputDivisor);
                if (parsedDivisor.isErr()) {
                    System.out.println("Error: " + parsedDivisor.err().toString());
                    continue;
                }

                Integer divisor = parsedDivisor.ok();

                Result<Integer, DivisionError> divisionResult = calc.div(dividend, divisor);
                if (divisionResult.isErr()) {
                    System.out.println("Error: " + divisionResult.err().toString());
                    continue;
                }

                Integer result = divisionResult.ok();
                System.out.println("Result: " + result);
            }

            String input1 = scanner.nextLine();

            if (input1.equals("exit") || input1.isBlank()) {
                System.out.println("Exiting...");
                break;
            }

            Result<Integer, ParseIntError> parsed1 = calc.parseInt(input1);

            if (parsed1.isErr()) {
                System.out.println("Error: " + parsed1.err().toString());
                continue;
            }

            Integer number1 = parsed1.ok();
        }
    }
}
