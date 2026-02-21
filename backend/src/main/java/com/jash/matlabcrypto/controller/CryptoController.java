package com.jash.matlabcrypto.controller;

import com.jash.matlabcrypto.dto.CryptoRequest;
import com.jash.matlabcrypto.service.SosemanukService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/crypto")
@CrossOrigin
public class CryptoController {

    private final SosemanukService sosemanukService;

    public CryptoController(SosemanukService sosemanukService) {
        this.sosemanukService = sosemanukService;
    }

    @PostMapping("/encrypt")
    public ResponseEntity<String> encrypt(@RequestBody CryptoRequest request) {
        try {
            String result = sosemanukService.process(
                    request.getMsg(),
                    request.getKey(),
                    request.getIv(),
                    "encrypt"
            );
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Encryption Error: " + e.getMessage());
        }
    }

    @PostMapping("/decrypt")
    public ResponseEntity<String> decrypt(@RequestBody CryptoRequest request) {
        try {
            String result = sosemanukService.process(
                    request.getMsg(),
                    request.getKey(),
                    request.getIv(),
                    "decrypt"
            );
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Decryption Error: " + e.getMessage());
        }
    }
}