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
 * This class is a Message Driven Bean that receives messages from chat
 * queue. This is used to accept user's messages and ultimately sends message
 * to friend. 
 * 
 * @author Rajiv Konkimalla
 */
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationLookup", 
            propertyValue = "java:/jms/queue/chat"),
    @ActivationConfigProperty(propertyName = "destinationType",
            propertyValue = "javax.jms.Queue"),
    @ActivationConfigProperty(propertyName = "connectionFactoryLookup",
            propertyValue = "java:/ConnectionFactory")
})
public class MessageQueueMDB implements MessageListener {
    
    @EJB
    private UserAccessBeanLocal userAccessBean;
       
    
    public MessageQueueMDB() {
    }
    
    //Process the message containing user,friend,message,timestamp to send
    //chat message to friend.
    public void onMessage(Message message) {
        
        ObjectMessage objMsg = null;
        ChatMessage msgDetails = null;
        
        try {
            System.out.println("chat queue");
            if (message instanceof ObjectMessage) {
                objMsg = (ObjectMessage) message;
                msgDetails = (ChatMessage) objMsg.getObject();
                            
                processMessage(msgDetails);
                System.out.println("Received Message from chat queue: " + 
                        msgDetails.getMessageInfo());
            } else {
                System.out.println("Message of wrong type at chat queue: " + 
                        message.getClass().getName());
            }
            
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
        
    }
    
    //Call friend stateful bean and send message comprising chat message, which
    //user sent it and timestamp    
    private void processMessage(ChatMessage msgInfo) {

        ChatMessage msgDetails = new ChatMessage();
        msgDetails.setFromUser(msgInfo.getFromUser());
        msgDetails.setMessageInfo(msgInfo.getMessageInfo());
        msgDetails.setTimeStamp(msgInfo.getTimeStamp());            
            
        userAccessBean.getUser(msgInfo.getToFriend()).addMessage(msgDetails);
                
    }
    
    
}
