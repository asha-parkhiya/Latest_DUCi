package com.sparkle.devicescanner.events;

/**
 * Created by josejuansanchez on 05/10/16.
 */

public class ProtocolCommandEvent {
    public final String name;
    public final String command;

    public ProtocolCommandEvent(String name, String command) {
        this.name = name;
        this.command = command;
    }
}