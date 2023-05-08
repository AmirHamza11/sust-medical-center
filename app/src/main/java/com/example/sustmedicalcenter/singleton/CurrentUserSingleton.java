package com.example.sustmedicalcenter.singleton;

import com.example.sustmedicalcenter.model.User;

public final class CurrentUserSingleton {

    private static volatile CurrentUserSingleton INSTANCE = null;

    private User currentUser;

    private CurrentUserSingleton() {}

    public static CurrentUserSingleton getInstance() {
        if (INSTANCE == null) {
            synchronized (CurrentUserSingleton.class) {
                if(INSTANCE == null) {
                    INSTANCE = new CurrentUserSingleton();
                }
            }
        }
        return INSTANCE;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
