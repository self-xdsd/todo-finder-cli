package io.github.lukacupic.todocli;

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
 */
public class TodoVisitor extends SimpleFileVisitor<Path> {

    /**
     * The parser used to extract the TODOs.
     */
    private TodoParser parser;

    /**
     * Creates a new TodoVisitor object.
     */
    public TodoVisitor() {
        this.parser = new TodoParser();
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        return super.preVisitDirectory(dir, attrs);
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        return super.postVisitDirectory(dir, exc);
    }

    @Override
    public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
        String file = path.toString();
        if (!file.endsWith(".java")) return CONTINUE;

        List<Todo> todos = parser.parse(file);
        if (todos.size() == 0) return CONTINUE;

        System.out.printf("\nFound %d TODO objects in %s:\n", todos.size(), file);

        for (Todo todo : todos) {
            System.out.println(todo);
        }
        System.out.println();

        return CONTINUE;
    }
}
