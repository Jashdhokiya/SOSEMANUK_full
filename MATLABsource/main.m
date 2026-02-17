function output = main(input, key_hex, iv_hex, mode)

    default_key_hex = '0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF';
    default_iv_hex  = 'ABCDEF0123456789ABCDEF0123456789';

    if nargin < 2 || isempty(key_hex)
        key_hex = default_key_hex;
    end

    if nargin < 3 || isempty(iv_hex)
        iv_hex = default_iv_hex;
    end

    if nargin < 4 || isempty(mode)
        mode = 'encrypt';
    end

    key_hex = upper(regexprep(key_hex, '[^0-9A-F]', ''));
    iv_hex  = upper(regexprep(iv_hex,  '[^0-9A-F]', ''));

    key_hex = pad(key_hex, 64, 'right', '0');
    key_hex = key_hex(1:64);

    iv_hex = pad(iv_hex, 32, 'right', '0');
    iv_hex = iv_hex(1:32);

    key = uint8(sscanf(key_hex, '%2x').');
    iv  = uint8(sscanf(iv_hex,  '%2x').');

    ctx = sosemanuk_init(key, iv);

    if strcmp(mode, 'encrypt')
        plaintext = uint8(input);
        ciphertext = sosemanuk_process(ctx, plaintext);
        output = upper(reshape(dec2hex(ciphertext,2)',1,[]));
    else
        % convert hex back to bytes
        cipher_bytes = uint8(sscanf(input, '%2x').');
        plaintext = sosemanuk_process(ctx, cipher_bytes);
        output = char(plaintext);
    end
end
