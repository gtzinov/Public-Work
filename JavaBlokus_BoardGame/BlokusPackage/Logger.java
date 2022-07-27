package BlokusPackage;

import java.io.IOException;
import java.nio.file.*;
import java.time.*;

// Logs events to a unique log file
public class Logger implements Observer{

    private final Path path = Path.of(System.getProperty("user.dir"), "logs", String.format("Log_%s_%s", LocalDate.now(), LocalTime.now().toString().replaceAll(":|\\.", "_")+".txt"));

    // Prints the exception
    private void err(Exception e) {
        System.out.printf("Logging Error: %s", e);
    }

    // Writes a string to the log file
    private void write(String to_log) {
        try {
            Files.writeString(path, to_log+System.lineSeparator(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        }
        catch (IOException e) {
            err(e);
        }
    }

    // Logs the specified event with the given parameters
    public void update(Observer.Event event, String[] args) {
        try {
            switch (event) {
                case PIECEPLACED -> {
                    write(String.format("PIECEPLACED %s %s %s %s", (Object[]) args));
                }
                case ROTATE -> {
                    write(String.format("ROTATE %s", (Object[]) args));
                }
                case FLIP -> {
                    write(String.format("FLIP %s", (Object[]) args));
                }
                case GIVEUP -> {
                    write(String.format("GIVEUP %s", (Object[]) args));
                }
                case VICTORY -> {
                    write(String.format("VICTORY %s", (Object[]) args));
                }
            }
        }
        catch (ArrayIndexOutOfBoundsException e) {
            err(e);
        }
    }

}
