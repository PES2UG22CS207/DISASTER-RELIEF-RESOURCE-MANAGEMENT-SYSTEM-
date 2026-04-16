package com.disaster.relief_system.service;

import com.disaster.relief_system.entity.User;

public interface AuthService {

    String login(String email, String password);

    String currentSession();

    String logout();

    User getCurrentUser();
}