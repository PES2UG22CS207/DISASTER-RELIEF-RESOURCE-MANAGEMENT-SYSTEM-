package com.disaster.relief_system.decorator;

public class BasicNotification implements Notification {

    @Override
    public String send() {
        return "Basic Notification Sent";
    }
}