function y = bitrol(x, n)
%BITROL Custom implementation of 32-bit rotate left.
%   Performs a circular left shift on a 32-bit unsigned integer.

    % Ensure the input is a 32-bit unsigned integer for correct bitwise operations
    x = uint32(x);
    n = uint32(n);
    
    % Ensure the shift amount is within the 32-bit range
    n = mod(n, 32);
    
    % Perform the rotate using two bitshifts and a bitwise OR
    y = bitor(bitshift(x, n), bitshift(x, -(32-n)));
end