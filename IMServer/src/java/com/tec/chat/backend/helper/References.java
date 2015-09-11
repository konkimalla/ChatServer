
package com.tec.chat.backend.helper;

import com.tec.chat.backend.beans.UserAccessBeanLocal;
import com.tec.chat.backend.beans.UserSessionBeanLocal;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * This class provides access to beans. Clients call this bean to get access
 * to stateful session beans.
 * 
 * @author Rajiv Konkimalla
 */
public class References {
    
    //Return UserSessionBean's local interface
    public UserSessionBeanLocal lookupUserSessionBeanLocal() {
        try {
            Context c = new InitialContext();
            return (UserSessionBeanLocal) c.lookup(
                    "java:global/IMServer/UserSessionBean!com.tec.chat.backend.beans.UserSessionBeanLocal");
        } catch (NamingException ne) {
            System.out.println("lookupUserSessionBeanLocal:exception");
            throw new RuntimeException(ne);
        }
    }
    
    
    //Return UserAccessBean's local interface
    public UserAccessBeanLocal lookupUserAccessBeanLocal() {
        try {
            Context c = new InitialContext();
            return (UserAccessBeanLocal) c.lookup(
                    "java:global/IMServer/UserAccessBean!com.tec.chat.backend.beans.UserAccessBeanLocal");
        } catch (NamingException ne) {
            System.out.println("lookupUserAccessBeanLocal:exception");
            throw new RuntimeException(ne);
        }
    }  
    
    
}
