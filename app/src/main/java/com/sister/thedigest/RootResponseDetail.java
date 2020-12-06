package com.sister.thedigest;

import com.google.gson.annotations.SerializedName;

public class RootResponseDetail {
    @SerializedName("data")
    private Article data;

    public RootResponseDetail(Article data) {
        this.data = data;
    }

    public Article getData() {
        return data;
    }
}
