% serpent_linear_transform.m
function output_block = serpent_linear_transform(input_block)
% Implements the Linear Transformation (LT) of the Serpent cipher.
    
    x = input_block; % The 4 words of the 128-bit block
    
    x(1) = bitrol(x(1), 13);
    x(3) = bitrol(x(3), 3);
    x(2) = bitxor(x(2), bitxor(x(1), x(3)));
    x(4) = bitxor(x(4), bitxor(x(3), bitshift(x(1), 3)));
    x(2) = bitrol(x(2), 1);
    x(4) = bitrol(x(4), 7);
    x(1) = bitxor(x(1), bitxor(x(2), x(4)));
    x(3) = bitxor(x(3), bitxor(x(4), bitshift(x(2), 7)));
    x(1) = bitrol(x(1), 5);
    x(3) = bitrol(x(3), 22);

    output_block = x;
end