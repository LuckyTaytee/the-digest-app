package com.sister.thedigest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RootArticle {

    @SerializedName("data")
    @Expose
    public Article[] data;

    public Article[] getData() {
        return data;
    }

    public void setData(Article[] data) {
        this.data = data;
    }
}
