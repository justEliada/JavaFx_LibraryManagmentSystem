package com.example.mangmentsystem.utils;


public class SessionManager {
    private static SessionManager instance;
    private int currentUserId = -1;

    private SessionManager() {

    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void setCurrentUserId(int userId) {
        this.currentUserId = userId;
    }

    public int getCurrentUserId() {
        return currentUserId;
    }
}
