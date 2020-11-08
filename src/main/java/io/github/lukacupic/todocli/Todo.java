package io.github.lukacupic.todocli;

public class Todo {

    private int start;
    private int end;
    private String body;
    private String ticketID;
    private String estimatedTime;
    private String path;

    public Todo(int start, int end, String body, String ticketID, String estimatedTime, String path) {
        this.start = start;
        this.end = end;
        this.body = body;
        this.ticketID = ticketID;
        this.estimatedTime = estimatedTime;
        this.path = path;
    }

    public Todo(int start, int end, String ticketID, String estimatedTime) {
        this.start = start;
        this.end = end;
        this.ticketID = ticketID;
        this.estimatedTime = estimatedTime;
    }

    public Todo(int start, int end, String body) {
        this.start = start;
        this.end = end;
        this.body = body;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public String getTicketID() {
        return ticketID;
    }

    public String getEstimatedTime() {
        return estimatedTime;
    }

    public String getBody() {
        return body;
    }

    public String getPath() {
        return path;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setTicketID(String ticketID) {
        this.ticketID = ticketID;
    }

    public void setEstimatedTime(String estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Compares this Todo object to the given Todo. The method returns true if and only
     * if the following conditions are satisfied: The objects have the same starting and ending
     * line numbers; the objects have the same ticket ID; the objects have the same estimated time.
     * <p>
     * Body is not compared because there could be slight irregularities as a result of parsing,
     * such as an extra or a missing space, non-corresponding punctuation marks, etc.
     *
     * @param o the object to compare this one to
     * @return true if the Todo objects are equal; false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Todo todo = (Todo) o;

        if (start != todo.start) return false;
        if (end != todo.end) return false;
        if (!ticketID.equals(todo.ticketID)) return false;
        return estimatedTime.equals(todo.estimatedTime);
    }

    @Override
    public int hashCode() {
        int result = start;
        result = 31 * result + end;
        result = 31 * result + ticketID.hashCode();
        result = 31 * result + estimatedTime.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("TODO [Lines: %d-%d, TicketID: %s, Time: %s, Body: %s]",
                start, end, ticketID, estimatedTime, body
        );
    }
}