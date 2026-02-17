% serpent24_datarand.m
function [B12, B18, B24] = serpent24_datarand(iv_words, subkeys)
% Performs 24 rounds of Serpent and captures the state at rounds 12, 18, and 24.

    B = iv_words; % Start with the IV as the initial block
    
    for i = 0:23 % Loop for 24 rounds (0 to 23)
        
        % Get the 128-bit subkey for the current round
        K_i = subkeys((4*i + 1):(4*i + 4));
        
        % --- Perform one round of Serpent ---
        % 1. XOR with the subkey
        temp = bitxor(B, K_i);
        
        % 2. Apply the S-box
        sbox_index = mod(i, 8);
        temp = apply_sbox_to_words(temp, sbox_index);
        
        % 3. Apply the Linear Transformation
        % SOSEMANUK's Serpent24 performs LT in all 24 rounds [cite: 135]
        B = serpent_linear_transform(temp);

        % --- Capture the state at specific rounds ---
        if i == 11 % After round 12 (0-indexed)
            B12 = B;
        elseif i == 17 % After round 18
            B18 = B;
        elseif i == 23 % After round 24
            B24 = B;
        end
    end
end