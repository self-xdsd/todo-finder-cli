/**
 * Copyright (c) 2020, Self XDSD Contributors
 * All rights reserved.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"),
 * to read the Software only. Permission is hereby NOT GRANTED to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.selfxdsd.todocli;

import java.util.Objects;

/**
 * Representation of a TODO or FIXME.
 *
 * @version $Id$
 * @checkstyle AbbreviationAsWordInName (500 lines)
 * @checkstyle FinalParameters (500 lines)
 * @since 0.0.1
 * @todo #4:60min  Add interface Todos which should represent
 *  an iterable of Todo objects. Afterwards, add class JsonTodos,
 *  which should represent the TODOs as JsonArray.
 */
public final class Todo {

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
     * The estimated time (in minutes) to complete this Todo.
     */
    private int estimatedTime;

    /**
     * The path of the file from which this Todo was extracted.
     */
    private String path;

    /**
     * Creates a new Todo object.
     *
     * @param start The starting line
     * @param end The ending line
     * @param ticketID The ticket ID
     * @param estimatedTime The estimated time
     */
    public Todo(int start, int end, String ticketID, int estimatedTime) {
        this.start = start;
        this.end = end;
        this.ticketID = ticketID;
        this.estimatedTime = estimatedTime;
    }

    /**
     * Creates a new Todo object.
     *
     * @param start The starting line
     * @param end The ending line
     * @param ticketID The ticket ID
     * @param estimatedTime The estimated time
     * @param body The body
     */
    public Todo(
        int start, int end, String ticketID,
        int estimatedTime, String body
    ) {
        this.start = start;
        this.end = end;
        this.ticketID = ticketID;
        this.estimatedTime = estimatedTime;
        this.body = body;
    }

    /**
     * Creates a new Todo object.
     *
     * @param start The starting line
     * @param end The ending line
     * @param body The body
     */
    public Todo(int start, int end, String body) {
        this.start = start;
        this.end = end;
        this.body = body;
    }

    /**
     * Returns the unique identifier of this Todo.
     * Todo's ID depends on the body, originating ticket, and estimation time.
     * @return ID of this Todo object.
     */
    public long getID() {
        return hashCode();
    }

    /**
     * Gets the starting line.
     * @return The starting line
     */
    public int getStart() {
        return start;
    }

    /**
     * Gets the ending line.
     * @return The ending line
     */
    public int getEnd() {
        return end;
    }

    /**
     * Gets the body. If the body has not been set, the method
     * returns an empty string.
     *
     * @return The body
     */
    public String getBody() {
        final String body;
        if(this.body == null) {
            body = "";
        } else {
            body = this.body;
        }
        return body;
    }

    /**
     * Gets the ticket ID.
     *
     * @return The ticket ID
     */
    public String getTicketID() {
        return ticketID;
    }

    /**
     * Gets the estimated time.
     *
     * @return The estimated time
     */
    public int getEstimatedTime() {
        return estimatedTime;
    }

    /**
     * Gets the path of the file from which this Todo was extracted.
     *
     * @return The path
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets the ticket ID.
     *
     * @param ticketID The ticket ID
     */
    public void setTicketID(final String ticketID) {
        this.ticketID = ticketID;
    }

    /**
     * Sets the estimated time.
     *
     * @param estimatedTime The estimated time
     */
    public void setEstimatedTime(final int estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    /**
     * Sets the path.
     *
     * @param path The path
     */
    public void setPath(final String path) {
        this.path = path;
    }


    /**
     * Compares this to the given, other, Todo object.
     * The method returns true if and only if the following conditions
     * are satisfied:<br><br>
     * The objects have the same starting and ending line numbers;<br>
     * The objects have the same estimated time;<br>
     * The objects have the same ticket ID.
     *
     * @param other The object to compare this one to
     * @return True if the objects are equal; false otherwise
     * @checkstyle ReturnCount (50 lines)
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Todo todo = (Todo) other;
        if (estimatedTime == todo.estimatedTime) {
            if (Objects.equals(body, todo.body)) {
                if (ticketID.equals(todo.ticketID)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result;
        if(this.body != null) {
            result = this.body.hashCode();
        } else {
            result = 0;
        }
        result = 31 * result + ticketID.hashCode();
        result = 31 * result + estimatedTime;
        return result;
    }

    @Override
    public String toString() {
        final String linesPart;
        if (this.start == this.end) {
            linesPart = "Line: " + this.start;
        } else {
            linesPart = String.format("Lines: %d-%d", this.start, this.end);
        }

        return String.format(
                "TODO [%s, TicketID: %s, Estimated Time: %s, "
                        + "Body: '%s', Path: '%s']",
                linesPart,
                this.ticketID,
                this.estimatedTime,
                this.body,
                this.path
        );
    }
}
