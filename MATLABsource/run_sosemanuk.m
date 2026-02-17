addpath('D:/College/sem 5/IS/project')

msg  = getenv('MSG');
key  = getenv('KEY');
iv   = getenv('IV');
mode = getenv('MODE');

result = main(msg, key, iv, mode);

disp(result);
exit;
