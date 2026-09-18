package sage.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;
import java.util.Locale;

/**
 * Shares the date parsing and display rules used by deadlines and events.
 */
public final class TaskDateTime {
    private static final DateTimeFormatter DISPLAY_FORMATTER =
            DateTimeFormatter.ofPattern("MMM d uuuu, h:mma", Locale.ENGLISH);
    private static final DateTimeFormatter DATE_ONLY_DISPLAY_FORMATTER =
            DateTimeFormatter.ofPattern("MMM d uuuu", Locale.ENGLISH);
    private static final List<DateTimeFormatter> INPUT_FORMATTERS = List.of(
            strictFormatter("d/M/uuuu HHmm"),
            strictFormatter("d/M/uuuu HH:mm"),
            strictFormatter("d/M/uuuu"),
            strictFormatter("uuuu-MM-dd HHmm"),
            strictFormatter("uuuu-MM-dd HH:mm"),
            strictFormatter("uuuu-MM-dd"),
            DateTimeFormatter.ISO_LOCAL_DATE_TIME
    );

    private TaskDateTime() {
    }

    /**
     * Parses supported input formats, treating dates without a time as midnight.
     *
     * @param rawValue The date or date-time text to parse.
     * @return The parsed value, or null for blank or unsupported text.
     */
    public static LocalDateTime parse(String rawValue) {
        String value = rawValue == null ? "" : rawValue.trim();
        if (value.isEmpty()) {
            return null;
        }

        for (DateTimeFormatter formatter : INPUT_FORMATTERS) {
            LocalDateTime parsedValue = tryParse(value, formatter);
            if (parsedValue != null) {
                return parsedValue;
            }
        }
        return null;
    }

    /**
     * Checks date-like input strictly while retaining natural-language times such as Sunday.
     *
     * @param value The nonempty time text to validate.
     * @throws IllegalArgumentException If a numeric date is invalid or the value is empty.
     */
    public static void validate(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("The time cannot be empty.");
        }
        if (parse(value) == null && value.strip().matches("[+-]?\\d+\\s*[/.-].*")) {
            throw new IllegalArgumentException("That date or time is invalid. Use a real date such as "
                    + "2026-09-18 or 18/9/2026 1430.");
        }
    }

    /**
     * Builds a formatter that rejects impossible dates instead of silently adjusting them.
     *
     * @param pattern The supported date pattern.
     * @return The strict, language-independent formatter.
     */
    private static DateTimeFormatter strictFormatter(String pattern) {
        return DateTimeFormatter.ofPattern(pattern, Locale.ENGLISH).withResolverStyle(ResolverStyle.STRICT);
    }

    /**
     * Attempts one format as a date-time, then as a date without a time.
     *
     * @param value The nonblank date text.
     * @param formatter The format to try.
     * @return The parsed value, or null if this format does not match.
     */
    private static LocalDateTime tryParse(String value, DateTimeFormatter formatter) {
        try {
            return LocalDateTime.parse(value, formatter);
        } catch (DateTimeParseException ignored) {
            // The format may describe a date without a time; try that next.
        }
        try {
            return LocalDate.parse(value, formatter).atStartOfDay();
        } catch (DateTimeParseException ignored) {
            // A format mismatch is expected while searching the supported formats.
            return null;
        }
    }

    /**
     * Formats a parsed date-time, retaining the original text when parsing was unavailable.
     *
     * @param dateTime The parsed value, possibly null.
     * @param fallbackText The original text to display for an unparsed value.
     * @return The display text, omitting the time at midnight.
     */
    static String format(LocalDateTime dateTime, String fallbackText) {
        if (dateTime == null) {
            return fallbackText;
        }
        if (dateTime.toLocalTime().equals(LocalTime.MIDNIGHT)) {
            return dateTime.toLocalDate().format(DATE_ONLY_DISPLAY_FORMATTER);
        }
        return dateTime.format(DISPLAY_FORMATTER);
    }
}
