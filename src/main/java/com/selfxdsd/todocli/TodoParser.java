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
     * TODO Markers.
     */
    private static final String MARKERS = "@todo|TODO|@fixme|FIXME";

    /**
     * Regex used for matching a single-line comment.
     */
    private final String singleLineRegex;


    /**
     * Regex used for matching a multi-line comment.
     */
    private final String multiLineRegex;

    /**
     * Pattern used for matching the body of a TODO in a multi-line comment.
     */
    private final Pattern multiLinePattern;

    /**
     * Index of the currently parsed line.
     */
    private int currentIndex;

    /**
     * Creates a new TodoParser object.
     */
    public TodoParser() {
        this.singleLineRegex = "^(\\s*//\\s*)(" + MARKERS + ").*$";
        this.multiLineRegex = "^(\\s*[*]\\s*)(" + MARKERS + ").*$";
        this.multiLinePattern = Pattern.compile("(\\s*[*])(\\s\\s)(.*)");
    }

    /**
     * Finds and returns a list of all TODOs found in the file given its path.
     * @param path Path to the file being parsed.
     * @return List of found TODOs.
     * @throws IOException If something goes wrong.
     */
    public List<Todo> parse(final String path) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(path));
        List<Todo> todos = new ArrayList<>();

        for (currentIndex = 0; currentIndex < lines.size(); currentIndex++) {
            String line = lines.get(currentIndex);

            int headerIndex = getHeaderIndex(line);
            if (headerIndex == -1) {
                continue;
            }

            final String[] parts = line.substring(headerIndex)
                .trim().split("\\s", 2);
            if (parts[0].isEmpty()) {
                continue;
            }

            final String header = parts[0].trim();
            final String body;
            if (parts.length == 2) {
                body = parts[1].trim();
            } else {
                body = "";
            }

            Todo todo;
            if (line.matches(this.singleLineRegex)) {
                todo = new Todo(currentIndex + 1, currentIndex + 1, body);
            } else if (line.matches(this.multiLineRegex)) {
                todo = parseMultiLineTodo(lines, body);
            } else {
                continue;
            }

            setRemainingTodoFields(todo, header, path);
            todos.add(todo);
        }
        return todos;
    }

    /**
     * Parses multiple lines of the input file and extracts the TODO object.
     * @param lines Lines of the multi-line TODO.
     * @param body The TODO's body; could be empty
     * @return The extracted TODO object
     */
    private Todo parseMultiLineTodo(
        final List<String> lines,
        final String body
    ) {
        int start = currentIndex + 1;
        StringBuilder bodyBuilder = new StringBuilder(body);
        while (true) {
            String line = lines.get(++currentIndex);
            Matcher matcher = this.multiLinePattern.matcher(line);
            if (matcher.find()) {
                String bodyContinuation = matcher.group(3);

                if (bodyBuilder.length() > 0) {
                    bodyBuilder.append(" ");
                }
                bodyBuilder.append(bodyContinuation);

            } else {
                currentIndex--;
                break;
            }
        }
        return new Todo(start, currentIndex + 1, bodyBuilder.toString());
    }

    /**
     * Sets the remaining TODO fields: ticket ID, estimated time, and path.
     * @param todo The TODO object to update.
     * @param header The header, containing ticket ID and estimated time.
     * @param path The path of the file in which the TODO was found.
     */
    private void setRemainingTodoFields(
        final Todo todo,
        final String header,
        final String path
    ) {
        final String[] parts = header.split(":");

        todo.setTicketID(parts[0]);
        todo.setEstimatedTime(
            Integer.parseInt(
                parts[1].replaceAll("[A-Za-z]+", "")
            )
        );
        todo.setPath(path);
    }

    /**
     * Checks if a TODO is present in the given line and if so, returns
     * the index of its header, i.e. of the header's first character.
     * Header consists of the ticker number and the estimated time.
     *
     * @param line The source code line to check
     * @return Index of header or -1 if the line contains no TODO
     */
    private int getHeaderIndex(final String line) {
        int index = -1;
        for (final String todo : MARKERS.split("\\|")) {
            int todoIndex = line.indexOf(todo);
            if (todoIndex != -1) {
                index = todoIndex + todo.length();
                break;
            }
        }
        return index;
    }

}