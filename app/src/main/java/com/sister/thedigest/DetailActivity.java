package com.sister.thedigest;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Path;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    Activity activity = this;
    ImageView ivdetailimg;
    TextView tvdetailauthor;
    TextView tvdetailtime;
    TextView tvdetailtitle;
    TextView tvdetailcontent;
    TextView tvdetailby;
    TextView hiddenbutton;

    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_detail);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);

        Intent intent = getIntent();
        token = intent.getStringExtra("Token");
        String id = getIntent().getStringExtra("idArticle");

        ivdetailimg = findViewById(R.id.ivdetailimg);
        tvdetailauthor = findViewById(R.id.tvdetailauthor);
        tvdetailtitle = findViewById(R.id.tvdetailtitle);
        tvdetailtime = findViewById(R.id.tvdetailtime);
        tvdetailcontent = findViewById(R.id.tvdetailcontent);
        tvdetailby = findViewById(R.id.tvdetailby);
        hiddenbutton = findViewById(R.id.hiddenbutton);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("My Notification", "My Notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        hiddenbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String message = "New Article for You.";
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                                DetailActivity.this, "My Notification"
                        )
                                .setSmallIcon(R.drawable.news)
                                .setContentTitle("The Digest")
                                .setContentText(message)
                                .setAutoCancel(true);

                        Intent intent = new Intent(DetailActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("Token", token);

                        PendingIntent pendingIntent = PendingIntent.getActivity(DetailActivity.this,
                                0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
                        builder.setContentIntent(pendingIntent);

                        NotificationManager notificationManager = (NotificationManager)getSystemService(
                                Context.NOTIFICATION_SERVICE

                        );
                        notificationManager.notify(1, builder.build());
                    }
                }, 10000);
            }
        });

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

        Call<RootResponseDetail> call = jsonApi.getNewsId(id);

        call.enqueue(new Callback<RootResponseDetail>() {
            @Override
            public void onResponse(Call<RootResponseDetail> call, Response<RootResponseDetail> response) {
                RootResponseDetail rootResponseDetail = response.body();

                putDataIntoLayout(rootResponseDetail);
            }

            @Override
            public void onFailure(Call<RootResponseDetail> call, Throwable t) {

            }
        });
    }

    private void putDataIntoLayout(RootResponseDetail rootResponseDetail) {
        String img = rootResponseDetail.getData().getImg();
        String author = rootResponseDetail.getData().getAuthor();
        String title = rootResponseDetail.getData().getTitle();
        String time = rootResponseDetail.getData().getPubTime();
        String content = rootResponseDetail.getData().getContent();

        Glide.with(this)
                .load(img)
                .into(ivdetailimg);

        tvdetailauthor.setText(author);
        tvdetailtitle.setText(title);
        tvdetailtime.setText(time);
        tvdetailcontent.setText(content);
        tvdetailby.setText("By ");
    }

}