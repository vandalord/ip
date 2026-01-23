import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Event extends Task {

    protected LocalDateTime from;
    protected LocalDateTime to;

    public Event(String description, String from, String to) {
        super(description);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
        this.from = LocalDateTime.parse(from, formatter);
        this.to = LocalDateTime.parse(to, formatter);
    }

    @Override
    public String toString() {
        return "[E]"
            + super.toString()
            + " (from: "
            + from.format(DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma"))
            + " to: "
            + to.format(DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma"))+")";
    }
}