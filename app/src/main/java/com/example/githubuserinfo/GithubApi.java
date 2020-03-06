package com.example.githubuserinfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GithubApi {
    @GET("/users/{user}")
    Call<UserModel> getUser(@Path("user") String user);

    @GET("/users/{user}/repos")
    Call<List<RepositoryModel>> getRepo(@Path("user") String name);
}
