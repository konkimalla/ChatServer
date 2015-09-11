package com.tec.chat.backend.restservices;

import com.tec.chat.backend.beans.UserAccessBeanLocal;
import com.tec.chat.backend.data.ChatMessage;
import com.tec.chat.backend.data.ChatMessages;
import com.tec.chat.backend.helper.Constants;
import com.tec.chat.backend.helper.References;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.POST;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

/**
 * REST Web Service - This service is used to invoke HTTP GET method to
 * process resources.
 *
 * @author Rajiv Konkimalla
 */
@Path("/ms")
@RequestScoped
public class MessagesResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of MessagesResource
     */
    public MessagesResource() {
    }

    /**
     * Retrieves representation of an instance of 
     * com.tec.chat.backend.restservices.MessagesResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("text/plain")
    public String getText(
            @QueryParam("username") String user,
            @QueryParam("message") String message,
            @QueryParam("friend") String friend,
            @QueryParam("presence") String presence,
            @QueryParam("operation") String operation)               
    {
        References references = new References();
        UserAccessBeanLocal userAccessBeanLocal;
        userAccessBeanLocal = references.lookupUserAccessBeanLocal();
        String result = null;
                
        try { 

            //Invoke business method based on operation
            switch(Integer.parseInt(operation)) {
                
                case Constants.SEND_FRIEND_REQUEST:
                    
                    userAccessBeanLocal.getUser(user).sendContactRequest(friend);
                    result = "success";        
                    break;                

                case Constants.SEND_MESSAGE_REQUEST :

                    userAccessBeanLocal.getUser(user).sendMessageRequest(message, user , friend);
                    result = "success";
                    break;

                case Constants.SEND_PRESENCESTATUS_REQUEST:
                    
                    userAccessBeanLocal.getUser(user).sendPresenceRequest(
                            Integer.parseInt(presence));
                    result = "success"; 
                    break;

                case Constants.OBTAIN_ALLUSER_INFORMATION :    
                    
                    StringBuilder displayMsgs = new StringBuilder();
                    HashMap<String, ChatMessages> contacts = null;
                    List<ChatMessage> messages = null;
                    Iterator<String> userNames = userAccessBeanLocal.getUsers().keySet().iterator();
                    String tempUserName = null;
                    String tempFriendName = null;
                    //Proceed to get information from all users
                    while(userNames.hasNext()) {
                                        
                        tempUserName = userNames.next();
                        displayMsgs.append("User ");
                        displayMsgs.append(tempUserName);
                        displayMsgs.append(", presence status ");
                        displayMsgs.append(userAccessBeanLocal.getUser(tempUserName).getUserPresence());
                        displayMsgs.append(", contacts:");
                        displayMsgs.append("\n");                        
                    
                        contacts = userAccessBeanLocal.getUser(tempUserName).getContacts();
                        Iterator<String> friendNames = contacts.keySet().iterator();
                        //Proceed to get information from user's friends
                        while(friendNames.hasNext()) {
                            
                            tempFriendName = friendNames.next();
                            messages = contacts.get(tempFriendName).getMessage();

                            displayMsgs.append("   ");
                            displayMsgs.append("Friend ");
                            displayMsgs.append(tempFriendName);
                            displayMsgs.append(", presence status ");
                            displayMsgs.append(contacts.get(tempFriendName).getPresence());
                            displayMsgs.append(", messages :");
                            displayMsgs.append("\n");                        
                            
                            //Proceed to gather all friend's messages that
                            //have been sent by user
                            for(int i=0; i<messages.size(); i++) {
                                displayMsgs.append("     ");
                                displayMsgs.append(messages.get(i).getFromUser());
                                displayMsgs.append(" sent chat message - "  );
                                displayMsgs.append(messages.get(i).getMessageInfo());
                                displayMsgs.append(" : at "  );
                                displayMsgs.append(messages.get(i).getTimeStamp().getTime().toString());
                                displayMsgs.append("\n");
                            }
                            displayMsgs.append("\n");
                                                        
                        }
                        
                        displayMsgs.append("\n\n");                        
                                                
                    }
                    
                    result = displayMsgs.toString();      
                    break;                
                    
                default:
                    result = "Wrong Operation";
                    break;

            }     
        
        } catch(Exception ex) {
            ex.printStackTrace();
            result = ex.getMessage();
        }
        
        return result;   
    }

    /**
     * POST method for creating an instance of MessageResource
     * @param content representation for the new resource
     * @return an HTTP response with content of the created resource
     */
    @POST
    @Consumes("text/plain")
    @Produces("text/plain")
    public Response postText(String content) {
        //TODO
        return Response.created(context.getAbsolutePath()).build();
    }

    /**
     * Sub-resource locator method for {id}
     */
    @Path("{id}")
    public MessageResource getMessageResource(@PathParam("id") String id) {
        return MessageResource.getInstance(id);
    }
}
