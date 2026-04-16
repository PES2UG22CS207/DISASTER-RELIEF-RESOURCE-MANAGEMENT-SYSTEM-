package com.disaster.relief_system.service.impl;

import com.disaster.relief_system.entity.User;
import com.disaster.relief_system.repository.UserRepository;
import com.disaster.relief_system.service.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository repository;

    private User loggedInUser = null;

    public AuthServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public String login(String email, String password) {

        for(User user : repository.findAll()) {

            if(user.getEmail().equals(email)
                    && user.getPassword().equals(password)) {

                loggedInUser = user;

                return user.getRole() + " Login Successful";
            }
        }

        return "Invalid Credentials";
    }

    @Override
    public String currentSession() {

        if(loggedInUser == null)
            return "No Active Session";

        return loggedInUser.getName() + " - " + loggedInUser.getRole();
    }

    @Override
    public String logout() {
        loggedInUser = null;
        return "Logged Out Successfully";
    }

    @Override
    public User getCurrentUser() {
        return loggedInUser;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }
}