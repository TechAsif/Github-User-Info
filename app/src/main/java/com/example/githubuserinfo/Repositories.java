package com.example.githubuserinfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Repositories extends AppCompatActivity {


    String receivedUserName;
    TextView userNameTV;
    RecyclerView mRecyclerView;
    List<RepositoryModel> myDataSource = new ArrayList<>();
    RecyclerView.Adapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repositories);

        Bundle extras = getIntent().getExtras();
        receivedUserName = extras.getString("username");

        userNameTV = (TextView) findViewById(R.id.userNameTV);

        userNameTV.setText("User: " + receivedUserName);

        mRecyclerView= (RecyclerView) findViewById(R.id.repos_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new ReposAdapter(myDataSource, R.layout.list_item_of_repositories,
                getApplicationContext());

        mRecyclerView.setAdapter(myAdapter);
        loadRepositories();


    }

    public void loadRepositories(){
        //create Retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //auto implement api class help of retrofit

        GithubApi githubApi = retrofit.create(GithubApi.class);

        Call<List<RepositoryModel>> call = githubApi.getRepo(receivedUserName);

        call.enqueue(new Callback<List<RepositoryModel>>() {
            @Override
            public void onResponse(Call<List<RepositoryModel>> call, Response<List<RepositoryModel>> response) {
                myDataSource.clear();
                myDataSource.addAll(response.body());
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<RepositoryModel>> call, Throwable t) {
                // Log error here since request failed
                Log.e("Repos", t.toString());
            }
        });


    }
}
