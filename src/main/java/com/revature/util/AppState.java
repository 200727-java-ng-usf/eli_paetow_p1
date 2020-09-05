package com.revature.util;

import com.revature.models.Reimbursement;
import com.revature.models.User;
import com.revature.repos.UserRepository;
import com.revature.services.UserService;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class AppState {

    private BufferedReader console;
    private User currentUser;
    private boolean appRunning;
    private Reimbursement currentReimbursement;

//    private ReimbRepository reimbRepo = new ReimbRepository();

    public AppState() {
        appRunning = true;
        console = new BufferedReader(new InputStreamReader(System.in));

        final UserRepository userRepo = new UserRepository();
//        final ReimbRepository reimbRepo = new ReimbRepository();
        final UserService userService = new UserService(userRepo);
//        final ReimbService reimbService = new ReimbService(reimbRepo);

    }

    public BufferedReader getConsole() {
        return console;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public boolean isAppRunning() {
        return appRunning;
    }

    public void setAppRunning(boolean appRunning) {
        this.appRunning = appRunning;
    }

    public Reimbursement getCurrentReimbursement() {
        return currentReimbursement;
    }

    public void setCurrentReimbursement(Reimbursement currentReimbursement) {
        this.currentReimbursement = currentReimbursement;
    }

    public void invalidateCurrentSession() {
        currentUser = null;
    }

    public boolean isSessionValid() {
        return (this.currentUser != null);
    }
}
