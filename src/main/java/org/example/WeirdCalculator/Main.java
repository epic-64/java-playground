package org.example.WeirdCalculator;

import org.example.Result;

import java.util.Scanner;

public class Main {
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
