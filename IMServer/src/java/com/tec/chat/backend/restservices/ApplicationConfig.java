package com.tec.chat.backend.restservices;

import java.util.HashSet;
import java.util.Set;


/**
 * This class registers rest web services.
 * 
 * @author Rajiv Konkimalla
 */
public class ApplicationConfig extends javax.ws.rs.core.Application {

    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<Class<?>>();
        classes.add(com.tec.chat.backend.restservices.MessageResource.class);
        classes.add(com.tec.chat.backend.restservices.MessagesResource.class);
        return classes;
    }
    
}