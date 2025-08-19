package dev.scopped.zenLifesteal.utility;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class StringUtils {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static String replace(String message, Object... params) {
        if (params.length % 2 != 0) {
            throw new IllegalArgumentException("Parameters should be in key-value pairs.");
        }

        for (int i = 0; i < params.length; i += 2) {
            message = message.replace(params[i].toString(), params[i + 1].toString());
        }

        return message;
    }

    public static String formatTime(long seconds) {
        long[] values = {seconds / 86400L, seconds % 86400L / 3600L, seconds % 3600L / 60L, seconds % 60L};
        String[] labels = {"d", "h", "m", "s"};

        StringBuilder formattedTime = new StringBuilder();

        for (int i = 0; i < values.length; i++) {
            if (values[i] > 0) {
                formattedTime.append(values[i]).append(labels[i]).append(" ");
            }
        }

        return formattedTime.isEmpty() ? "0s" : formattedTime.toString().trim();
    }

    public static String formatDate(long millis) {
        LocalDateTime dateTime = Instant.ofEpochMilli(millis)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        return dateTime.format(DATE_FORMATTER);
    }

}
