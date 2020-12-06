package com.sister.thedigest;

import com.google.gson.annotations.SerializedName;

public class RootUser {

    @SerializedName("user")
    private User user;


    public RootUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
