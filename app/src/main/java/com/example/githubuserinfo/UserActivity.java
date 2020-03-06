package com.example.githubuserinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserActivity extends AppCompatActivity {

    ImageView avatarImg;
    TextView userNameTV;
    TextView followersTV;
    TextView followingTV;
    TextView logIn;
    TextView email;

    Bundle extras;
    String newString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        avatarImg = findViewById(R.id.avatar);
        userNameTV = (TextView) findViewById(R.id.username);
        followersTV = (TextView) findViewById(R.id.followers);
        followingTV = (TextView) findViewById(R.id.following);
        logIn = (TextView) findViewById(R.id.logIn);
        email = (TextView) findViewById(R.id.email);

        extras = getIntent().getExtras();
        newString = extras.getString("STRING_I_NEED");

        //create Retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //auto implement api class help of retrofit

        GithubApi githubApi = retrofit.create(GithubApi.class);

        //call getPosts auto implemented method..it does low level networking.

        Call<UserModel> call = githubApi.getUser(newString);

        //now we will execute call and get response back

        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                UserModel userModel = response.body();


                if(response.body().getName() == null){
                    userNameTV.setText("No name provided");
                } else {
                    userNameTV.setText("Username: " + response.body().getName());
                }
                Glide.with(getApplicationContext()).
                        load(userModel.getAvatar()).apply(new RequestOptions().override(220, 220)).
                        into(avatarImg);
                followersTV.setText("Followers: " + userModel.getFollowers());
                followingTV.setText("Following: " + userModel.getFollowing());
                logIn.setText("LogIn: " + response.body().getLogin());
                if(response.body().getEmail() == null){
                    email.setText("No email provided");
                } else{
                    email.setText("Email: " + response.body().getEmail());
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {

            }
        });


    }

    public void loadOwnRepos(View view) {
        Intent intent = new Intent(UserActivity.this,Repositories.class);
        intent.putExtra("username", newString);
        startActivity(intent);
    }
}
