function subkeys = serpent_key_schedule(key_bytes)
%SERPENT_KEY_SCHEDULE Generates subkeys from a given secret key.
%   This function implements the key scheduling algorithm for the Serpent
%   block cipher to produce 25 128-bit subkeys (100 32-bit words) as
%   required for Serpent24 in SOSEMANUK.

    %% Step 1: Key Preparation
    % Ensure the key is 32 bytes (256 bits) by zero-padding if necessary.
    if numel(key_bytes) < 32
        padded_key = zeros(1, 32, 'uint8');
        padded_key(1:numel(key_bytes)) = key_bytes;
        key_bytes = padded_key;
    end
    
    % Convert the 32-byte key into 8 32-bit words (little-endian).
    w = typecast(key_bytes, 'uint32');

    %% Step 2: Pre-key Expansion
    % Expand the 8 words into 132 pre-keys (w_0 to w_131).
    w_expanded = zeros(1, 132, 'uint32');
    w_expanded(1:8) = w;
    
    phi = uint32(hex2dec('9e3779b9')); % The golden ratio fraction

    for i = 9:132
        % Formula: w[i] = ROTL32(w[i-8] ^ w[i-5] ^ w[i-3] ^ w[i-1] ^ phi ^ (i-1), 11)
        % Note: Using (i-1) for 0-based index from the spec.
        temp = bitxor(w_expanded(i-8), w_expanded(i-5));
        temp = bitxor(temp, w_expanded(i-3));
        temp = bitxor(temp, w_expanded(i-1));
        temp = bitxor(temp, phi);
        temp = bitxor(temp, uint32(i-1));
        
        w_expanded(i) = bitrol(temp, 11);
    end

    %% Step 3: Final Subkey Generation using S-boxes
    % Generate 25 128-bit subkeys (K_0 to K_24) from the expanded pre-keys.
    subkeys = zeros(1, 100, 'uint32');
    
    for i = 0:24 % For each of the 25 subkeys
        % Determine which S-box to use: S_(3-i) mod 8
        sbox_index = mod(3 - i, 8);
        
        % Get the four 32-bit pre-keys for this round
        w_slice = w_expanded((4*i + 1):(4*i + 4));
        
        % Apply the S-box to all four words
        k_slice = apply_sbox_to_words(w_slice, sbox_index);
        
        % Place the resulting 128-bit key into the final subkey array
        subkeys((4*i + 1):(4*i + 4)) = k_slice;
    end
end

