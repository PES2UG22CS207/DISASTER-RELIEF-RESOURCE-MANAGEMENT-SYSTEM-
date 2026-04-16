package com.disaster.relief_system.observer;

public class CampObserver implements Observer {

    @Override
    public void update(String message) {
        System.out.println("Camp Alert: " + message);
    }
}