package com.unmcelearning.android.thyroidpathology;

/**
 * Created by adamk_000 on 11/12/2016.
 */

public class Users {

    public String email;
    public String username;

    public int totalScore;

    public Users() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Users(String email) {
        this.email = email;
        setUsername(email);
        username = getUsername();
        this.totalScore = 0;

    }

    public Users(String email, boolean newUser) {
        this.email = email;
        setUsername(email);
        username = getUsername();
    }

    private void setUsername(String email) {
        int atIndex = email.indexOf("@");
        username = email.substring(0, atIndex);
        username = username.replace(".", "");
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public int getTotalScore() {
        return totalScore;
    }
}
