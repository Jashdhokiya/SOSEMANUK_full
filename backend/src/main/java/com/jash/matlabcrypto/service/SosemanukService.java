package com.jash.matlabcrypto.service;

import com.jash.matlabcrypto.cryptoEngine.SosemanukEngine;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.util.HexFormat;

@Service
public class SosemanukService {

    // A simple wrapper to carry both the PDF bytes and the keys back to the controller
    public static class ProcessResult {
        public final byte[] data;
        public final String keyUsed;
        public final String ivUsed;

        public ProcessResult(byte[] data, String keyUsed, String ivUsed) {
            this.data = data;
            this.keyUsed = keyUsed;
            this.ivUsed = ivUsed;
        }
    }

    public static class TextResult {
        public final String output;
        public final String keyUsed;
        public final String ivUsed;

        public TextResult(String output, String keyUsed, String ivUsed) {
            this.output = output;
            this.keyUsed = keyUsed;
            this.ivUsed = ivUsed;
        }
    }

    /**
     * Processes files (PDFs) and generates random keys if they are missing.
     */
    public ProcessResult processFile(byte[] fileData, String keyHex, String ivHex) throws Exception {
        // 1. Generate random keys if inputs are empty or null
        String finalKey = (keyHex == null || keyHex.isBlank()) ? randomHexString(64) : keyHex;
        String finalIv = (ivHex == null || ivHex.isBlank()) ? randomHexString(32) : ivHex;

        // 2. Prepare Key/IV (Padding/Sanitizing)
        byte[] keyBytes = parseHex(finalKey, 64);
        byte[] ivBytes = parseHex(finalIv, 32);

        // 3. Initialize Engine and Process
        SosemanukEngine engine = new SosemanukEngine(keyBytes, ivBytes);
        byte[] processedData = engine.process(fileData);

        // 4. Return the data along with the keys used
        return new ProcessResult(processedData, finalKey, finalIv);
    }

    /**
     * Processes text messages (Standard mode).
     */
    public TextResult process(String msg, String keyHex, String ivHex, String mode) throws Exception {
        // 1. Generate random keys for text if empty (consistent with PDF logic)
        String finalKey = (keyHex == null || keyHex.isBlank()) ? randomHexString(64) : keyHex;
        String finalIv = (ivHex == null || ivHex.isBlank()) ? randomHexString(32) : ivHex;

        byte[] key = parseHex(finalKey, 64);
        byte[] iv = parseHex(finalIv, 32);

        SosemanukEngine engine = new SosemanukEngine(key, iv);

        if ("encrypt".equalsIgnoreCase(mode)) {
            byte[] inputBytes = msg.getBytes();
            byte[] encrypted = engine.process(inputBytes);
            return new TextResult(bytesToHex(encrypted), finalKey, finalIv);
        } else {
            byte[] cipherBytes = hexToBytes(msg);
            byte[] decrypted = engine.process(cipherBytes);
            return new TextResult(new String(decrypted), finalKey, finalIv);
        }
    }

    // --- Helper Methods ---

    private byte[] parseHex(String hex, int requiredLen) {
        String clean = (hex == null) ? "" : hex.replaceAll("[^0-9A-Fa-f]", "");
        if (clean.length() < requiredLen) {
            clean = String.format("%-" + requiredLen + "s", clean).replace(' ', '0');
        } else {
            clean = clean.substring(0, requiredLen);
        }
        return hexToBytes(clean);
    }

    private String randomHexString(int length) {
        // Use SecureRandom for cryptographic strength
        SecureRandom sr = new SecureRandom();
        byte[] bytes = new byte[length / 2];
        sr.nextBytes(bytes);
        return HexFormat.of().withUpperCase().formatHex(bytes);
    }

    private byte[] hexToBytes(String s) {
        return HexFormat.of().parseHex(s);
    }

    private String bytesToHex(byte[] bytes) {
        return HexFormat.of().withUpperCase().formatHex(bytes);
    }
}