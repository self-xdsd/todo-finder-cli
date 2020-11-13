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
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

import static java.nio.file.FileVisitResult.CONTINUE;

/**
 * Performs the visit of the given directory structure and prints a list of
 * all extracted TODOs.
 * @version $Id$
 * @since 0.0.1
 */
public final class TodoVisitor extends SimpleFileVisitor<Path> {

    /**
     * The parser used to extract the TODOs.
     */
    private final TodoParser parser;

    /**
     * Todos serializer.
     */
    private final TodosSerializer serializer;

    /**
     * Root path.
     */
    private Path root;

    /**
     * Creates a new TodoVisitor object.
     * @param serializer Todos serializer.
     */
    public TodoVisitor(final TodosSerializer serializer) {
        this.serializer = serializer;
        this.parser = new TodoParser();
    }

    @Override
    public FileVisitResult preVisitDirectory(
        final Path dir, final BasicFileAttributes attrs
    ) throws IOException {
        if (root == null) {
            root = dir;
        }
        return super.preVisitDirectory(dir, attrs);
    }

    @Override
    public FileVisitResult postVisitDirectory(
        final Path dir, final IOException exc
    ) throws IOException {
        if (dir.equals(root)) {
            // scanning root has finished.
            this.serializer.serialize();
        }
        return super.postVisitDirectory(dir, exc);
    }

    @Override
    public FileVisitResult visitFile(
        final Path path,
        final BasicFileAttributes attrs
    ) throws IOException {
        final String file = path.toString();
        if (file.endsWith(".java")) {
            final List<Todo> todos = parser.parse(file);
            if (todos.size() > 0) {
                System.out.printf(
                    "\nFound %d TODOs in %s:\n",
                    todos.size(), file
                );
                for (final Todo todo : todos) {
                    System.out.println(todo);
                }
                System.out.println();
                this.serializer.addAll(todos);
            }
        }
        return CONTINUE;
    }
}
