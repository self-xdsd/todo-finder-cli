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
public class TodoParser {

    /**
     * The list of all TODO markers.
     */
    private static final String[] MARKERS = new String[]{"@todo", "TODO", "@fixme", "FIXME"};

    /**
     * The replacement label for MARKERS.
     */
    private static final String MARKERS_LABEL = "<MARKERS>";

    /**
     * Regex used for matching a single-line comment.
     */
    private static String SINGLE_LINE_REGEX = "^(\\s*//\\s*)(" + MARKERS_LABEL + ").*$";


    /**
     * Regex used for matching a multi-line comment.
     */
    private static String MULTI_LINE_REGEX = "^(\\s*[*]\\s*)(" + MARKERS_LABEL + ").*$";


    /**
     * Pattern used for matching the body of a TODO in a multi-line comment.
     */
    private static Pattern MULTI_LINE_PATTERN = Pattern.compile("(\\s*[*])(\\s\\s)(.*)");

    static {
        StringBuilder sb = new StringBuilder();
        for (String todo : MARKERS) {
            sb.append(todo).append("|");
        }
        String markersGroup = sb.substring(0, sb.length() - 1);

        SINGLE_LINE_REGEX = SINGLE_LINE_REGEX.replace(MARKERS_LABEL, markersGroup);
        MULTI_LINE_REGEX = MULTI_LINE_REGEX.replace(MARKERS_LABEL, markersGroup);
    }

    /**
     * Index of the currently parsed line.
     */
    private int currentIndex;

    /**
     * Creates a new TodoParser object.
     */
    public TodoParser() {
    }

    /**
     * Finds and returns a list of all TODOs found in the file given its path.
     */
    public List<Todo> parse(String path) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(path));
        List<Todo> todos = new ArrayList<>();

        for (currentIndex = 0; currentIndex < lines.size(); currentIndex++) {
            String line = lines.get(currentIndex);

            int headerIndex = getHeaderIndex(line);
            if (headerIndex == -1) continue;

            String[] parts = line.substring(headerIndex).trim().split("\\s", 2);
            if (parts[0].isEmpty()) continue;

            String header = parts[0].trim();
            String body = parts.length == 2 ? parts[1].trim() : "";

            Todo todo;
            if (line.matches(SINGLE_LINE_REGEX)) {
                todo = parseSingleLineTodo(body);

            } else if (line.matches(MULTI_LINE_REGEX)) {
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
     * Parses the current line of the input file and extracts the TODO object.
     *
     * @param body the TODO's body; could be empty
     * @return the extracted TODO object
     */
    private Todo parseSingleLineTodo(String body) {
        return new Todo(currentIndex + 1, currentIndex + 1, body);
    }

    /**
     * Parses multiple lines of the input file and extracts the TODO object.
     *
     * @param body the TODO's body; could be empty
     * @return the extracted TODO object
     */
    private Todo parseMultiLineTodo(List<String> lines, String body) {
        int start = currentIndex + 1;
        StringBuilder sb = new StringBuilder(body);

        while (true) {
            String line = lines.get(++currentIndex);

            Matcher m = MULTI_LINE_PATTERN.matcher(line);

            if (m.find()) {
                body = m.group(3);

                if (sb.length() > 0) {
                    sb.append(" ");
                }
                sb.append(body);

            } else {
                currentIndex--;
                break;
            }
        }

        return new Todo(start, currentIndex + 1, sb.toString());
    }

    /**
     * Sets the remaining TODO fields: ticket ID, estimated time, and path.
     *
     * @param todo   the TODO object to update
     * @param header the header, containing ticket ID and estimated time
     * @param path   the path of the file in which the TODO was found
     */
    private void setRemainingTodoFields(Todo todo, String header, String path) {
        String[] parts = header.split(":");

        todo.setTicketID(parts[0]);
        todo.setEstimatedTime(getEstimatedTimeAsInt(parts[1]));
        todo.setPath(path);
    }

    /**
     * Checks if a TODO is present in the given line and if so, returns
     * the index of its header, i.e. of the header's first character.
     * Header consists of the ticker number and the estimated time.
     *
     * @param line the source code line to check
     * @return index of header or -1 if the line contains no TODO
     */
    private static int getHeaderIndex(String line) {
        for (String todo : MARKERS) {
            int todoIndex = line.indexOf(todo);
            if (todoIndex != -1) {
                return todoIndex + todo.length();
            }
        }
        return -1;
    }

    /**
     * Removes any trailing characters (such as "min" or "minutes") and returns the
     * proper integer value of the estimated time
     *
     * @param estimatedTime the estimated time, as extracted from the header
     * @return estimated time in minutes
     */
    private static int getEstimatedTimeAsInt(String estimatedTime) {
        return Integer.parseInt(estimatedTime.replaceAll("[A-Za-z]+", ""));
    }
}