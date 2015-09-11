package com.tec.chat.backend.beans;

import com.tec.chat.backend.data.ChatMessage;
import com.tec.chat.backend.data.ChatMessages;
import java.util.List;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 * This class is a Message Driven Bean that receives messages from presence
 * queue. This is used to accept user's presence status and ultimately updates
 * status to all of its friends.
 * 
 * @author Rajiv Konkimalla
 */
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationLookup",
            propertyValue = "java:/jms/queue/presence"),
    @ActivationConfigProperty(propertyName = "destinationType", 
            propertyValue = "javax.jms.Queue"),
    @ActivationConfigProperty(propertyName = "connectionFactoryLookup",
            propertyValue = "java:/ConnectionFactory")
})
public class PresenceQueueMDB implements MessageListener {
    
    @EJB
    private UserAccessBeanLocal userAccessBean;
    
    public PresenceQueueMDB() {
    }
    
    //Process the message containing friends list,presence status to send
    //presence status to all friends.
    public void onMessage(Message message) {

        ObjectMessage objMsg = null;
        ChatMessages msgFriends = null;

        try {
            System.out.println("presence queue");
            if (message instanceof ObjectMessage) {
                objMsg = (ObjectMessage) message;
                msgFriends = (ChatMessages) objMsg.getObject();
                processMessage(msgFriends);
                
            } else {
                System.out.println("Message of wrong type at presence queue: " +
                        message.getClass().getName());
            }

        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
        
    }
    
    //Call friends' stateful bean and update friends' presence status    
    private void processMessage(ChatMessages msgInfo) {
        
        List<ChatMessage> messages = msgInfo.getMessage();
        for(int i=0; i<messages.size(); i++) {

            userAccessBean.getUser
                    (messages.get(i).getToFriend()).updateFriendPresence(
                     messages.get(i).getFromUser(), msgInfo.getPresence());                    
        }
                
    }
    
}
