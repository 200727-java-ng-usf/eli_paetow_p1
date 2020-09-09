package com.revature.services;

import com.revature.exceptions.AuthenticationException;
import com.revature.exceptions.InvalidRequestException;
import com.revature.exceptions.ResourceNotFoundException;
import com.revature.models.User;
import com.revature.models.Role;
import com.revature.repos.UserRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class  UserService {

//    public static AppState app = new AppState();

    private UserRepository userRepo = new UserRepository();


    public User authenticate(String username, String password) {

        // validate that the provided username and password are not non-values
        if (username == null || username.trim().equals("") || password == null || password.trim().equals("")) {
            throw new InvalidRequestException("Invalid credential values provided!");
        }


        return userRepo.findUserByCredentials(username, password)
                .orElseThrow(AuthenticationException::new);

//        app.setCurrentUser(authUser);

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
    public Set<User> getAllUsers() {

        Set<User> users = userRepo.findAllUsers();

        if (users.isEmpty()) {
            throw new ResourceNotFoundException();
        }

        return users;
    }
    public void register(User newUser) {

        if (!isUserValid(newUser)) {
            throw new InvalidRequestException("Invalid user field values provided during registration!");
        }

        Optional<User> existingUser = userRepo.findUserByUsername(newUser.getUsername());
        if (existingUser.isPresent()) {
            // TODO implement a custom ResourcePersistenceException
            throw new RuntimeException("Provided username is already in use!");
        }

        newUser.setUserRole(Role.EMPLOYEE);
        userRepo.save(newUser);
        System.out.println(newUser);
//        app.setCurrentUser(newUser);

    }
    public Set<User> getUsersByRole() {
        return new HashSet<>();
    }

    public User getUserById(int id) {
        if(id <= 0 ){
            throw new InvalidRequestException("id cannot be less than 0");

        }
        return userRepo.findUserById(id)
                .orElseThrow(ResourceNotFoundException::new);

    }

    public User getUserByUsername(String username) {
        return null;
    }

    public boolean deleteUserById(int id) {
        return false;
    }




}
