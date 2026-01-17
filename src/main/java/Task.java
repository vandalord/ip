public class Task {

    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getStatusIcon() {
        return (isDone ? "[X]" : "[ ]");
    }

    public boolean mark_task() {
        if (isDone) {
            return false;
        }
        isDone = true;
        return true;
    }

    public boolean unmark_task() {
        if (!isDone) {
            return false;
        }
        isDone = false;
        return true;
    }

    @Override
    public String toString() {
        return getStatusIcon() + " " +  description;
    }
}

