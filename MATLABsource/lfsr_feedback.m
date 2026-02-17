function fb = lfsr_feedback(ctx)
% Implements the full LFSR feedback logic for SOSEMANUK.

    s = ctx.s;
    
    % The official formula is s_t+10 = s_t+3 XOR (a^-1 * s_t+1) XOR (a * s_t)
    % In our 1-based array:
    % s_t+3 is s(7)
    % s_t+1 is s(9)
    % s_t   is s(10)
    
    term1 = s(7);
    term2 = alpha_inv_mul(s(9));
    term3 = alpha_mul(s(10));
    
    fb = bitxor(term1, bitxor(term2, term3));
end