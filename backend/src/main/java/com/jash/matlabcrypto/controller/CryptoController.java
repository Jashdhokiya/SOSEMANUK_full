package com.jash.matlabcrypto.controller;

import com.jash.matlabcrypto.dto.CryptoRequest;
import com.jash.matlabcrypto.service.SosemanukService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/crypto")
@CrossOrigin // Allows your React frontend to communicate with this API
public class CryptoController {

    private final SosemanukService sosemanukService;

    public CryptoController(SosemanukService sosemanukService) {
        this.sosemanukService = sosemanukService;
    }

    @PostMapping("/encrypt")
    public ResponseEntity<SosemanukService.TextResult> encrypt(@RequestBody CryptoRequest request) {
        try {
            SosemanukService.TextResult result = sosemanukService.process(
                    request.getMsg(),
                    request.getKey(),
                    request.getIv(),
                    "encrypt"
            );
            // Returning the whole object so the frontend gets { "output": "...", "keyUsed": "...", "ivUsed": "..." }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/decrypt")
    public ResponseEntity<SosemanukService.TextResult> decrypt(@RequestBody CryptoRequest request) {
        try {
            SosemanukService.TextResult result = sosemanukService.process(
                    request.getMsg(),
                    request.getKey(),
                    request.getIv(),
                    "decrypt"
            );
            // Returning the whole object so the frontend gets { "output": "...", "keyUsed": "...", "ivUsed": "..." }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/encrypt-pdf")
    public ResponseEntity<byte[]> encryptPdf(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "iv", required = false) String iv) {
        try {
            // Call the updated service that returns ProcessResult
            SosemanukService.ProcessResult result = sosemanukService.processFile(file.getBytes(), key, iv);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"encrypted.pdf\"")
                    // Custom headers to pass the keys back to the frontend
                    .header("X-Generated-Key", result.keyUsed)
                    .header("X-Generated-IV", result.ivUsed)
                    // CRITICAL: Tells the browser it's okay to read these custom headers
                    .header("Access-Control-Expose-Headers", "X-Generated-Key, X-Generated-IV")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(result.data);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/decrypt-pdf")
    public ResponseEntity<byte[]> decryptPdf(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "iv", required = false) String iv) {
        try {
            // For stream ciphers, decryption is the same XOR logic as encryption
            SosemanukService.ProcessResult result = sosemanukService.processFile(file.getBytes(), key, iv);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"decrypted.pdf\"")
                    .header("X-Generated-Key", result.keyUsed)
                    .header("X-Generated-IV", result.ivUsed)
                    .header("Access-Control-Expose-Headers", "X-Generated-Key, X-Generated-IV")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(result.data);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}