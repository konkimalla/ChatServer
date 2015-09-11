package com.tec.chat.backend.beans;

import com.tec.chat.backend.data.ChatMessage;
import com.tec.chat.backend.data.ChatMessages;
import com.tec.chat.backend.helper.Constants;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import javax.annotation.Resource;
import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSRuntimeException;
import javax.jms.Queue;

/**
 * This class is a Stateful Session Bean that maintains user's state and 
 * provides business logic.
 * 
 * @author Rajiv Konkimalla
 */
@Stateful
public class UserSessionBean implements UserSessionBeanLocal {

    @Inject @JMSConnectionFactory("java:/ConnectionFactory") 
    private JMSContext context;
    
    @Resource(lookup = "java:/jms/queue/chat")
    private Queue chatQueue;
    
    @Resource(lookup = "java:/jms/queue/contacts")
    private Queue contactsQueue;
    
    @Resource(lookup = "java:/jms/queue/presence")
    private Queue presenceQueue;
    
    private String user;
    private int userPresence = Constants.ONLINE;
    private ChatMessages messages;
    private HashMap<String, ChatMessages> contacts;

    //Send message comprising from user, to friend, chat message, timestamp to
    //JMS chat queue.
    @Override
    public void sendMessageRequest(String message, String fromUser, String toFriend) {
                
        try {
    
            ChatMessage msgDetails = new ChatMessage();
            msgDetails.setFromUser(fromUser);
            msgDetails.setToFriend(toFriend);
            msgDetails.setMessageInfo(message);
            msgDetails.setTimeStamp(Calendar.getInstance());
                        
            context.createProducer().send(chatQueue, msgDetails);           
        } catch (JMSRuntimeException ex) {
            ex.printStackTrace();
            System.out.println("Error during the delivery of chat message");
        }
        
    }
    
    //Send message comprising from user, to friend to JMS contacts queue.
    @Override
    public void sendContactRequest(String friend) {
        try {

            ChatMessage msgDetails = new ChatMessage();
            msgDetails.setFromUser(user);
            msgDetails.setToFriend(friend);
                        
            context.createProducer().send(contactsQueue, msgDetails);        
        } catch (JMSRuntimeException ex) {
            ex.printStackTrace();
            System.out.println("Error during the delivery of friend request");
        }
        
    }
    
    //Send message comprising from user, friends' list, presence to
    //JMS presence queue.
    @Override
    public void sendPresenceRequest(int presence) {
        
        try {
                        
            ChatMessage msgDetails;
            //Proceed only if this user has friends
            if(!contacts.isEmpty()) {
            
                ChatMessages friends = new ChatMessages(presence);
                Iterator<String> friendNames = contacts.keySet().iterator();
                while(friendNames.hasNext()) {
                    
                    msgDetails = new ChatMessage();
                    msgDetails.setFromUser(user);
                    msgDetails.setToFriend(friendNames.next());
                    friends.addMessage(msgDetails);

                }
                setUserPresence(presence);
                context.createProducer().send(presenceQueue, friends);                          
            }
            
        } catch (JMSRuntimeException ex) {
            ex.printStackTrace();
            System.out.println("Error during the delivery of presence request");
        }        
        
    }
    
    //Initialize properites
    @Override
    public void setUser(String user) {
       
        this.user = user;
        contacts = new HashMap();
        System.out.println("set user:" + this.user);
    }

    //returns user
    @Override
    public String getUser() {
        return this.user;
    }

    //Create an entry for friend to accomodate chat messages and presence
    @Override
    public void addContact(String friend) {
        
        //Entry needs to be created only once
        if(!contacts.containsKey(friend)) {
          
            messages = new ChatMessages(Constants.ONLINE);
            contacts.put(friend, messages);
        }
        
    }

    //Add a new chat message to user
    @Override
    public void addMessage(ChatMessage msgInfo) {
        
        contacts.get(msgInfo.getFromUser()).addMessage(msgInfo);
        
    }

    /**
     * @return the contacts
     */
    public HashMap<String, ChatMessages> getContacts() {
        return contacts;
    }

    //Obtain friend information and update its presence
    @Override
    public void updateFriendPresence(String friend, int presence) {
        
        contacts.get(friend).setPresence(presence);
        
    }

    /**
     * @return the userPresence
     */
    public int getUserPresence() {
        return userPresence;
    }

    /**
     * @param userPresence the userPresence to set
     */
    public void setUserPresence(int userPresence) {
        this.userPresence = userPresence;
    }
   
}
