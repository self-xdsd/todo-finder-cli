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
     * @checkstyle ExecutableStatementCount (60 lines)
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
                if (!hasTodoStarted(todoPosition)) {
                    final Matcher matcher = this.canStartTodo(line);
                    if (matcher != null) {
                        todoPosition = this.startTodo(
                            matcher,
                            bodyBuilder,
                            todoBuilder,
                            lineIndex,
                            line
                        );
                    }
                } else if (this.isLinePartOfTodo(todoPosition, line)) {
                    // this line might start a todo too even if is passing the
                    // todo line criteria.
                    final Matcher matcher = this.canStartTodo(line);
                    if (matcher != null) {
                        this.endTodo(
                            bodyBuilder,
                            todoBuilder,
                            lineIndex,
                            todos
                        );
                        todoPosition = this.startTodo(
                            matcher,
                            bodyBuilder,
                            todoBuilder,
                            lineIndex,
                            line
                        );
                    } else {
                        this.addLineToTodo(todoPosition, line, bodyBuilder);
                    }
                } else {
                    todoPosition = this.endTodo(
                        bodyBuilder,
                        todoBuilder,
                        lineIndex,
                        todos
                    );
                    final Matcher matcher = this.canStartTodo(line);
                    if (matcher != null) {
                        todoPosition = this.startTodo(
                            matcher,
                            bodyBuilder,
                            todoBuilder,
                            lineIndex,
                            line
                        );
                    }
                }
            }
        }
        return todos;
    }

    /**
     * Checks if todo has started.
     * @param todoPosition Position.
     * @return Boolean
     */
    private boolean hasTodoStarted(final int todoPosition) {
        return todoPosition != -1;
    }

    /**
     * Checks if the line has valid todo in it.
     * @param line Line.
     * @return Matcher or null if there is no todo.
     */
    private Matcher canStartTodo(final String line){
        final Matcher matcher = TODO_PATTERN.matcher(line);
        final Matcher canStart;
        if (matcher.find()) {
            canStart = matcher;
        } else {
            canStart = null;
        }
        return canStart;
    }

    /**
     * Starts a todo.
     * @param matcher Matcher
     * @param bodyBuilder Todo body builder
     * @param todoBuilder Tod builder.
     * @param lineIndex File line index.
     * @param line File line
     * @return Starting position.
     */
    private int startTodo(final Matcher matcher,
                          final StringBuilder bodyBuilder,
                          final TodoBuilder todoBuilder,
                          final int lineIndex,
                          final String line){
        final int todoPosition = matcher.start(3);
        todoBuilder.setAuthor(matcher.group(1).trim())
            .setTimestamp(matcher.group(2))
            .setStart(lineIndex + 1);
        bodyBuilder.append(matcher.group(6));
        this.addHeader(todoBuilder, matcher.group(4));
        return todoPosition;
    }

    /**
     * Ends the todo. This will create the Todo from builder and
     * add it to the list. Also resets the body builder and reset
     * the todoPosition (-1).
     * @param bodyBuilder Todo body builder.
     * @param todoBuilder Todo builder.
     * @param lineIndex File line index.
     * @param todos List of todos.
     * @return Todo position reset value.
     */
    private int endTodo(final StringBuilder bodyBuilder,
                        final TodoBuilder todoBuilder,
                        final int lineIndex,
                        final List<Todo> todos){
        final String body = bodyBuilder.toString().trim();
        if (!body.isBlank() && !body.startsWith("Autogenerated")) {
            final Todo todo = todoBuilder
                .setBody(body)
                .setEnd(lineIndex)
                .build();
            todos.add(todo);
        }
        bodyBuilder.setLength(0);
        return -1;
    }

    /**
     * Checks if current line could be part of todo.
     * @param todoPosition Todo position.
     * @param line File line.
     * @return Boolean.
     */
    private boolean isLinePartOfTodo(final int todoPosition, final String line){
        return todoPosition < line.length()
            && Character.isSpaceChar(line.charAt(todoPosition))
            && line.substring(0, todoPosition + 1)
             .matches("^"+GIT_BLAME_PATTERN+"\\s*\\W?\\s+$");
    }

    /**
     * Add current line to todo body builder starting from todoPosition.
     * @param todoPosition Todo position.
     * @param bodyBuilder Todo body builder.
     * @param line File line.
     */
    private void addLineToTodo(final int todoPosition,
                               final String line,
                               final StringBuilder bodyBuilder){
        bodyBuilder.append(line.substring(todoPosition).stripTrailing());
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
