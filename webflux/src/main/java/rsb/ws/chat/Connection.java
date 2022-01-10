package rsb.ws.chat;

import org.springframework.web.reactive.socket.WebSocketSession;

record Connection(String id, WebSocketSession session) {
}