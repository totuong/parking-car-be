package main.twinbackend;

import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ParkingWebSocketHandler extends TextWebSocketHandler {

    private final Set<WebSocketSession> sessions =
            ConcurrentHashMap.newKeySet();

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {

        sessions.add(session);

        System.out.println("Connected : " + session.getId());
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session,
                                      @NonNull CloseStatus status) {

        sessions.remove(session);

        System.out.println("Disconnected");
    }

    public void broadcast(String json) {

        for (WebSocketSession session : sessions) {

            if (session.isOpen()) {

                try {

                    session.sendMessage(new TextMessage(json));

                } catch (IOException e) {

                    e.printStackTrace();
                }

            }

        }

    }

}