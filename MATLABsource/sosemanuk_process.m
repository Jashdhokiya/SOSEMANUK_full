% sosemanuk_process.m
function output = sosemanuk_process(ctx, input)
% Encrypts/decrypts data using the full SOSEMANUK keystream pipeline.

    output = zeros(size(input), 'uint8');
    keystream_buffer = []; % Buffer to hold keystream bytes
    
    for i = 1:numel(input)
        if isempty(keystream_buffer)
            % --- Generate a 128-bit Keystream Chunk ---
            
            % 1. Clock the state 4 times to get four f_t values
            f = zeros(1, 4, 'uint32');
            s_out = zeros(1, 4, 'uint32');
            for j = 1:4
                [ctx, f(j)] = clock_state(ctx);
                s_out(j) = ctx.s(j);
            end
            
            % 2. Process the 4 f_t values through Serpent1
            serpent1_out = serpent1_process(f);
            
            % 3. XOR with the LFSR state to get the final keystream
            ks_chunk_32bit = bitxor(serpent1_out, s_out);
            
            % 4. Convert the 128-bit chunk into 16 bytes for the buffer
            keystream_buffer = typecast(ks_chunk_32bit, 'uint8');
        end
        
        % XOR one byte of data with one byte of keystream
        output(i) = bitxor(input(i), keystream_buffer(1));
        keystream_buffer(1) = [];
    end
end