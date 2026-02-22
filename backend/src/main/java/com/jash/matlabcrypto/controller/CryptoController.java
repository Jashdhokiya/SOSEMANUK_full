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
@CrossOrigin
public class CryptoController {

    private final SosemanukService sosemanukService;

    public CryptoController(SosemanukService sosemanukService) {
        this.sosemanukService = sosemanukService;
    }

    @PostMapping("/encrypt")
    public ResponseEntity<String> encrypt(@RequestBody CryptoRequest request) {
        try {
//            if(request.getKey().equals("")){
//
//            }
//            if(request.getIv().equals("")){
//
//            }
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
    @PostMapping("/encrypt-pdf")
    public ResponseEntity<byte[]> encryptPdf(
            @RequestParam("file") MultipartFile file,
            @RequestParam("key") String key,
            @RequestParam("iv") String iv) throws Exception {

        byte[] processedPdf = sosemanukService.processFile(file.getBytes(), key, iv);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"encrypted.pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(processedPdf);
    }

    @PostMapping("/decrypt-pdf")
    public ResponseEntity<byte[]> decryptPdf(
            @RequestParam("file") MultipartFile file,
            @RequestParam("key") String key,
            @RequestParam("iv") String iv) throws Exception {

        byte[] processedPdf = sosemanukService.processFile(file.getBytes(), key, iv);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"decrypted.pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(processedPdf);
    }
}