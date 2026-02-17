package com.jash.matlabcrypto.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
public class MatlabService {

    public String process(String msg, String key, String iv, String mode) throws Exception {

        ProcessBuilder processBuilder = new ProcessBuilder(
                "C:\\Program Files\\MATLAB\\R2025b\\bin\\matlab.exe",
                "-batch",
                "addpath('D:/College/sem 5/IS/project'); run_sosemanuk"
        );

        // ✅ Proper way to pass environment variables
        processBuilder.environment().put("MSG", msg == null ? "" : msg);
        processBuilder.environment().put("KEY", key == null ? "" : key);
        processBuilder.environment().put("IV", iv == null ? "" : iv);
        processBuilder.environment().put("MODE", mode);


        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream())
        );

        StringBuilder output = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            System.out.println("MATLAB: " + line); // debug
            output.append(line).append("\n");
        }

        process.waitFor();

        return output.toString().trim();
    }
}
