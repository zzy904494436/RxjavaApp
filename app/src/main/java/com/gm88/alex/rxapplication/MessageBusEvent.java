package com.gm88.alex.rxapplication;
//
public class MessageBusEvent {
    private String msg;
    public MessageBusEvent(String msg){
        this.msg = msg ;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
