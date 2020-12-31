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

import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Unit tests for {@link TodoVisitor}.
 *
 * @author criske
 * @version $Id$
 * @since 0.0.1
 */
public final class TodoVisitorTestCase {

    /**
     * TodoVisitor should call {@link TodosSerializer#serialize()} once
     * the files and sub-folders scanning has finished.
     *
     * @throws IOException        If something goes wrong.
     * @throws URISyntaxException If something goes wrong.
     */
    @Test
    public void callsSerializeOnce() throws IOException, URISyntaxException {
        final TodosSerializer serializer = Mockito.mock(TodosSerializer.class);
        final URI root = Objects.requireNonNull(
                TodoVisitorTestCase.class
                        .getClassLoader()
                        .getResource(".")
        ).toURI();
        Files.walkFileTree(Path.of(root), new TodoVisitor(serializer, null));

        Mockito.verify(serializer, Mockito.times(1))
                .serialize();
        Mockito.verify(serializer, Mockito.atLeast(1))
                .addAll(Mockito.anyCollection());
    }

    /**
     * TodoVisitor allows js files to be parsed for todos.
     *
     * @throws IOException If something goes wrong.
     */
    @Test
    public void shouldAllowJsFilesToBeParsedForTodos() throws IOException {
        final TodosSerializer serializer = Mockito.mock(TodosSerializer.class);
        Files.walkFileTree(Path.of("src/test/resources/js"),
                new TodoVisitor(serializer, null));

        Mockito.verify(serializer, Mockito.times(1))
                .serialize();
        Mockito.verify(serializer, Mockito.atLeast(1))
                .addAll(Mockito.anyCollection());
    }
}
