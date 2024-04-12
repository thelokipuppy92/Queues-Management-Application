package com.example.pt2024_group5_moga_diana_assignment2;
import java.io.*;

public class Logger {
    private StringBuilder log;

    public Logger() {
        this.log = new StringBuilder();
    }

    public void logEvent(String event) {
        log.append(event).append("\n");
    }

    public void writeLogToFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(log.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
