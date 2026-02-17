package com.jash.matlabcrypto.dto;

public class CryptoRequest {

    private String msg;
    private String key;
    private String iv;
    private String mode;

    public String getMode(){
        return mode;
    }
    public void setMode(String mode){
        this.mode = mode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }
}
