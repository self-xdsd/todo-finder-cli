package io.github.lukacupic.todocli;

public class Todo {

    /**
     * The first line of the Todo.
     */
    private int start;

    /**
     * The last line of the Todo.
     */
    private int end;

    /**
     * The Todo's body.
     */
    private String body;

    /**
     * The ID of the ticket denoted by this Todo.
     */
    private String ticketID;

    /**
     * The estimated time to complete this Todo.
     */
    private String estimatedTime;

    /**
     * The path of the file from which this Todo was extracted.
     */
    private String path;

    /**
     * Creates a new Todo object.
     *
     * @param start         the starting line
     * @param end           the ending line
     * @param ticketID      the ticket ID
     * @param estimatedTime the estimated time
     */
    public Todo(int start, int end, String ticketID, String estimatedTime) {
        this.start = start;
        this.end = end;
        this.ticketID = ticketID;
        this.estimatedTime = estimatedTime;
    }

    /**
     * Creates a new Todo object.
     *
     * @param start the starting line
     * @param end   the ending line
     * @param body  the body
     */
    public Todo(int start, int end, String body) {
        this.start = start;
        this.end = end;
        this.body = body;
    }

    /**
     * Sets the ticket ID.
     *
     * @param ticketID the ticket ID
     */
    public void setTicketID(String ticketID) {
        this.ticketID = ticketID;
    }

    /**
     * Sets the estimated time.
     *
     * @param estimatedTime the estimated time
     */
    public void setEstimatedTime(String estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    /**
     * Sets the path.
     *
     * @param path the path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Compares this to the given, other, Todo object. The method returns true if and only
     * if the following conditions are satisfied: The objects have the same starting and ending
     * line numbers; the objects have the same ticket ID; the objects have the same estimated time.
     * <p>
     * Body is not compared because there could be slight irregularities as a result of parsing,
     * such as a difference in a blank character or a punctuation mark.
     *
     * @param o the object to compare this one to
     * @return true if the objects are equal; false otherwise
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
        return String.format("TODO [Lines: %d-%d, TicketID: %s, Estimated Time: %s, Body: %s, Path: %s]",
                start, end, ticketID, estimatedTime, body, path
        );
    }
}