addpath('D:\College\sem 5\IS\SOSEMANUK\MATLABsource')

msg  = 'hello';
key  = 'mykey';
iv   = 'myiv';
mode = 'encrypt';

result = main(msg, key, iv, mode);

disp(result);
exit;
