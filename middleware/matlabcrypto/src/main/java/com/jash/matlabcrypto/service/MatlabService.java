package com.jash.matlabcrypto.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
public class MatlabService {

    public String encrypt(String msg, String key, String iv) throws Exception {

        String command = "set MSG=" + msg
                + " && set KEY=" + key
                + " && set IV=" + iv
                + " && matlab -batch \"run('D:/College/sem 5/IS/project/run_sosemanuk.m')\"";

        ProcessBuilder processBuilder =
                new ProcessBuilder("cmd.exe", "/c", command);

        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream())
        );

        StringBuilder output = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            output.append(line);
        }

        process.waitFor();

        return output.toString().trim();
    }
}
