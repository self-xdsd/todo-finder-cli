package com.selfxdsd.todocli;

import org.junit.Assert;
import org.junit.Test;

public class TodoIDTest {

    // ID tests

    @Test
    public void todosWithDifferentBodiesExpectDifferentIDs() {
        Todo todo1 = new Todo(1, 2, "#314", 30, "Hello");
        Todo todo2 = new Todo(1, 2, "#314", 30, "Olleh");

        Assert.assertNotEquals(todo1.getID(), todo2.getID());
    }

    @Test
    public void todosWithDifferentEstimationsExpectDifferentIDs() {
        Todo todo1 = new Todo(1, 2, "#314", 30, "Hello");
        Todo todo2 = new Todo(1, 2, "#314", 40, "Hello");

        Assert.assertNotEquals(todo1.getID(), todo2.getID());
    }

    @Test
    public void todosWithDifferentTicketIDsExpectDifferentIDs() {
        Todo todo1 = new Todo(1, 2, "#314", 30, "Hello");
        Todo todo2 = new Todo(1, 2, "#315", 30, "Hello");

        Assert.assertNotEquals(todo1.getID(), todo2.getID());
    }

    @Test
    public void todosWithSameTicketAndEstimationAndBodyExpectSameIDs() {
        Todo todo1 = new Todo(1, 2, "#314", 30, "Hello");
        Todo todo2 = new Todo(1, 2, "#314", 30, "Hello");

        Assert.assertEquals(todo1.getID(), todo2.getID());
    }

    // Equals tests

    @Test
    public void todosWithDifferentBodiesExpectNotEqual() {
        Todo todo1 = new Todo(1, 2, "#314", 30, "Hello");
        Todo todo2 = new Todo(1, 2, "#314", 30, "Olleh");

        Assert.assertNotEquals(todo1, todo2);
    }

    @Test
    public void todosWithDifferentEstimationsExpectNotEqual() {
        Todo todo1 = new Todo(1, 2, "#314", 30, "Hello");
        Todo todo2 = new Todo(1, 2, "#314", 40, "Hello");

        Assert.assertNotEquals(todo1, todo2);
    }

    @Test
    public void todosWithDifferentTicketIDsExpectNotEqual() {
        Todo todo1 = new Todo(1, 2, "#314", 30, "Hello");
        Todo todo2 = new Todo(1, 2, "#315", 30, "Hello");

        Assert.assertNotEquals(todo1, todo2);
    }

    @Test
    public void todosWithSameTicketAndEstimationAndBodyExpectEqual() {
        Todo todo1 = new Todo(1, 2, "#314", 30, "Hello");
        Todo todo2 = new Todo(1, 2, "#314", 30, "Hello");

        Assert.assertEquals(todo1, todo2);
    }
}