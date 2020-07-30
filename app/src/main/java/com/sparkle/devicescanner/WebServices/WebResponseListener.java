package com.sparkle.devicescanner.WebServices;


public interface WebResponseListener {

    void onError(String message);

    void onResponse(Object response, WebCallType webCallType);
}
