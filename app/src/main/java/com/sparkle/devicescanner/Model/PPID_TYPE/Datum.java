
package com.sparkle.devicescanner.Model.PPID_TYPE;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("productItemID")
    @Expose
    private Integer productItemID;
    @SerializedName("productModelID")
    @Expose
    private Integer productModelID;
    @SerializedName("productBatchID")
    @Expose
    private Integer productBatchID;
    @SerializedName("productItemOEM_SN")
    @Expose
    private String productItemOEMSN;
    @SerializedName("productItemPAYG_SN")
    @Expose
    private String productItemPAYGSN;
    @SerializedName("lifeCycleStatus")
    @Expose
    private String lifeCycleStatus;
    @SerializedName("firmwareVersion")
    @Expose
    private FirmwareVersion firmwareVersion;

    public Integer getProductItemID() {
        return productItemID;
    }

    public void setProductItemID(Integer productItemID) {
        this.productItemID = productItemID;
    }

    public Integer getProductModelID() {
        return productModelID;
    }

    public void setProductModelID(Integer productModelID) {
        this.productModelID = productModelID;
    }

    public Integer getProductBatchID() {
        return productBatchID;
    }

    public void setProductBatchID(Integer productBatchID) {
        this.productBatchID = productBatchID;
    }

    public String getProductItemOEMSN() {
        return productItemOEMSN;
    }

    public void setProductItemOEMSN(String productItemOEMSN) {
        this.productItemOEMSN = productItemOEMSN;
    }

    public String getProductItemPAYGSN() {
        return productItemPAYGSN;
    }

    public void setProductItemPAYGSN(String productItemPAYGSN) {
        this.productItemPAYGSN = productItemPAYGSN;
    }

    public String getLifeCycleStatus() {
        return lifeCycleStatus;
    }

    public void setLifeCycleStatus(String lifeCycleStatus) {
        this.lifeCycleStatus = lifeCycleStatus;
    }

    public FirmwareVersion getFirmwareVersion() {
        return firmwareVersion;
    }

    public void setFirmwareVersion(FirmwareVersion firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }

}
