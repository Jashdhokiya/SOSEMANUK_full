package com.jash.matlabcrypto.service;

import com.jash.matlabcrypto.cryptoEngine.SosemanukEngine;
import org.springframework.stereotype.Service;
import java.util.HexFormat; // Available in Java 17+

@Service
public class SosemanukService {
    // Add this method to your existing SosemanukService.java
    public byte[] processFile(byte[] fileData, String keyHex, String ivHex) throws Exception {
        // 1. Prepare Key/IV using your existing helper
        byte[] key = parseHex(keyHex, 64);
        byte[] iv = parseHex(ivHex, 32);

        // 2. Initialize the engine
        SosemanukEngine engine = new SosemanukEngine(key, iv);

        // 3. Process the bytes (Sosemanuk is a stream cipher, so encrypt/decrypt is the same XOR)
        return engine.process(fileData);
    }
    public String process(String msg, String keyHex, String ivHex, String mode) throws Exception {
        // 1. Sanitize and prepare Key/IV (matching your MATLAB logic)
        byte[] key = parseHex(keyHex, 64); // 32 bytes = 64 hex chars
        byte[] iv = parseHex(ivHex, 32);   // 16 bytes = 32 hex chars

        // 2. Initialize the native Java Engine
        SosemanukEngine engine = new SosemanukEngine(key, iv);

        if ("encrypt".equalsIgnoreCase(mode)) {
            // Text -> Bytes -> Encrypt -> Hex String
            byte[] inputBytes = msg.getBytes();
            byte[] encrypted = engine.process(inputBytes);
            return bytesToHex(encrypted);
        } else {
            // Hex String -> Bytes -> Decrypt -> Text
            byte[] cipherBytes = hexToBytes(msg);
            byte[] decrypted = engine.process(cipherBytes);
            return new String(decrypted);
        }
    }

    // --- Helper Methods to match MATLAB's hex handling ---

    private byte[] parseHex(String hex, int requiredLen) {
        String clean = hex == null ? "" : hex.replaceAll("[^0-9A-Fa-f]", "");
        // Pad or truncate to match your MATLAB 'pad' logic
        if (clean.length() < requiredLen) {
            clean = String.format("%-" + requiredLen + "s", clean).replace(' ', '0');
        } else {
            clean = clean.substring(0, requiredLen);
        }
        return hexToBytes(clean);
    }

    private byte[] hexToBytes(String s) {
        // If using Java 17+, HexFormat is much cleaner:
        return HexFormat.of().parseHex(s);
    }

    private String bytesToHex(byte[] bytes) {
        return HexFormat.of().withUpperCase().formatHex(bytes);
    }
}