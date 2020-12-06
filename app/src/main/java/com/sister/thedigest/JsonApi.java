package com.sister.thedigest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface JsonApi {

    @GET("articles")
    Call<RootArticle> getNews();

    @GET("articles/{id}")
    Call<RootResponseDetail> getNewsId(
            @Path("id") String id
    );

    @GET("articles/topic/{topic}")
    Call<RootArticle> getNewsTopic(
            @Path("topic") String topic
    );

    @POST("session")
    Call<RootResponseLogin> userLogin(@Body RootUser rootUser);

    @POST("registration")
    Call<RootResponseLogin> userRegister(@Body RootUser rootUser);
}
