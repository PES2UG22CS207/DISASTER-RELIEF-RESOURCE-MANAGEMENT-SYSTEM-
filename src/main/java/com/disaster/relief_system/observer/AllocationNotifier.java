package com.disaster.relief_system.observer;

import java.util.ArrayList;
import java.util.List;

public class AllocationNotifier {

    private List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void notifyAllObservers(String message) {

        for (Observer obs : observers) {
            obs.update(message);
        }
    }
}