package io.github.lukacupic.todocli;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

import static java.nio.file.FileVisitResult.CONTINUE;

public class TodoVisitor extends SimpleFileVisitor<Path> {

    @Override
    public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
        String file = path.toString();
        if (!file.endsWith(".java")) return CONTINUE;

        List<Todo> todos = TodoParser.parse(file);
        if (todos.size() == 0) return CONTINUE;

        System.out.printf("Found %d TODO objects in %s\n", todos.size(), file);

        return CONTINUE;
    }
}
