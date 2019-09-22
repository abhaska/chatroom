package edu.udacity.java.nano.chat;

import org.springframework.stereotype.Component;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * WebSocket Server
 *
 * @see ServerEndpoint WebSocket Client
 * @see Session   WebSocket Session
 */

@Component
@ServerEndpoint(value = "/chat/{username}", encoders = {JsonEncoder.class})
public class WebSocketChatServer {

    /**
     * All chat sessions.
     */
    private static Map<Session, String> onlineSessions = new ConcurrentHashMap<>();
    private static Set<Session> chatEndpoints = new CopyOnWriteArraySet<>();

    private static void sendMessageToAll(String msg) {

    }

    private static void broadcast(Message message)
        throws IOException, EncodeException {
      JsonObject event = Json.createObjectBuilder()
          .add("username", message.getName())
          .add("msg", message.getVal())
          .add("onlineCount", message.getOnlineCount())
          .build();
      chatEndpoints.forEach(endpoint -> {
        try {
          endpoint.getBasicRemote().
              sendObject(event);
        } catch (IOException | EncodeException e) {
          e.printStackTrace();
        }
      });
    }

    /**
     * Open connection, 1) add session, 2) add user.
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        onlineSessions.put(session, username);
        chatEndpoints.add(session);
    }

    /**
     * Send message, 1) get username and session, 2) send message to all.
     */
    @OnMessage
    public void onMessage(Session session, String jsonStr) {
      Map<String,String> myMap = new HashMap<String, String>();
      ObjectMapper objectMapper = new ObjectMapper();
      try {
        myMap = objectMapper.readValue(jsonStr, HashMap.class);
      } catch (IOException e) {
        e.printStackTrace();
      }
      Message msg = new Message();
      msg.setName(myMap.get("username"));
      msg.setVal(myMap.get("msg"));
      msg.setOnlineCount(chatEndpoints.size());
      try {
        broadcast(msg);
      } catch (IOException | EncodeException e) {
        e.printStackTrace();
      }
    }

    /**
     * Close connection, 1) remove session, 2) update user.
     */
    @OnClose
    public void onClose(Session session) {
      chatEndpoints.remove(session);
    }

    /**
     * Print exception.
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

}
