package br.edu.seffrin.stomp;

import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.concurrent.ExecutionException;

public class WebSocketStompClientWS {

    private static StompSession stompSession;

    public static void main(String[] args) {
        String brokerURL = "ws://mq2.arpasistemas.com.br:15674/ws";
        String username = "arpag";
        String password = "po$p@g2244#$up3rvis%";
        String vhost = "/";

        StandardWebSocketClient standardWebSocketClient = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(standardWebSocketClient);

        // Add a StringMessageConverter to handle String payloads
        stompClient.setMessageConverter(new StringMessageConverter());

        // Set up heartbeat and reconnection logic
        stompClient.setDefaultHeartbeat(new long[]{4000, 4000});
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.initialize();
        stompClient.setTaskScheduler(scheduler);

        StompSessionHandler sessionHandler = new StompSessionHandlerAdapter() {
            @Override
            public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                System.out.println("Connected!");
                stompSession = session;

                // Subscribe to a queue
                session.subscribe("/queue/test", new StompFrameHandler() {
                    @Override
                    public Type getPayloadType(StompHeaders headers) {
                        return String.class;
                    }

                    @Override
                    public void handleFrame(StompHeaders headers, Object payload) {
                        System.out.println("Received message: " + payload);
                    }
                });

                // Send the message after connection is established
                sendMessage("/queue/test", "Hello after connection!");
            }

            @Override
            public void handleTransportError(StompSession session, Throwable exception) {
                System.out.println("Transport error: " + exception.getMessage());
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                System.out.println("Received frame: " + payload);
            }

            @Override
            public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
                System.out.println("Error: " + exception.getMessage());
            }

            public void handleSessionDisconnect(StompSession session, StompHeaders headers) {
                System.out.println("Disconnected");
            }
        };

        StompHeaders connectHeaders = new StompHeaders();
        connectHeaders.add("login", username);
        connectHeaders.add("passcode", password);
        connectHeaders.add("host", vhost);

        try {
            stompClient.connect(brokerURL, new WebSocketHttpHeaders(), connectHeaders, sessionHandler).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sendMessage(String destination, String message) {
        if (stompSession != null ) {
//        if (stompSession != null && stompSession.isConnected()) {
            stompSession.send(destination, message);
            System.out.println("Message sent: " + message);
        } else {
            System.out.println("Cannot send message, session is not connected.");
        }
    }
}