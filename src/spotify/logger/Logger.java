package spotify.logger;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    private static final String LOG_FILE_PATH = "error_logs.txt";

    public static void logError(String username, Exception e) {
        logToFile(username, e);
    }

    private static void logToFile(String username, Exception exception) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE_PATH, true))) {
            if (username == null) {
                writer.println(getCurrentTimestamp() + " ERROR: " + exception.getMessage());
            }
            writer.println(getCurrentTimestamp() + " ERROR: " + exception.getMessage());
            exception.printStackTrace(writer);
            writer.println("---------------------------------------------");
        } catch (IOException e) {
            throw new RuntimeException("Failed to log error", e);
        }
    }

    private static String getCurrentTimestamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date());
    }
}
