package com.tec.chat.backend.restservices;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.enterprise.context.RequestScoped;

/**
 * REST Web Service - This service is used to invoke HTTP GET method to
 * process a particular resource.
 *
 * @author Rajiv Konkimalla
 */
@RequestScoped
public class MessageResource {

    private String id;

    /**
     * Creates a new instance of MessageResource
     */
    private MessageResource(String id) {
        this.id = id;
    }

    /**
     * Get instance of the MessageResource
     */
    public static MessageResource getInstance(String id) {
        // The user may use some kind of persistence mechanism
        // to store and restore instances of MessageResource class.
        return new MessageResource(id);
    }

    /**
     * Retrieves representation of an instance of 
     * com.tec.chat.backend.restservices.MessageResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("text/plain")
    public String getText() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of MessageResource
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("text/plain")
    public void putText(String content) {
    }

    /**
     * DELETE method for resource MessageResource
     */
    @DELETE
    public void delete() {
    }
}
