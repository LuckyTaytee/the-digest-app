package com.sister.thedigest;

import com.google.gson.annotations.SerializedName;

public class RootResponseLogin {

    @SerializedName("data")
    private ResponseLogin data;

    public RootResponseLogin(ResponseLogin data) {
        this.data = data;
    }

    public ResponseLogin getData() {
        return data;
    }

    public void setData(ResponseLogin data) {
        this.data = data;
    }
}
