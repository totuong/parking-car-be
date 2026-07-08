package main.twinbackend.service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class TokenStore {

    private static final Set<String> TOKENS = ConcurrentHashMap.newKeySet();

    public static void save(String token) {
        TOKENS.add(token);
    }

    public static boolean exists(String token) {
        return TOKENS.contains(token);
    }

    public static void remove(String token) {
        TOKENS.remove(token);
    }
}