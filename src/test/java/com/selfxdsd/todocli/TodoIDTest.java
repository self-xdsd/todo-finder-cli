package com.selfxdsd.todocli;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the added ID in the {@link Todo} class.
 */
public final class TodoIDTest {

    // ID tests

    /**
     * Checks if Todos with different bodies will have different IDs.
     */
    @Test
    public void todosWithDifferentBodiesExpectDifferentIDs() {
        Todo todoFirst = new Todo(1, 2, "#314", 30, "Hello");
        Todo todoSecond = new Todo(1, 2, "#314", 30, "Olleh");

        Assert.assertNotEquals(todoFirst.getID(), todoSecond.getID());
    }

    /**
     * Checks if Todos with different estimations will have different IDs.
     */
    @Test
    public void todosWithDifferentEstimationsExpectDifferentIDs() {
        Todo todo1 = new Todo(1, 2, "#314", 30, "Hello");
        Todo todo2 = new Todo(1, 2, "#314", 40, "Hello");

        Assert.assertNotEquals(todo1.getID(), todo2.getID());
    }

    /**
     * Checks if Todos with different ticket IDs will have different IDs.
     */
    @Test
    public void todosWithDifferentTicketIDsExpectDifferentIDs() {
        Todo todo1 = new Todo(1, 2, "#314", 30, "Hello");
        Todo todo2 = new Todo(1, 2, "#315", 30, "Hello");

        Assert.assertNotEquals(todo1.getID(), todo2.getID());
    }

    /**
     * Checks if two same Todos will have same IDs.
     */
    @Test
    public void todosWithSameTicketAndEstimationAndBodyExpectSameIDs() {
        Todo todo1 = new Todo(1, 2, "#314", 30, "Hello");
        Todo todo2 = new Todo(1, 2, "#314", 30, "Hello");

        Assert.assertEquals(todo1.getID(), todo2.getID());
    }

    // Equals tests

    /**
     * Checks if Todos with different bodies will be different.
     */
    @Test
    public void todosWithDifferentBodiesExpectNotEqual() {
        Todo todo1 = new Todo(1, 2, "#314", 30, "Hello");
        Todo todo2 = new Todo(1, 2, "#314", 30, "Olleh");

        Assert.assertNotEquals(todo1, todo2);
    }

    /**
     * Checks if Todos with different estimations will be different.
     */
    @Test
    public void todosWithDifferentEstimationsExpectNotEqual() {
        Todo todo1 = new Todo(1, 2, "#314", 30, "Hello");
        Todo todo2 = new Todo(1, 2, "#314", 40, "Hello");

        Assert.assertNotEquals(todo1, todo2);
    }

    /**
     * Checks if Todos with different ticket IDs will be different.
     */
    @Test
    public void todosWithDifferentTicketIDsExpectNotEqual() {
        Todo todo1 = new Todo(1, 2, "#314", 30, "Hello");
        Todo todo2 = new Todo(1, 2, "#315", 30, "Hello");

        Assert.assertNotEquals(todo1, todo2);
    }

    /**
     * Checks if same Todos will be equal.
     */
    @Test
    public void todosWithSameTicketAndEstimationAndBodyExpectEqual() {
        Todo todo1 = new Todo(1, 2, "#314", 30, "Hello");
        Todo todo2 = new Todo(1, 2, "#314", 30, "Hello");

        Assert.assertEquals(todo1, todo2);
    }
}