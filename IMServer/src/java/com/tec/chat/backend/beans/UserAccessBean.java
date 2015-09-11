package com.tec.chat.backend.beans;

import com.tec.chat.backend.helper.References;
import java.util.HashMap;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;

/**
 * This class is a Singleton Bean that maintains all users' stateful session
 * beans. Clients call this Bean to get access to session beans for processing
 * business logic.
 * 
 * @author Rajiv Konkimalla
 */
@Singleton
public class UserAccessBean implements UserAccessBeanLocal {
    
    private HashMap<String,UserSessionBeanLocal> users;
    
    //Initialize properites
    public UserAccessBean() {
        users = new HashMap<>();
    }
    
    //return users list
    @Lock(LockType.READ)
    public HashMap<String,UserSessionBeanLocal> getUsers() {
        
        return users;
    }
    
    //Create an user entry, should the user is invoking a business method 
    //for the first time
    @Lock(LockType.READ)
    public UserSessionBeanLocal getUser(String userName) {
        
        if(!users.containsKey(userName)) {
            createUser(userName);
        }
        return users.get(userName);
    }            
    
    //Create an entry for user with a corresponding stateful session bean 
    @Lock(LockType.WRITE)
    private void createUser(String userName) {
        
        References references = new References();
        UserSessionBeanLocal userSessionBean = 
                references.lookupUserSessionBeanLocal();
        userSessionBean.setUser(userName);
        users.put(userName, userSessionBean);
        
    }
    
    
}
