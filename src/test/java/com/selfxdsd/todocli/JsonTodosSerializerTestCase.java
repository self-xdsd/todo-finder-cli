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

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonReader;
import java.io.File;
import java.io.IOException;
import java.net.URI;

/**
 * Unit tests for {@link JsonTodosSerializer}.
 * @author criske
 * @version $Id$
 * @since 0.0.1
 */
public final class JsonTodosSerializerTestCase {

    /**
     * JsonTodosSerializer should serialize todos.
     * @throws IOException If something goes wrong.
     */
    @Test
    public void shouldSerializeTodos() throws IOException {

        final TodoParser parser = new TodoParser();
        final TodosSerializer serializer = new JsonTodosSerializer();

        serializer.addAll(parser
                .parse("src/test/resources/RtImagesITCase.java"));

        final URI location = serializer.serialize();
        final JsonReader reader = Json
            .createReader(location.toURL().openStream());
        final JsonArray json = reader.readArray();
        reader.close();

        MatcherAssert.assertThat(json, Matchers.equalTo(
            Json.createArrayBuilder()
                .add(Json.createObjectBuilder()
                    .add("id", 888060502)
                    .add("author", "cristianpela")
                    .add("timestamp", "2021-01-18 12:29:17 +0200")
                    .add("start", 42)
                    .add("end", 42)
                    .add("originatingTicket", "#153")
                    .add("estimatedTime", 30)
                    .add("body", "Edit:Add integration tests for filters.")
                    .add("file", "src/test/resources/RtImagesITCase.java")
                    .build())
                .add(Json.createObjectBuilder()
                    .add("id", -2091730235)
                    .add("author", "cristianpela")
                    .add("timestamp", "2021-01-18 11:32:28 +0200")
                    .add("start", 69)
                    .add("end", 72)
                    .add("originatingTicket", "#187")
                    .add("estimatedTime", 30)
                    .add("body",
                        "Edit:To have multiple controlled images for"
                            + " filtering and not the ubuntu image dependency"
                            + " for this test will be nice to have the build"
                            + " Images implemented as described here:"
                            + " https://docs.docker.com/engine/api/v1.37/"
                            + "#operation/ImageBuild."
                    )
                    .add("file", "src/test/resources/RtImagesITCase.java")
                    .build())
                .build()
        ));

        //cleanup
        final boolean deleted = new File(location).delete();
        MatcherAssert.assertThat(deleted, Matchers.is(Boolean.TRUE));
    }
}
