package io.github.lukacupic.todocli;

import java.io.IOException;
import java.util.List;

public class TodoDemo {

    public static void main(String[] args) throws IOException {
        TodoParser finder = new TodoParser();
        List<Todo> todos = finder.find("src/main/resources/RtImagesITCase.java");
        todos.forEach(System.out::println);
    }
}
