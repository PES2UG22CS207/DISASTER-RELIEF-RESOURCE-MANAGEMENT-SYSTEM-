package com.disaster.relief_system.decorator;

public class EmailNotification extends NotificationDecorator {

    public EmailNotification(Notification notification) {
        super(notification);
    }

    @Override
    public String send() {
        return notification.send() + " + Email Sent";
    }
}