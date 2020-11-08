package io.github.lukacupic.todocli;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TodoCLI {

    public static void main(String[] args) {
        try {
            Files.walkFileTree(Paths.get("."), new TodoVisitor());
        } catch (IOException e) {
            System.err.println("Could not walk file tree!");
        }
    }
}
