
package com.sparkle.devicescanner.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WifiName {

    @SerializedName("hello")
    @Expose
    private String string;

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

}
