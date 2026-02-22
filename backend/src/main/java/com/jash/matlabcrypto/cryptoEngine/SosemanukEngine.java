package com.jash.matlabcrypto.cryptoEngine;

import java.util.Arrays;

public class SosemanukEngine {

    private int[] s = new int[10]; // LFSR state
    private int r1, r2;            // FSM state
    private byte[] keyStreamBuffer = new byte[16];
    private int bufferPointer = 16;

    // Serpent S-Boxes (0-7)
    private static final int[][] SBOXES = {
            {3, 8, 15, 1, 10, 6, 5, 11, 14, 13, 4, 2, 7, 0, 9, 12}, // S0
            {15, 12, 2, 7, 9, 0, 5, 10, 1, 11, 14, 8, 6, 13, 3, 4}, // S1
            {8, 6, 7, 9, 3, 12, 10, 15, 13, 1, 14, 4, 0, 11, 5, 2}, // S2
            {0, 15, 11, 8, 12, 9, 6, 3, 13, 1, 2, 4, 10, 7, 5, 14}, // S3
            {1, 15, 8, 3, 12, 0, 11, 6, 2, 5, 4, 10, 9, 14, 7, 13}, // S4
            {15, 5, 2, 11, 4, 10, 0, 7, 12, 1, 6, 13, 8, 9, 3, 14}, // S5
            {8, 1, 4, 15, 13, 0, 10, 3, 7, 2, 9, 5, 14, 12, 11, 6}, // S6
            {13, 7, 14, 0, 5, 10, 3, 4, 6, 12, 1, 9, 2, 15, 8, 11}  // S7
    };

    public SosemanukEngine(byte[] key, byte[] iv) {
        init(key, iv);
    }

