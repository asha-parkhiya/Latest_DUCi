
package com.sparkle.devicescanner.Model.PPID_TYPE;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FirmwareVersion {

    @SerializedName("protocol")
    @Expose
    private String protocol;
    @SerializedName("firmware")
    @Expose
    private String firmware;

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getFirmware() {
        return firmware;
    }

    public void setFirmware(String firmware) {
        this.firmware = firmware;
    }

}
