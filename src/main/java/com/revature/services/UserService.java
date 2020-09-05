package com.revature.services;

import com.revature.exceptions.AuthenticationException;
import com.revature.exceptions.InvalidRequestException;
import com.revature.models.User;
import com.revature.models.Role;
import com.revature.repos.UserRepository;
import com.revature.util.AppState;

public class  UserService {

    private UserRepository userRepo;
    public static AppState app = new AppState();

    public UserService(UserRepository repo) {
        userRepo = repo;
    }


    public void authenticate(String username, String password) {

        // validate that the provided username and password are not non-values
        if (username == null || username.trim().equals("") || password == null || password.trim().equals("")) {
            throw new InvalidRequestException("Invalid credential values provided!");
        }


        User authUser = userRepo.findUserByCredentials(username, password)
                .orElseThrow(AuthenticationException::new);

        app.setCurrentUser(authUser);

    }

    //is the user valid. check if fields are not null or empty
    public boolean isUserValid(User user) {
        if (user == null) return false;
        if (user.getFirstName() == null || user.getFirstName().trim().equals("")) return false;
        if (user.getLastName() == null || user.getLastName().trim().equals("")) return false;
        if (user.getUsername() == null || user.getUsername().trim().equals("")) return false;
        if (user.getPassword() == null || user.getPassword().trim().equals("")) return false;
        if (user.getEmail() == null || user.getEmail().trim().equals("")) return false;
        return true;
    }

    //update user with the new values if the user is valid

    public void update(User ersUser) {

        if (!isUserValid(ersUser)) {
            throw new InvalidRequestException("User not found...");
        }

        ersUser.setUsername(ersUser.getUsername());
        ersUser.setPassword(ersUser.getPassword());
        ersUser.setFirstName(ersUser.getFirstName());
        ersUser.setLastName(ersUser.getLastName());
        ersUser.setEmail(ersUser.getEmail());
        ersUser.setUserRole(ersUser.getUserRole());
        userRepo.update(ersUser);
    }

}
