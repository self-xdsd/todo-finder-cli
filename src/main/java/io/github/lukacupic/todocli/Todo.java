package io.github.lukacupic.todocli;

public class Todo {

    private int start;
    private int end;
    private String body;
    private String ticketID;
    private String estimatedTime;

    public Todo(int start, int end, String body, String header) {
        this.start = start;
        this.end = end;
        this.body = body;

        parseHeader(header);
    }

    private void parseHeader(String header) {
        String[] parts = header.split(":");
        this.ticketID = parts[0];
        this.estimatedTime = parts[1];
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

    /**
     * Compares this Todo object to the given Todo. The method returns true if and only
     * if the following conditions are satisfied: The objects have the same starting and ending
     * line numbers; the objects have the same ticket ID; the objects have the same estimated time.
     * <p>
     * Body is not compared because there could be slight irregularities as a result from parsing,
     * such as an extra space, missing punctuation marks, etc.
     *
     * @param other the object to compare this one to
     * @return true if the Todo objects are equal; false otherwise
     */
    public boolean compareTo(Todo other) {

    }

    @Override
    public String toString() {
        return String.format("TODO [Lines: %d-%d, TicketID: %s, Time: %s, Body: %s]",
                start, end, ticketID, estimatedTime, body
        );
    }
}