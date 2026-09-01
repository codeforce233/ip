package sage.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class Deadline extends Task {
    protected LocalDateTime by;
    protected String byText;
    private static final DateTimeFormatter DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("MMM d yyyy, h:mma");
    private static final DateTimeFormatter DATE_ONLY_DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("MMM d yyyy");
    private static final List<DateTimeFormatter> INPUT_FORMATTERS = List.of(
            DateTimeFormatter.ofPattern("d/M/yyyy HHmm"),
            DateTimeFormatter.ofPattern("d/M/yyyy HH:mm"),
            DateTimeFormatter.ofPattern("d/M/yyyy"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
    );

    public Deadline(String description, String by) {
        super(description, TaskType.DEADLINE);
        this.byText = by == null ? "" : by.trim();
        this.by = parseDateTime(this.byText);
    }

    public Deadline(String description, LocalDateTime by) {
        super(description, TaskType.DEADLINE);
        this.by = by;
        this.byText = by == null ? "" : by.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public LocalDateTime getBy() {
        return by;
    }

    public String getByText() {
        return byText;
    }

    public static LocalDateTime parseDateTime(String rawValue) {
        String value = rawValue == null ? "" : rawValue.trim();
        if (value.isEmpty()) {
            return null;
        }

        for (DateTimeFormatter formatter : INPUT_FORMATTERS) {
            try {
                try {
                    return LocalDateTime.parse(value, formatter);
                } catch (DateTimeParseException ignored) {
                    LocalDate date = LocalDate.parse(value, formatter);
                    return LocalDateTime.of(date, LocalTime.MIDNIGHT);
                }
            } catch (DateTimeParseException ignored) {
                // Try the next supported format.
            }
        }

        try {
            return LocalDateTime.parse(value);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private String formatBy() {
        if (by == null) {
            return byText;
        }
        if (by.toLocalTime().equals(LocalTime.MIDNIGHT)) {
            return by.toLocalDate().format(DATE_ONLY_DISPLAY_FORMATTER);
        }
        return by.format(DISPLAY_FORMATTER);
    }

    @Override
    public String toString() {
        return "[" + type.getSymbol() + "][" + getStatusIcon() + "] " + description + " (by: " + formatBy() + ")";
    }
}
