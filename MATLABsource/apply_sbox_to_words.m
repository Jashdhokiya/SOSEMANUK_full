function output_words = apply_sbox_to_words(words, sbox_index)
% Helper function to apply a chosen S-box to an array of 32-bit words.
    
    % Serpent S-boxes (0-indexed)
    sboxes = { ...
        [3, 8, 15, 1, 10, 6, 5, 11, 14, 13, 4, 2, 7, 0, 9, 12], ... % S0
        [15, 12, 2, 7, 9, 0, 5, 10, 1, 11, 14, 8, 6, 13, 3, 4], ... % S1
        [8, 6, 7, 9, 3, 12, 10, 15, 13, 1, 14, 4, 0, 11, 5, 2], ... % S2
        [0, 15, 11, 8, 12, 9, 6, 3, 13, 1, 2, 4, 10, 7, 5, 14], ... % S3
        [1, 15, 8, 3, 12, 0, 11, 6, 2, 5, 4, 10, 9, 14, 7, 13], ... % S4
        [15, 5, 2, 11, 4, 10, 0, 7, 12, 1, 6, 13, 8, 9, 3, 14], ... % S5
        [8, 1, 4, 15, 13, 0, 10, 3, 7, 2, 9, 5, 14, 12, 11, 6], ... % S6
        [13, 7, 14, 0, 5, 10, 3, 4, 6, 12, 1, 9, 2, 15, 8, 11]  ... % S7
    };

    sbox = sboxes{sbox_index + 1}; % MATLAB is 1-indexed
    output_words = zeros(size(words), 'uint32');

    for i = 1:numel(words)
        word = words(i);
        new_word = uint32(0);
        for j = 0:7 % Iterate through the 8 nibbles (4-bit chunks) of the word
            % Extract the nibble
            nibble = bitand(bitshift(word, -4 * j), uint32(15));
            % S-box lookup (MATLAB is 1-indexed)
            new_nibble = sbox(nibble + 1);
            % Place the new nibble back into the word
            new_word = bitor(new_word, bitshift(uint32(new_nibble), 4 * j));
        end
        output_words(i) = new_word;
    end
end