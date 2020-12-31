package com.selfxdsd.todocli;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the added ID in the {@link Todo} class.
 */
public final class TodoIdTest {

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
        Todo first = new Todo(1, 2, "#314", 30, "Hello");
        Todo second = new Todo(1, 2, "#314", 40, "Hello");

        Assert.assertNotEquals(first.getID(), second.getID());
    }

    /**
     * Checks if Todos with different ticket IDs will have different IDs.
     */
    @Test
    public void todosWithDifferentTicketIDsExpectDifferentIDs() {
        Todo first = new Todo(1, 2, "#314", 30, "Hello");
        Todo second = new Todo(1, 2, "#315", 30, "Hello");

        Assert.assertNotEquals(first.getID(), second.getID());
    }

    /**
     * Checks if two same Todos will have same IDs.
     */
    @Test
    public void todosWithSameTicketAndEstimationAndBodyExpectSameIDs() {
        Todo first = new Todo(1, 2, "#314", 30, "Hello");
        Todo second = new Todo(1, 2, "#314", 30, "Hello");

        Assert.assertEquals(first.getID(), second.getID());
    }

    /**
     * Checks if Todos with different bodies will be different.
     */
    @Test
    public void todosWithDifferentBodiesExpectNotEqual() {
        Todo first = new Todo(1, 2, "#314", 30, "Hello");
        Todo second = new Todo(1, 2, "#314", 30, "Olleh");

        Assert.assertNotEquals(first, second);
    }

    /**
     * Checks if Todos with different estimations will be different.
     */
    @Test
    public void todosWithDifferentEstimationsExpectNotEqual() {
        Todo first = new Todo(1, 2, "#314", 30, "Hello");
        Todo second = new Todo(1, 2, "#314", 40, "Hello");

        Assert.assertNotEquals(first, second);
    }

    /**
     * Checks if Todos with different ticket IDs will be different.
     */
    @Test
    public void todosWithDifferentTicketIDsExpectNotEqual() {
        Todo first = new Todo(1, 2, "#314", 30, "Hello");
        Todo second = new Todo(1, 2, "#315", 30, "Hello");

        Assert.assertNotEquals(first, second);
    }

    /**
     * Checks if same Todos will be equal.
     */
    @Test
    public void todosWithSameTicketAndEstimationAndBodyExpectEqual() {
        Todo first = new Todo(1, 2, "#314", 30, "Hello");
        Todo second = new Todo(1, 2, "#314", 30, "Hello");

        Assert.assertEquals(first, second);
    }
}
