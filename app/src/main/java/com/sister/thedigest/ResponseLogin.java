package com.sister.thedigest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseLogin {

    @SerializedName("renew_token")
    @Expose
    private String renew_token;

    @SerializedName("token")
    @Expose
    private String token;

    public ResponseLogin(String renew_token, String token) {
        this.renew_token = renew_token;
        this.token = token;
    }

    public String getRenew_token() {
        return renew_token;
    }

    public void setRenew_token(String renew_token) {
        this.renew_token = renew_token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
