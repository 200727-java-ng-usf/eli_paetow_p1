package com.revature.services;

import com.revature.exceptions.AuthenticationException;
import com.revature.exceptions.InvalidRequestException;
import com.revature.exceptions.ResourceNotFoundException;
import com.revature.models.ErsUser;
import com.revature.repos.UserRepository;

import java.util.Optional;
import java.util.Set;


public class UserService {

    private UserRepository userRepo = new UserRepository();


    public void register(ErsUser newUser) {

        if (!isUserValid(newUser)) {
            throw new InvalidRequestException("Invalid user field values provided during registration!");
        }

        Optional<ErsUser> existingUser = userRepo.findUserByUsername(newUser.getUsername());
        if (existingUser.isPresent()) {
            throw new RuntimeException("Provided username is already in use!");
        }


        userRepo.save(newUser);
        System.out.println(newUser);


    }


    public Set<ErsUser> getAllUsers() {

        Set<ErsUser> users = userRepo.findAllUsers();

        if (users.isEmpty()) {
            throw new ResourceNotFoundException();
        }

        return users;
    }


    public ErsUser authenticate(String username, String password) {

        // validate that the provided username and password are not non-values
        if (username == null || username.trim().equals("") || password == null || password.trim().equals("")) {
            throw new InvalidRequestException("Invalid credential values provided!");
        }

        return userRepo.findUserByCredentials(username, password)
                .orElseThrow(AuthenticationException::new);


    }

    public ErsUser getUserById(int id) {

        if (id <= 0) {
            throw new InvalidRequestException("The provided id cannot be less than or equal to zero.");
        }

        return userRepo.findUserById(id)
                .orElseThrow(ResourceNotFoundException::new);

    }


    public boolean isUsernameAvailable(String username) {
        ErsUser user = userRepo.findUserByUsername(username).orElse(null); // todo custom exception?
        return user == null;
    }


    public boolean isEmailAvailable(String email) {
        ErsUser user = userRepo.findUserByEmail(email).orElse(null); // todo custom exception?
        return user == null;
    }


    public void update(ErsUser updatedUser) {

        if (!isUserValid(updatedUser)) {
            throw new InvalidRequestException("User not found");
        }

        userRepo.update(updatedUser);

    }


    public boolean isUserValid(ErsUser user) {
        if (user == null) return false;
        if (user.getFirstName() == null || user.getFirstName().trim().equals("")) return false;
        if (user.getLastName() == null || user.getLastName().trim().equals("")) return false;
        if (user.getUsername() == null || user.getUsername().trim().equals("")) return false;
        if (user.getPassword() == null || user.getPassword().trim().equals("")) return false;
        return true;
    }


}


