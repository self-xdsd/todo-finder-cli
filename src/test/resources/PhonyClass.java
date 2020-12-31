package io.github.lukacupic.todocli;

/**
 * This phony class contains phony TODO #187:30min objects which should NOT be interpreted as TODO objects.
 * They are simply here to confuse the TodoParser. They should not succeed in their intent.
 */
public class PhonyClass {

    // keep in mind that neither @todo #187:30min nor @fixme #187:30min
    // nor TODO #187:30min nor FIXME #187:30min should be triggered by this comment

    public void method1() {
        // this should not trigger the TODO #187:30min parser because this is not an actual todo
    }

    /**
     * Neither of these should trigger the todo #187:30min parser.
     */
    public void method2() {
        String temp = "//@todo #187:30min this shouldn't trigger the parser";
        String temp2 = "// @todo #187:30min neither should this";
        String temp3 = "*@todo #187:30min nor this";
        String temp4 = "* @todo #187:30min nor this";
    }

    /**
     * This should not trigger the @todo parser becauase it's not an actual todo.
     */
    public void method3() {
        "// @\\todo #187:30min this should not trigger the todo parser because this is not an actual todo".trim();
    }

    /**
     * This should not trigger the TODO
     */
    public void method4() {
        // nothing
    }
}
