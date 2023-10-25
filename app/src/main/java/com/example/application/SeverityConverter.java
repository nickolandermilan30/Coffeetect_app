package com.example.application;

public class SeverityConverter {

    public static int convertToInteger(String severity) {
        switch (severity) {
            case "Mild":
                return 1;
            case "Moderate":
                return 2;
            case "Critical":
                return 3;
            default:
                return 0; // Default severity level
        }
    }

    public static String convertToString(int severity) {
        switch (severity) {
            case 1:
                return "Mild";
            case 2:
                return "Moderate";
            case 3:
                return "Critical";
            default:
                return "Unknown"; // Default severity level
        }
    }
}
