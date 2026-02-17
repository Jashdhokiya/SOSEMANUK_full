package com.jash.matlabcrypto.controller;

import com.jash.matlabcrypto.dto.CryptoRequest;
import com.jash.matlabcrypto.service.MatlabService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/crypto")
@CrossOrigin
public class CryptoController {

    private final MatlabService matlabService;

    public CryptoController(MatlabService matlabService) {
        this.matlabService = matlabService;
    }

    @PostMapping("/encrypt")
    public ResponseEntity<String> encrypt(@RequestBody CryptoRequest request) throws Exception {
        System.out.println("KEY FROM FRONTEND: " + request.getKey());
        System.out.println("IV FROM FRONTEND: " + request.getIv());
        return ResponseEntity.ok(
                matlabService.process(request.getMsg(),
                        request.getKey(),
                        request.getIv(),
                        "encrypt")
        );
    }

    @PostMapping("/decrypt")
    public ResponseEntity<String> decrypt(@RequestBody CryptoRequest request) throws Exception {
        return ResponseEntity.ok(
                matlabService.process(request.getMsg(),
                        request.getKey(),
                        request.getIv(),
                        "decrypt")
        );
    }


}
