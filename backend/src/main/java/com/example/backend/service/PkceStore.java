package com.example.backend.service;

import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory store for PKCE authorization codes.
 *
 * Each code is single-use and expires after {@value #TTL_MS} ms (60 seconds).
 * This is sufficient for the mobile → token exchange round-trip while preventing
 * replay attacks.
 */
@Component
public class PkceStore {

    /** Authorization codes are valid for 60 seconds. */
    private static final long TTL_MS = 60_000L;

    private final ConcurrentHashMap<String, Entry> store = new ConcurrentHashMap<>();

    /**
     * Persist a code challenge for the given user and return a new one-time
     * authorization code.
     */
    public String store(String email, String codeChallenge) {
        String code = UUID.randomUUID().toString().replace("-", "");
        store.put(code, new Entry(email, codeChallenge, System.currentTimeMillis() + TTL_MS));
        return code;
    }

    /**
     * Consume and return the {@link Entry} for the given code.
     * Returns {@code null} if the code was never issued, already consumed, or expired.
     */
    public Entry consume(String code) {
        Entry entry = store.remove(code);
        if (entry == null || entry.isExpired()) return null;
        return entry;
    }

    public static class Entry {
        public final String email;
        public final String codeChallenge;
        private final long expiresAt;

        Entry(String email, String codeChallenge, long expiresAt) {
            this.email = email;
            this.codeChallenge = codeChallenge;
            this.expiresAt = expiresAt;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() > expiresAt;
        }
    }
}
