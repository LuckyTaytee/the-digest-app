package com.sister.thedigest;

import android.app.Activity;
import android.content.Intent;
import android.provider.DocumentsContract;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Activity activity = this;
    private TextInputEditText etloginemail;
    private TextInputEditText etloginpassword;
    private Button btloginlogin;
    private TextView btloginregister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_loginfigma);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);

        etloginemail = findViewById(R.id.etloginemail);
        etloginpassword = findViewById(R.id.etloginpassword);
        btloginlogin = findViewById(R.id.btloginlogin);
        btloginregister = findViewById(R.id.btloginregister);

        btloginlogin.setOnClickListener(this);
        btloginregister.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btloginlogin:{
                userLogin();
                break;
            } case R.id.btloginregister:{
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

    private void userLogin(){
        String email = etloginemail.getText().toString();
        String password = etloginpassword.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(activity, "Insert your email and password.", Toast.LENGTH_SHORT).show();
        } else {
            Retrofit retrofit = new Retrofit.Builder().baseUrl("https://the-digest-app.herokuapp.com/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            JsonApi jsonApi = retrofit.create(JsonApi.class);

            User user = new User(email, password);
            RootUser rootUser = new RootUser(user);

            Call<RootResponseLogin> call = jsonApi.userLogin(rootUser);

            call.enqueue(new Callback<RootResponseLogin>() {
                @Override
                public void onResponse(Call<RootResponseLogin> call, Response<RootResponseLogin> response) {
                    if(response.code() != 200) {
                        Toast.makeText(LoginActivity.this, "Invalid email or password.",
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                        RootResponseLogin rootResponseLogin = response.body();
                        ResponseLogin responseLogin = new ResponseLogin("hold","hold");
                        responseLogin.setToken(rootResponseLogin.getData().getToken());
                        responseLogin.setRenew_token(rootResponseLogin.getData().getRenew_token());
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("Token", responseLogin.getToken());
                        startActivity(intent);
                        Toast.makeText(LoginActivity.this, "Login Successful.",
                                Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Call<RootResponseLogin> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_LONG).show();
                }
            });
        }
    }


}