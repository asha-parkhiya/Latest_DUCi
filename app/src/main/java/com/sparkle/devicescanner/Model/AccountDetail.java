package com.sparkle.devicescanner.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccountDetail {

    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("firstname")
    @Expose
    private String firstname;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("lastname")
    @Expose
    private String lastname;

    @SerializedName("codehasformate")
    @Expose
    private String codehasformate;

    public String getCode() {
    return code;
    }

    public void setCode(String code) {
    this.code = code;
    }

    public String getFirstname() {
    return firstname;
    }

    public void setFirstname(String firstname) {
    this.firstname = firstname;
    }

    public String getId() {
    return id;
    }

    public void setId(String id) {
    this.id = id;
    }

    public String getLastname() {
    return lastname;
    }

    public void setLastname(String lastname) {
    this.lastname = lastname;
    }

    public String getCodehasformate() {
        return codehasformate;
    }

    public void setCodehasformate(String codehasformate) {
        this.codehasformate = codehasformate;
    }
}