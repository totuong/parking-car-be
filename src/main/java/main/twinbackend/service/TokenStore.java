package main.twinbackend.service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class TokenStore {

    private static final Set<String> ACTIVE_TOKENS = ConcurrentHashMap.newKeySet();
    private static final Set<String> REVOKED_TOKENS = ConcurrentHashMap.newKeySet();

    public static void save(String token) {
        if (token != null && !token.isBlank()) {
            ACTIVE_TOKENS.add(token);
        }
    }

    public static boolean exists(String token) {
        return token != null && ACTIVE_TOKENS.contains(token);
    }

    public static void revoke(String token) {
        if (token != null && !token.isBlank()) {
            ACTIVE_TOKENS.remove(token);
            REVOKED_TOKENS.add(token);
        }
    }

    public static boolean isRevoked(String token) {
        return token != null && REVOKED_TOKENS.contains(token);
    }

    public static void remove(String token) {
        revoke(token);
    }
}