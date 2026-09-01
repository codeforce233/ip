import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class Event extends Task {
    protected LocalDateTime from;
    protected LocalDateTime to;
    protected String fromText;
    protected String toText;
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

    public Event(String description, String from, String to) {
        super(description, TaskType.EVENT);
        this.fromText = from == null ? "" : from.trim();
        this.toText = to == null ? "" : to.trim();
        this.from = parseDateTime(this.fromText);
        this.to = parseDateTime(this.toText);
    }

    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description, TaskType.EVENT);
        this.from = from;
        this.to = to;
        this.fromText = from == null ? "" : from.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.toText = to == null ? "" : to.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public LocalDateTime getFrom() {
        return from;
    }

    public LocalDateTime getTo() {
        return to;
    }

    public String getFromText() {
        return fromText;
    }

    public String getToText() {
        return toText;
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

    private String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        if (dateTime.toLocalTime().equals(LocalTime.MIDNIGHT)) {
            return dateTime.toLocalDate().format(DATE_ONLY_DISPLAY_FORMATTER);
        }
        return dateTime.format(DISPLAY_FORMATTER);
    }

    @Override
    public String toString() {
        return "[" + type.getSymbol() + "][" + getStatusIcon() + "] " + description
                + " (from: " + (from != null ? formatDateTime(from) : fromText)
                + " to: " + (to != null ? formatDateTime(to) : toText) + ")";
    }
}