    private void init(byte[] key, byte[] iv) {
        int[] subkeys = serpentKeySchedule(key);
        int[] ivWords = bytesToWords(iv);

        // serpent24_datarand logic
        int[] B = ivWords;
        int[] B12 = new int[4], B18 = new int[4], B24 = new int[4];

        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 4; j++) B[j] ^= subkeys[4 * i + j];
            B = applySboxToWords(B, i % 8);
            B = serpentLinearTransform(B);
            if (i == 11) B12 = B.clone();
            if (i == 17) B18 = B.clone();
            if (i == 23) B24 = B.clone();
        }

        // Initialize LFSR (s0...s9) and FSM (R1, R2)
        System.arraycopy(B24, 0, s, 0, 4);
        System.arraycopy(B18, 0, s, 4, 4);
        System.arraycopy(B12, 0, s, 8, 2);
        this.r1 = B12[2];
        this.r2 = B12[3];
    }

    public byte[] process(byte[] input) {
        byte[] output = new byte[input.length];
        for (int i = 0; i < input.length; i++) {
            if (bufferPointer >= 16) {
                generateKeystreamChunk();
                bufferPointer = 0;
            }
            output[i] = (byte) (input[i] ^ keyStreamBuffer[bufferPointer++]);
        }
        return output;
    }

    private void generateKeystreamChunk() {
        int[] f = new int[4];
        int[] sOut = new int[4];

        for (int j = 0; j < 4; j++) {
            f[j] = fsmStep();
            sOut[j] = s[0]; // s_t
            clockLFSR();
        }

        int[] serpentOut = serpent1Process(f);
        for (int j = 0; j < 4; j++) {
            int word = serpentOut[j] ^ sOut[j];
            // Little-endian word to bytes
            keyStreamBuffer[j * 4] = (byte) (word & 0xFF);
            keyStreamBuffer[j * 4 + 1] = (byte) ((word >> 8) & 0xFF);
            keyStreamBuffer[j * 4 + 2] = (byte) ((word >> 16) & 0xFF);
            keyStreamBuffer[j * 4 + 3] = (byte) ((word >> 24) & 0xFF);
        }
    }

    private int fsmStep() {
        // 1. Calculate the new R1 (Equation 19)
        // Needs to use R2 from previous state
        int muxOut = ((r1 & 1) == 1) ? (s[1] ^ s[8]) : s[1];
        int r1Next = (int)(((long)(r2 & 0xFFFFFFFFL) + (muxOut & 0xFFFFFFFFL)) & 0xFFFFFFFFL);

        // 2. Calculate the output value f_t (Equation 21)
        // IMPORTANT: This uses the NEW R1 but the OLD R2
        int f_t = (int)(((long)(s[9] & 0xFFFFFFFFL) + (r1Next & 0xFFFFFFFFL)) & 0xFFFFFFFFL) ^ r2;

        // 3. Update R2 for next time (Equation 20)
        // Trans(z) = (0x54655307 * z mod 2^32) >>> 7 (Rotate Right)
        long prod = (0x54655307L * (r1 & 0xFFFFFFFFL)) & 0xFFFFFFFFL;
        int r2Next = Integer.rotateRight((int) prod, 7);

        // 4. Update state
        this.r1 = r1Next;
        this.r2 = r2Next;

        return f_t;
    }

    private int[] serpentKeySchedule(byte[] keyBytes) {
        int[] w = new int[132];
        byte[] padded = new byte[32];
        System.arraycopy(keyBytes, 0, padded, 0, Math.min(keyBytes.length, 32));

        // Initialize first 8 words
        for (int i = 0; i < 8; i++) {
            w[i] = (padded[i*4] & 0xFF) | ((padded[i*4+1] & 0xFF) << 8) |
                    ((padded[i*4+2] & 0xFF) << 16) | ((padded[i*4+3] & 0xFF) << 24);
        }

        // Expansion logic - Fix: use (i-1) not (i-8)
        int phi = 0x9e3779b9;
        for (int i = 8; i < 132; i++) {
            int val = w[i-8] ^ w[i-5] ^ w[i-3] ^ w[i-1] ^ phi ^ (i-8); // The (i-8) here matches 0-based index logic
            w[i] = Integer.rotateLeft(val, 11);
        }

        int[] subkeys = new int[100];
        for (int i = 0; i < 25; i++) {
            int sboxIdx = (3 - i) % 8;
            if (sboxIdx < 0) sboxIdx += 8;

            // Grab the expanded words
            int[] slice = {w[4*i + 8], w[4*i + 9], w[4*i + 10], w[4*i + 11]};
            int[] k = applySboxToWords(slice, sboxIdx);
            System.arraycopy(k, 0, subkeys, i * 4, 4);
        }
        return subkeys;
    }

    private void clockLFSR() {
        int fb = s[6] ^ alphaInvMul(s[8]) ^ alphaMul(s[9]);
        // Shift right (s[0] is s_t)
        for (int i = 9; i > 0; i--) s[i] = s[i - 1];
        s[0] = fb;
    }

    private int alphaMul(int w) {
        int res = w << 1;
        if ((w & 0x80000000) != 0) res ^= 0xA966827B;
        return res;
    }

    private int alphaInvMul(int w) {
        int res = w >>> 1;
        if ((w & 1) != 0) res ^= 0xB89D3415;
        return res;
    }

    private int[] serpent1Process(int[] f) {
        return applySboxToWords(f, 2); // SOSEMANUK uses Sbox 2 for internal clocking
    }

    private int[] serpentLinearTransform(int[] x) {
        x[0] = Integer.rotateLeft(x[0], 13);
        x[2] = Integer.rotateLeft(x[2], 3);
        x[1] ^= x[0] ^ x[2];
        x[3] ^= x[2] ^ (x[0] << 3);
        x[1] = Integer.rotateLeft(x[1], 1);
        x[3] = Integer.rotateLeft(x[3], 7);
        x[0] ^= x[1] ^ x[3];
        x[2] ^= x[3] ^ (x[1] << 7);
        x[0] = Integer.rotateLeft(x[0], 5);
        x[2] = Integer.rotateLeft(x[2], 22);
        return x;
    }



    private int[] applySboxToWords(int[] words, int sboxIdx) {
        int[] out = new int[words.length];
        int[] sbox = SBOXES[sboxIdx];
        for (int i = 0; i < words.length; i++) {
            int word = words[i];
            int res = 0;
            for (int j = 0; j < 8; j++) {
                int nibble = (word >> (4 * j)) & 0xF;
                res |= (sbox[nibble] << (4 * j));
            }
            out[i] = res;
        }
        return out;
    }

    private int[] bytesToWords(byte[] b) {
        int[] w = new int[b.length / 4];
        for (int i = 0; i < w.length; i++) {
            w[i] = (b[i*4] & 0xFF) | ((b[i*4+1] & 0xFF) << 8) |
                    ((b[i*4+2] & 0xFF) << 16) | ((b[i*4+3] & 0xFF) << 24);
        }
        return w;
    }
}