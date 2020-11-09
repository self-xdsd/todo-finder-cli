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

import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link TodoParser}.
 * @version $Id$
 * @since 0.0.1
 */
public final class TodoParserTest {

    /**
     * Class RtImagesITCase should contain 2 TODOs.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void rtImagesITCaseExpectTwoReturnedTodos() throws IOException {
        List<Todo> todos = new TodoParser().parse(
            "src/test/resources/RtImagesITCase.java"
        );

        assertEquals(todos.size(), 2);

        Todo first = todos.get(0);
        Todo expectedFirst = new Todo(42, 42, "#153", 30);
        assertEquals(expectedFirst, first);

        Todo second = todos.get(1);
        Todo expectedSecond = new Todo(69, 72, "#187", 30);
        assertEquals(expectedSecond, second);
    }

    /**
     * Class LegalTodos should contain ten TODOs.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void legalTodosClassExpectExactlyTenTodos() throws IOException {
        final List<Todo> todos = new TodoParser().parse(
            "src/test/resources/LegalTodos.java"
        );
        assertEquals(10, todos.size());

        assertEquals(
            new Todo(1, 1, "#900", 90),
            todos.get(0)
        );
        assertEquals(
            new Todo(3, 3, "#111", 11),
            todos.get(1)
        );
        assertEquals(
            new Todo(10, 11, "#112", 12),
            todos.get(2)
        );
        assertEquals(
            new Todo(20, 35, "#113", 13),
            todos.get(3)
        );
        assertEquals(
            new Todo(41, 41, "#114", 14),
            todos.get(4)
        );
        assertEquals(
            new Todo(42, 42, "#115", 15),
            todos.get(5)
        );
        assertEquals(
            new Todo(50, 52, "#116", 16),
            todos.get(6)
        );
        assertEquals(
            new Todo(62, 62, "#117", 17),
            todos.get(7)
        );
        assertEquals(
            new Todo(63, 64, "#118", 18),
            todos.get(8)
        );
        assertEquals(
            new Todo(65, 65, "#119", 19),
            todos.get(9)
        );
    }

    /**
     * TODOs in class TodosWithBodies are parsed correctly.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void todoWithBodyExpectBodyToBeProperlyParsed() throws IOException {
        final List<Todo> todos = new TodoParser().parse(
            "src/test/resources/TodosWithBodies.java"
        );
        assertEquals(7, todos.size());

        assertEquals(
            "this is an example of a single-line todo.",
            todos.get(0).getBody()
        );
        assertEquals(
            "this is an example of a multi-line todo.",
            todos.get(1).getBody()
        );
        assertEquals(
            "small todo",
            todos.get(2).getBody()
        );
        assertEquals(
            "this todo should only be three lines long",
            todos.get(3).getBody()
        );
        assertEquals(
            "this body is in the next line...",
            todos.get(4).getBody()
        );
        assertEquals(
            "", todos.get(5).getBody()
        );
        assertEquals(
            "...body...",
            todos.get(6).getBody()
        );
    }

    /**
     * Class ConfigurationProperties has no TODOs.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void configurationPropertiesClassExpectNoTodos() throws IOException {
        final List<Todo> todos = new TodoParser().parse(
            "src/test/resources/ConfigurationPropertiesReportEndpoint.java"
        );
        assertEquals(0, todos.size());
    }

    /**
     * HashMap class has no todos.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void hashMapClassExpectNoTodos() throws IOException {
        final List<Todo> todos = new TodoParser().parse(
            "src/test/resources/HashMap.java"
        );
        assertEquals(0, todos.size());
    }

    /**
     * AbstractStringBuilder has no TODOs.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void abstractStringBuilderExpectNoTodos() throws IOException {
        final List<Todo> todos = new TodoParser().parse(
            "src/test/resources/AbstractStringBuilder.java"
        );
        assertEquals(0, todos.size());
    }

    /**
     * IllegalTodos has no TODOs.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void illegalTodosClassExpectNoTodos() throws IOException {
        final List<Todo> todos = new TodoParser().parse(
            "src/test/resources/IllegalTodos.java"
        );
        assertEquals(0, todos.size());
    }

    /**
     * PhonyClass has no TODOs.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void phonyClassExpectNoTodos() throws IOException {
        final List<Todo> todos = new TodoParser().parse(
            "src/test/resources/PhonyClass.java"
        );
        assertEquals(0, todos.size());
    }
}