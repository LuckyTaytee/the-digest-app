package com.sister.thedigest;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.Toast;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements Adaptery.OnArticleListener {

    RecyclerView rvmain;
    List<Article> articleList;

    String token;
    String topic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);

        Intent intent = getIntent();
        token = intent.getStringExtra("Token");

        rvmain = findViewById(R.id.rvmain);
        articleList = new ArrayList<>();

        jsonRequest();
    }

    private void jsonRequest() {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request.Builder ongoing = chain.request().newBuilder();
                        ongoing.addHeader("Authorization", token);
                        return chain.proceed(ongoing.build());
                    }
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://the-digest-app.herokuapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();

        JsonApi jsonApi = retrofit.create(JsonApi.class);

        Call<RootArticle> call = jsonApi.getNews();

        call.enqueue(new Callback<RootArticle>() {
            @Override
            public void onResponse(Call<RootArticle> call, Response<RootArticle> response) {
                if(response.code() != 200) {
                    Toast.makeText(MainActivity.this, "Load News Failed",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                RootArticle rootArticle = response.body();
                articleList = new ArrayList<>(Arrays.asList(rootArticle.getData()));

                PutDataIntoRecyclerView(articleList);
            }

            @Override
            public void onFailure(Call<RootArticle> call, Throwable t) {

            }
        });
    }


    private void PutDataIntoRecyclerView(List<Article> articleList) {
        Adaptery adaptery = new Adaptery(this, articleList, this);
        rvmain.setLayoutManager(new LinearLayoutManager(this));
        rvmain.setAdapter(adaptery);
    }

    @Override
    public void onArticleClick(int position) {
        articleList.get(position);
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("Token", token);
        intent.putExtra("idArticle", articleList.get(position).getId());
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.topic, menu);

        for (int i = 0; i < menu.size(); i++) {
            MenuItem mi = menu.getItem(i);
            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }
            //the method we have create in activity
            applyFontToMenuItem(mi);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.topic_tech:
                topic = "tech";
                jsonRequestTopic();
                break;

            case R.id.topic_sport:
                topic = "sport";
                jsonRequestTopic();
                break;

            case R.id.topic_business:
                topic = "business";
                jsonRequestTopic();
                break;

            case R.id.topic_all:
                jsonRequest();
        }
        return super.onOptionsItemSelected(item);
    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "calibribold.TTF");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    private void jsonRequestTopic() {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request.Builder ongoing = chain.request().newBuilder();
                        ongoing.addHeader("Authorization", token);
                        return chain.proceed(ongoing.build());
                    }
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://the-digest-app.herokuapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();

        JsonApi jsonApi = retrofit.create(JsonApi.class);

        Call<RootArticle> call = jsonApi.getNewsTopic(topic);

        call.enqueue(new Callback<RootArticle>() {
            @Override
            public void onResponse(Call<RootArticle> call, Response<RootArticle> response) {
                if(response.code() != 200) {
                    Toast.makeText(MainActivity.this, "Load News Failed", Toast.LENGTH_LONG).show();
                    return;
                }
                RootArticle rootArticle = response.body();
                articleList = new ArrayList<>(Arrays.asList(rootArticle.getData()));

                PutDataIntoRecyclerView(articleList);
            }

            @Override
            public void onFailure(Call<RootArticle> call, Throwable t) {
                Toast.makeText(MainActivity.this, "JSON Exception", Toast.LENGTH_LONG).show();
            }
        });
    }
}