package com.tec.chat.backend.beans;

import com.tec.chat.backend.data.ChatMessage;
import com.tec.chat.backend.data.ChatMessages;
import java.util.HashMap;
import javax.ejb.Local;

/**
 * This interface holds the methods for the UserSessionBean bean
 * 
 * @author Rajiv Konkimalla
 */
@Local
public interface UserSessionBeanLocal {

    void sendMessageRequest(String message, String fromUser, String toFriend);

    void setUser(String user);
    
    String getUser();

    void addContact(String friend);

    void sendContactRequest(String friend);

    void addMessage(ChatMessage msgInfo);
    
    HashMap<String, ChatMessages> getContacts();

    void sendPresenceRequest(int presence);

    void updateFriendPresence(String friend, int presence);
    
    int getUserPresence();
}
