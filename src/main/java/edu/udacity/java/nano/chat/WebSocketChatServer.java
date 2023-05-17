package edu.udacity.java.nano.chat;

import com.alibaba.fastjson.JSON;
import jakarta.websocket.*;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import org.springframework.stereotype.Component;

import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * WebSocket Server
 *
 * @see ServerEndpoint WebSocket Client
 * @see Session   WebSocket Session
 */

@Component
@ServerEndpoint("/chat/{username}")
public class WebSocketChatServer {

    private static final String ENTER = "ENTER";
    private static final String CHAT = "CHAT";
    private static final String LEAVE = "LEAVE";

    /**
     * All chat sessions.
     */
    private static Map<String, Session> onlineSessions = new ConcurrentHashMap<>();

    private static void sendMessageToAll(Message msg) throws IOException {

        for(Map.Entry<String, Session> user : onlineSessions.entrySet()) {
            Session session = user.getValue();

            session.getBasicRemote().sendText(JSON.toJSONString(msg));
        }
    }

    /**
     * Open connection, 1) add session, 2) add user.
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) throws IOException {
        onlineSessions.put(username, session);

        Message message = new Message(" is now connected!", ENTER, username, onlineSessions.size());
        sendMessageToAll(message);
    }

    /**
     * Send message, 1) get username and session, 2) send message to all.
     */
    @OnMessage
    public void onMessage(Session session, String jsonStr) throws IOException {
        Message message = JSON.parseObject(jsonStr, Message.class);
        message.setType(CHAT);
        message.setOnlineCount(onlineSessions.size());
        sendMessageToAll(message);
    }

    /**
     * Close connection, 1) remove session, 2) update user.
     */
    @OnClose
    public void onClose(Session session, @PathParam("username") String username) throws IOException {
        onlineSessions.remove(username, session);
        Message message = new Message(" has left the chat", LEAVE, username, onlineSessions.size());
        sendMessageToAll(message);
    }

    /**
     * Print exception.
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

}
