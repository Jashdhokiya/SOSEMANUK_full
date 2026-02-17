% sosemanuk_init.m
function ctx = sosemanuk_init(key, iv)
% Initializes the SOSEMANUK context using the real Serpent24 procedure.

    ctx = struct();
    
    % --- Step 1: Generate Subkeys (no change here) ---
    ctx.subkeys = serpent_key_schedule(key);
    
    % --- Step 2: Run Serpent24 to get initial state values ---
    iv_words = typecast(iv, 'uint32');
    [B12, B18, B24] = serpent24_datarand(iv_words, ctx.subkeys);
    
    % --- Step 3: Set the initial state of the LFSR and FSM ---
    % SOSEMANUK uses a 10-word LFSR [cite: 171]
    ctx.s = zeros(1, 10, 'uint32'); 
    
    % The state is assembled from the captured Serpent24 outputs [cite: 242]
    % Note: Indices are adjusted for MATLAB's 1-based arrays
    ctx.s(7:10) = B12;                  % s6, s7, s8, s9
    ctx.s(5:6)  = [B18(2), B18(4)];     % s4, s5
    ctx.s(1:4)  = B24;                  % s0, s1, s2, s3
    
    ctx.fsm     = [B18(1), B18(3)];     % R1_0, R2_0
    
    ctx.fsm_idx = 1;

    % --- Step 4: Warm-up clocks (no change here) ---
    for i = 1:10
        ctx = clock_state(ctx);
    end
end