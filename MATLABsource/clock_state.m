% clock_state.m
function [ctx, f_t] = clock_state(ctx)
% Advances the cipher state by one step and returns the FSM output f_t.
    
    % Update the FSM and get its pre-Serpent1 output
    [ctx, f_t] = fsm_step(ctx);
    
    % Update the LFSR
    fb = lfsr_feedback(ctx);
    ctx.s = [fb, ctx.s(1:end-1)];
end