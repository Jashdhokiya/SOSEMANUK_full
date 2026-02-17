function plaintext = sosemanuk_decrypt(ctx, ciphertext)
    % SOSEMANUK decryption = XOR with keystream
    % same as encryption
    
    plaintext = sosemanuk_process(ctx, ciphertext);
end
