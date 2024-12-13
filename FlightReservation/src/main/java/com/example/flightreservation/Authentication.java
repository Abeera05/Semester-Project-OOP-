package com.example.flightreservation;

import java.util.HashMap;
import java.util.Map;

public class Authentication {

    private static final Map<String, String> users = new HashMap<>();
    private static final Map<String, String> roles = new HashMap<>();

    static {

        users.put("admin", "admin123");
        users.put("passenger1", "pass123");


        roles.put("admin", "admin");
        roles.put("passenger1", "passenger");
    }


    public static String validateLogin(String username, String password) {
        if (users.containsKey(username) && users.get(username).equals(password)) {
            return roles.get(username);
        }
        return null;
    }
    public static boolean resetPassword(String username, String oldPassword, String newPassword) {
        if (users.containsKey(username) && users.get(username).equals(oldPassword)) {
            users.put(username, newPassword);
            return true; // Password reset successfully
        }
        return false; // Invalid username or old password
    }
}