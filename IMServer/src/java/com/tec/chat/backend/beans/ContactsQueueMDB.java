package com.tec.chat.backend.beans;

import com.tec.chat.backend.data.ChatMessage;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 * This class is a Message Driven Bean that receives messages from contacts
 * queue. This is used to accept friend requests and ultimately add contact to
 * user. 
 * 
 * @author Rajiv Konkimalla
 */
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationLookup", 
            propertyValue = "java:/jms/queue/contacts"),
    @ActivationConfigProperty(propertyName = "destinationType", 
            propertyValue = "javax.jms.Queue"),
    @ActivationConfigProperty(propertyName = "connectionFactoryLookup", 
            propertyValue = "java:/ConnectionFactory")
})
public class ContactsQueueMDB implements MessageListener {
    
    @EJB
    private UserAccessBeanLocal userAccessBean;

    public ContactsQueueMDB() {
    }
    
    //Process the message containing user,friend to add each other as contact.
    public void onMessage(Message message) {
        
        ObjectMessage objMsg = null;
        ChatMessage msgDetails = null;
        
        try {
            System.out.println("contacts queue");
            if (message instanceof ObjectMessage) {
                objMsg = (ObjectMessage) message;
                msgDetails = (ChatMessage) objMsg.getObject();
                            
                processMessage(msgDetails);
                System.out.println("Received Message from contacts queue: " +
                        msgDetails.getFromUser() + " sent friend request to  " +
                        msgDetails.getToFriend());
            } else {
                System.out.println("Message of wrong type at contacts queue: " +
                        message.getClass().getName());
            }
            
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
        
    }
    
    //Call both user and friend stateful beans and update their contact lists
    //with friend information
    private void processMessage(ChatMessage msgInfo) {
 
        //friend adding user
        userAccessBean.getUser(msgInfo.getToFriend()).addContact(
                msgInfo.getFromUser());

        //user adding friend
        userAccessBean.getUser(msgInfo.getFromUser()).addContact(
                msgInfo.getToFriend());
        
    }

    
}
