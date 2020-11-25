package com.selfxdsd.todocli;

import org.junit.Assert;
import org.junit.Test;

import javax.json.JsonObject;

/**
 * Unit tests for {@link JsonTodo}.
 *
 * @version $Id$
 * @since 0.0.1
 */
public final class JsonTodoTest {

    /**
     * Existing Todo object should be successfully converted into Json.
     */
    @Test
    public void whenCallingCtorOfJsonTodoExpectJsonObject() {
        Todo todo = new Todo(1, 2, "#1138", 60);

        JsonTodo jsonTodo = new JsonTodo(todo);
        JsonObject jsonObject = jsonTodo.asJsonObject();

        Assert.assertEquals(jsonObject.getInt("start"), 1);
        Assert.assertEquals(jsonObject.getInt("end"), 2);
        Assert.assertEquals(jsonObject.getString("originatingTicket"),
            "#1138");
        Assert.assertEquals(jsonObject.getInt("estimatedTime"), 60);
        Assert.assertEquals(jsonObject.getString("body"), "");
    }
}
