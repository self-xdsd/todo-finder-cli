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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Todo Parser.
 *
 * @version $Id$
 * @since 0.0.1
 */
public final class TodoParser {

    /**
     * Git blame header pattern that should be at the start of each line.
     */
    private static final String GIT_BLAME_PATTERN =
        ".*\\((.+)\\s+(\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}\\s\\+\\d{4})"
        + "\\s+\\d+\\)\\s";

    /**
     * TODO Pattern.
     */
    private static final Pattern TODO_PATTERN = Pattern.compile(
        "^" + GIT_BLAME_PATTERN
            + "\\W*(@todo|TODO|@fixme|FIXME)\\s*(#\\d+:\\d+(m|min|mins))\\b(.*)"
            + "$"
    );

    /**
     * Finds and returns a list of all TODOs found in the file given its path.
     * @param path Path to the file being parsed.
     * @return List of found TODOs.
     * @throws IOException If something goes wrong.
     */
    public List<Todo> parse(final String path) throws IOException {
        final List<Todo> todos = new ArrayList<>();
        try (final BufferedReader reader = this.readFileWithBlame(path)) {
            final StringBuilder bodyBuilder = new StringBuilder();
            final TodoBuilder todoBuilder = new TodoBuilder().setPath(path);
            int lineIndex = -1;
            int todoPosition = -1;
            String line;
            while ((line = reader.readLine()) != null) {
                lineIndex++;
                // replace tabs with 4 spaces to have constituency when check
                // for alignment in the case of multiline body todos.
                // NOTE: this assumes that user is using 4 spaces for a tab.
                line = line.replace("\t", " ".repeat(4));
                if (todoPosition == -1) {
                    todoPosition = this.checkForTodo(
                        bodyBuilder,
                        todoBuilder,
                        lineIndex,
                        line
                    );
                } else if (todoPosition < line.length()
                    && Character.isSpaceChar(line.charAt(todoPosition))) {
                    // if the character aligned to todoPosition is a white
                    // space, then we have multiline.
                    bodyBuilder.append(line.substring(todoPosition));
                } else {
                    final Todo todo = this.createTodo(
                        bodyBuilder,
                        todoBuilder,
                        lineIndex
                    );
                    if (todo != null) {
                        todos.add(todo);
                    }
                    //reset buffer
                    bodyBuilder.setLength(0);
                    todoPosition = this.checkForTodo(
                        bodyBuilder,
                        todoBuilder,
                        lineIndex,
                        line
                    );
                }
            }
        }
        return todos;
    }

    /**
     * Initializes a new todo and its body via builders when todo pattern
     * is matching current line and returns the todo starting position.
     * @param bodyBuilder Todo builder.
     * @param todoBuilder Body builder.
     * @param lineIndex Current line index.
     * @param line Current line.
     * @return Todo starting position in the line or -1 if there is no match.
     */
    private int checkForTodo(final StringBuilder bodyBuilder,
                             final TodoBuilder todoBuilder,
                             final int lineIndex,
                             final String line) {
        final Matcher matcher = TODO_PATTERN.matcher(line);
        final int todoPosition;
        if (matcher.find()) {
            todoPosition = matcher.start(3);
            todoBuilder.setAuthor(matcher.group(1).trim())
                .setTimestamp(matcher.group(2))
                .setStart(lineIndex + 1);
            bodyBuilder.append(matcher.group(6));
            this.addHeader(todoBuilder, matcher.group(4));
        } else {
            todoPosition = -1;
        }
        return todoPosition;
    }

    /**
     * Creates the Todo if passes the body conditions.
     * @param bodyBuilder Body builder.
     * @param todoBuilder Todo builder.
     * @param lineIndex Line index where will be the todo "end".
     * @return Todo or null if was not created.
     */
    private Todo createTodo(final StringBuilder bodyBuilder,
                            final TodoBuilder todoBuilder,
                            final int lineIndex) {
        final String body = bodyBuilder.toString().trim();
        final Todo todo;
        if (!body.isBlank() && !body.startsWith("Autogenerated")) {
            todo = todoBuilder.setBody(body).setEnd(lineIndex).build();
        } else {
            todo = null;
        }
        return todo;
    }

    /**
     * Adds the header consisting of ticket id and estimated time to todo
     * builder.
     * @param todoBuilder TodoBuilder.
     * @param header Header.
     */
    private void addHeader(final TodoBuilder todoBuilder,
                           final String header) {
        final String[] parts = header.split(":");
        todoBuilder.setTicketId(parts[0]);
        todoBuilder.setEstimatedTime(
            Integer.parseInt(
                parts[1].replaceAll("[A-Za-z]+", "")
            )
        );
    }

    /**
     * Reads file together with its git blame.
     * @param path File path.
     * @return Reader.
     * @throws IOException If something goes wrong.
     */
    private BufferedReader readFileWithBlame(final String path)
        throws IOException {
        final String command = "git blame " + path;
        final Process process = Runtime.getRuntime().exec(command);
        return new BufferedReader(
            new InputStreamReader(process.getInputStream())
        );
    }
}
