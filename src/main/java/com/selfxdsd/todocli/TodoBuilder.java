/**
 * Copyright (c) 2020-2021, Self XDSD Contributors
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

/**
 * Builder for {@link Todo}.
 * @author criske
 * @version $Id$
 * @since 0.0.2
 */
class TodoBuilder {

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
    private String ticketId;

    /**
     * The estimated time (in minutes) to complete this Todo.
     */
    private int estimatedTime;

    /**
     * The path of the file from which this Todo was extracted.
     */
    private String path;


    /**
     * The author of todo.
     */
    private String author;

    /**
     * Timestamp of creation.
     */
    private String timestamp;

    /**
     * The first line of the Todo.
     *
     * @param start First line.
     * @return Builder.
     */
    public TodoBuilder setStart(final int start) {
        this.start = start;
        return this;
    }

    /**
     * The last line of the Todo.
     *
     * @param end Last line.
     * @return Builder.
     */
    public TodoBuilder setEnd(final int end) {
        this.end = end;
        return this;
    }

    /**
     * The Todo's body.
     *
     * @param body Body.
     * @return Builder.
     */
    public TodoBuilder setBody(final String body) {
        this.body = body;
        return this;
    }

    /**
     * The ID of the ticket denoted by this Todo.
     *
     * @param ticketId Ticket id.
     * @return Builder.
     */
    public TodoBuilder setTicketId(final String ticketId) {
        this.ticketId = ticketId;
        return this;
    }

    /**
     * Sets the estimated time.
     *
     * @param estimatedTime The estimated time
     * @return Builder.
     */
    public TodoBuilder setEstimatedTime(final int estimatedTime) {
        this.estimatedTime = estimatedTime;
        return this;
    }

    /**
     * The path of the file from which this Todo was extracted.
     *
     * @param path Path.
     * @return Builder.
     */
    public TodoBuilder setPath(final String path) {
        if (path != null && path.startsWith("./")) {
            this.path = path.substring(2);
        } else {
            this.path = path;
        }
        return this;
    }

    /**
     * Set the todo author.
     * @param author Author.
     * @return Builder.
     */
    public TodoBuilder setAuthor(final String author){
        this.author = author;
        return this;
    }

    /**
     * Set the todo creation timestamp.
     * @param timestamp Timestamp.
     * @return Builder.
     */
    public TodoBuilder setTimestamp(final String timestamp){
        this.timestamp = timestamp;
        return this;
    }

    /**
     * Creates a new Todo.
     *
     * @return Todo
     */
    public Todo build() {
        final Todo todo = new Todo(
            this.start,
            this.end,
            this.ticketId,
            this.estimatedTime,
            this.body
        );
        todo.setPath(this.path);
        todo.setAuthor(this.author);
        todo.setTimestamp(this.timestamp);
        return todo;
    }
}
