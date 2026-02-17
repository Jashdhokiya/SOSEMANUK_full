% fsm_step.m
function [ctx, f_t] = fsm_step(ctx)
% Implements the full FSM state update and output for SOSEMANUK.
    
    % --- Trans function from the paper (eq. 22) ---
    % Trans(z) = (0x54655307 * z mod 2^32) >>> 7
    function R2_new = Trans(z)
        % Perform modular multiplication
        prod = uint64(hex2dec('54655307')) * uint64(z);
        prod_32 = uint32(mod(prod, 2^32));
        % Perform 7-bit rotate right (>>> 7)
        R2_new = bitor(bitshift(prod_32, -7), bitshift(prod_32, 32-7));
    end

    % Get previous FSM state
    R1_prev = ctx.fsm(1);
    R2_prev = ctx.fsm(2);
    
    % --- Update R1 (eq. 19) ---
    % mux selects between s_t+1 and s_t+1 XOR s_t+8
    lsb_r1 = bitand(R1_prev, 1);
    mux_out = ctx.s(2); % s_t+1
    if lsb_r1 == 1
        mux_out = bitxor(mux_out, ctx.s(9)); % s_t+8
    end
    R1_new = mod(uint64(R2_prev) + uint64(mux_out), 2^32);

    % --- Update R2 (eq. 20) ---
    R2_new = Trans(R1_prev);

    % Update the context's FSM state for the next cycle
    ctx.fsm = uint32([R1_new, R2_new]);

    % --- Calculate FSM output f_t (eq. 21) ---
    sum_val = mod(uint64(ctx.s(10)) + uint64(R1_new), 2^32); % s_t+9 + R1_t
    f_t = bitxor(uint32(sum_val), R2_new);
end