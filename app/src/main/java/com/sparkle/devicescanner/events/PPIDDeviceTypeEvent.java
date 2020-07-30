package com.sparkle.devicescanner.events;

public class PPIDDeviceTypeEvent {

    public final String device;
    public final String ppid;

    public PPIDDeviceTypeEvent(String device, String ppid) {
        this.device = device;
        this.ppid = ppid;
    }
}
