package com.sparkle.devicescanner.events;

public class CodeStringEvent {

    public final String device;
    public final String ppid;
    public final String code;

    public CodeStringEvent(String device,String ppid,String topic) {
        this.device = device;
        this.ppid = ppid;
        this.code = topic;
    }
}
