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

import javax.json.Json;
import javax.json.JsonWriter;
import javax.json.stream.JsonGenerator;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Json Serializer for {@link Todo}.
 * @author criske
 * @version $Id$
 * @since 0.0.1
 */
public final class JsonTodosSerializer implements TodosSerializer {

    /**
     * Todos to be serialized.
     */
    private final List<Todo> todos;

    /**
     * Ctor.
     */
    public JsonTodosSerializer() {
        todos = new CopyOnWriteArrayList<>();
    }


    @Override
    public void add(final Todo... todo) {
        todos.addAll(List.of(todo));
    }

    @Override
    public URI serialize() {
        final URI location;
        try (final JsonWriter writer = Json
            .createWriterFactory(Map.of(JsonGenerator.PRETTY_PRINTING, true))
            .createWriter(new FileWriter(this.getFile(true)))) {
            writer.write(new JsonTodos(todos));
            location = this.getFile(false).toURI();
        } catch (final URISyntaxException | IOException exception) {
            throw new RuntimeException(exception);
        }
        return location;
    }

    /**
     * File were todos are going to be saved. This is in the same location
     * of the running application.
     *
     * @param deleteFirst If file exists, it will be deleted first.
     * @return File.
     * @throws URISyntaxException If something went wrong when getting path.
     * @throws IOException        If something when wrong while deleting the
     *                            file.
     */
    private File getFile(final boolean deleteFirst) throws URISyntaxException,
            IOException {
        //folder where application is running.
        final String parent = new File(JsonTodosSerializer.class
                .getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .toURI())
                .getParent();
        final File file = new File(parent, "todos.json");
        if (deleteFirst && file.exists()) {
            if (!file.delete()) {
                throw new IOException(
                    "Existent \"todos.json\" file could not be deleted."
                );
            }
        }
        return file;
    }
}