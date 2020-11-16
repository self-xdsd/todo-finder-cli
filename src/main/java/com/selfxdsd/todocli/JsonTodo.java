package com.selfxdsd.todocli;

import javax.json.Json;

/**
 * Todo as JsonObject.
 *
 * @author lukacupic
 */
public class JsonTodo extends AbstractJsonObject {

    /**
     * Ctor.
     *
     * @param todo Todo to be converted to JsonObject.
     */
    public JsonTodo(final Todo todo) {
        super(Json.createObjectBuilder()
                // @todo #4:5min Uncomment the line below once #10 is merged
//                .add("id", todo.getID())
                .add("start", todo.getStart())
                .add("end", todo.getEnd())
                .add("ticketID", todo.getTicketID())
                .add("estimatedTime", todo.getEstimatedTime())
                .add("body", todo.getBody())
                .build());
    }
}
