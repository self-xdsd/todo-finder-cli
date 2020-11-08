package io.github.lukacupic.todocli;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class TodoParserTest {

    private TodoParser parser;
    private ClassLoader classLoader;

    @Before
    public void setUp() {
        parser = new TodoParser();
        classLoader = getClass().getClassLoader();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void whenParsingRtClass_ExpectTwoReturnedTodos() throws IOException {
        List<Todo> todos = parser.parse("src/test/resources/RtImagesITCase.java");

        assertEquals(todos.size(), 2);

        Todo todo1 = todos.get(0);
        Todo expectedTodo1 = new Todo(42, 42, "#153", "30min");
        assertEquals(expectedTodo1, todo1);

        Todo todo2 = todos.get(1);
        Todo expectedTodo2 = new Todo(69, 72, "#187", "30min");
        assertEquals(expectedTodo2, todo2);
    }

    @Test
    public void whenParsingLegalTodosClass_ExpectExactlyTenTodos() throws IOException {
        List<Todo> todos = parser.parse("src/test/resources/LegalTodos.java");
        assertEquals(10, todos.size());

        assertEquals(new Todo(1, 1, "#900", "90days"), todos.get(0));
        assertEquals(new Todo(3, 3, "#111", "11min"), todos.get(1));
        assertEquals(new Todo(10, 11, "#112", "12min"), todos.get(2));
        assertEquals(new Todo(20, 35, "#113", "13min"), todos.get(3));
        assertEquals(new Todo(41, 41, "#114", "14min"), todos.get(4));
        assertEquals(new Todo(42, 42, "#115", "15min"), todos.get(5));
        assertEquals(new Todo(50, 52, "#116", "16min"), todos.get(6));
        assertEquals(new Todo(62, 62, "#117", "17min"), todos.get(7));
        assertEquals(new Todo(63, 64, "#118", "18min"), todos.get(8));
        assertEquals(new Todo(65, 65, "#119", "19min"), todos.get(9));
    }

    @Test
    public void whenParsingConfigurationPropertiesClass_ExpectNoTodos() throws IOException {
        List<Todo> todos = parser.parse("src/test/resources/ConfigurationPropertiesReportEndpoint.java");
        assertEquals(0, todos.size());
    }

    @Test
    public void whenParsingHashMapClass_ExpectNoTodos() throws IOException {
        List<Todo> todos = parser.parse("src/test/resources/HashMap.java");
        assertEquals(0, todos.size());
    }

    @Test
    public void whenParsingAbstractStringBuilder_ExpectNoTodos() throws IOException {
        List<Todo> todos = parser.parse("src/test/resources/AbstractStringBuilder.java");
        assertEquals(0, todos.size());
    }

    @Test()
    public void whenParsingIllegalTodoClass_ExpectIllegalTodoException() throws IOException {
        List<Todo> todos = parser.parse("src/test/resources/IllegalTodos.java");
        assertEquals(0, todos.size());
    }

    @Test
    public void whenParsingPhonyClass_ExpectNoTodos() throws IOException {
        List<Todo> todos = parser.parse("src/test/resources/PhonyClass.java");
        assertEquals(0, todos.size());
    }
}