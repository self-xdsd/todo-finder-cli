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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Todo Parser.
 * @version $Id$
 * @since 0.0.1
 */
public final class TodoParser {

    /**
     * TODO Pattern.
     */
    private static final Pattern TODO_PATTERN = Pattern.compile(
        "^\\W*(@todo|TODO|@fixme|FIXME)\\s*(#\\d+:\\d+(m|min|mins))\\b(.*)$"
    );

    /**
     * Finds and returns a list of all TODOs found in the file given its path.
     * @param path Path to the file being parsed.
     * @return List of found TODOs.
     * @throws IOException If something goes wrong.
     * @checkstyle ExecutableStatementCount (50 lines)
     */
    public List<Todo> parse(final String path) throws IOException {
        final List<Todo> todos = new ArrayList<>();
        final List<String> lines = Files.readAllLines(Paths.get(path));
        final StringBuilder bodyBuilder = new StringBuilder();
        final TodoBuilder todoBuilder = new TodoBuilder().setPath(path);
        int lineIndex = -1;
        int todoPosition = -1;
        while (++lineIndex < lines.size()) {
            // replace tabs with 4 spaces to have constituency when check for
            // alignment in the case of multiline body todos.
            // NOTE: this assumes that user is using 4 spaces for a tab.
            final String line = lines.get(lineIndex)
                .replace("\t", " ".repeat(4));
            if (todoPosition == -1) {
                final Matcher matcher = TODO_PATTERN.matcher(line);
                if (matcher.find()) {
                    todoPosition = matcher.start(1);
                    todoBuilder.setStart(lineIndex + 1);
                    bodyBuilder.append(matcher.group(4));
                    this.addHeader(todoBuilder, matcher.group(2));
                }
            } else if (todoPosition < line.length()
                && Character.isSpaceChar(line.charAt(todoPosition))) {
                // if the character aligned to todoPosition is a white space,
                // then we have multiline.
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
                //reset buffer and position
                bodyBuilder.setLength(0);
                todoPosition = -1;
                //go back one position to check if line is starting another todo
                lineIndex--;
            }
        }
        return todos;
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

}