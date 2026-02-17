% serpent1_process.m
function output_block = serpent1_process(input_block)
% Performs one round of Serpent using only S-box 2, as required by SOSEMANUK.
    
    % The input_block is a 1x4 uint32 array (f_t+3, f_t+2, f_t+1, f_t)
    
    % S-box 2 from the Serpent specification
    sbox2 = [8, 6, 7, 9, 3, 12, 10, 15, 13, 1, 14, 4, 0, 11, 5, 2];
    
    output_block = zeros(size(input_block), 'uint32');
    
    for i = 1:4 % For each of the four 32-bit words
        word = input_block(i);
        new_word = uint32(0);
        for j = 0:7 % For each of the 8 nibbles in the word
            nibble = bitand(bitshift(word, -4 * j), uint32(15));
            new_nibble = sbox2(nibble + 1); % MATLAB is 1-indexed
            new_word = bitor(new_word, bitshift(uint32(new_nibble), 4 * j));
        end
        output_block(i) = new_word;
    end
end