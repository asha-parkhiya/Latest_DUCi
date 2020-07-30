package com.sparkle.devicescanner.Model.PPID_TYPE;

public class DeviceType {

    String PPID;
    String code;
    String deviceType;

    public DeviceType() {
    }

    public DeviceType(String PPID, String codehasformate, String deviceType) {
        this.PPID = PPID;
        this.code = codehasformate;
        this.deviceType = deviceType;
    }

    public String getPPID() {
        return PPID;
    }

    public void setPPID(String PPID) {
        this.PPID = PPID;
    }

    public String getcode() {
        return code;
    }

    public void setcode(String codehasformate) {
        this.code = codehasformate;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }
}
