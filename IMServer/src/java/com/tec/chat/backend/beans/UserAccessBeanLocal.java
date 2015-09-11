
package com.tec.chat.backend.beans;

import java.util.HashMap;
import javax.ejb.Local;

/**
 * This interface holds the methods for the UserAccessBean bean
 * 
 * @author Rajiv Konkimalla
 */
@Local
public interface UserAccessBeanLocal {
    
    public HashMap<String,UserSessionBeanLocal> getUsers();
    public UserSessionBeanLocal getUser(String userName);
    
}
