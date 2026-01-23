import java.io.Serializable;
import java.time.LocalDate;

public class Task implements Serializable {

    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getStatusIcon() {
        return (isDone ? "[X]" : "[ ]");
    }

    public boolean markTask() {
        if (isDone) {
            return false;
        }
        isDone = true;
        return true;
    }

    public boolean unmarkTask() {
        if (!isDone) {
            return false;
        }
        isDone = false;
        return true;
    }

    public boolean occursOn(LocalDate date) {
        return false;
    }

    @Override
    public String toString() {
        return getStatusIcon() + " " +  description;
    }
}

