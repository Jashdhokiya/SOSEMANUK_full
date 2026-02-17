function result = alpha_mul(word)
% Multiplies a 32-bit word by alpha in GF(2^32).

    % The reduction polynomial for this field is 0xA966827B
    poly = uint32(hex2dec('A966827B')); 
    
    % Check if the most significant bit is 1
    msb = bitget(word, 32);
    
    % Left shift the word by 1
    result = bitshift(word, 1);
    
    % If the MSB was 1, XOR with the polynomial
    if msb == 1
        result = bitxor(result, poly);
    end
end