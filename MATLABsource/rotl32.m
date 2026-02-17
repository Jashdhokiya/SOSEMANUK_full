function y = rotl32(x,n)
    n = uint32(n);
    y = bitor(bitshift(x,n), bitshift(x, n-32));
end
