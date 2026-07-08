package main.twinbackend.config;

import lombok.RequiredArgsConstructor;
import main.twinbackend.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Arrays;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AuthHandshakeInterceptor
        implements HandshakeInterceptor {

    private final JwtService jwtService;

    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes) {

        String query = request.getURI().getQuery();

        if (query == null) {
            response.setStatusCode(HttpStatus.BAD_REQUEST);
            return false;
        }

        String token = Arrays.stream(query.split("&"))
                .map(s -> s.split("=", 2))
                .filter(s -> s[0].equals("token"))
                .map(s -> s[1])
                .findFirst()
                .orElse(null);

        if (token == null) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }

        if (!jwtService.validate(token)) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }

        attributes.put("username",
                jwtService.getUsername(token));

        return true;
    }

    @Override
    public void afterHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Exception exception) {

    }
}