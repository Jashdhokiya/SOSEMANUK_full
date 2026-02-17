function result = alpha_inv_mul(word)
% Multiplies a 32-bit word by alpha^-1 in GF(2^32).

    % The inverse reduction polynomial is 0xB89D3415
    inv_poly = uint32(hex2dec('B89D3415'));
    
    % Check if the least significant bit is 1
    lsb = bitget(word, 1);
    
    % Right shift the word by 1
    result = bitshift(word, -1);
    
    % If the LSB was 1, XOR with the inverse polynomial
    if lsb == 1
        result = bitxor(result, inv_poly);
    end
end