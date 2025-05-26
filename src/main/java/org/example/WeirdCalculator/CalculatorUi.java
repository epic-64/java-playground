package org.example.WeirdCalculator;

import java.util.Map;

public class CalculatorUi {
    private final SafeCalculator calculator;

    public CalculatorUi(SafeCalculator calculator) {
        this.calculator = calculator;
    }

    public String[] getMenu() {
        String[] menuOptions = getMenuOptions()
            .entrySet()
            .stream()
            .map(entry -> String.format("[%s] %s", entry.getKey(), entry.getValue()))
            .toArray(String[]::new);

        return new String[]{
            "Weird Calculator Menu",
            String.join("\n", menuOptions),
            "---------------------",
            "Enter your choice: ",
        };
    }

    public Map<String, String> getMenuOptions() {
        return Map.of(
            "1", "Divide two numbers",
            "2", "Parse an integer",
            "q", "Quit"
        );
    }
}
