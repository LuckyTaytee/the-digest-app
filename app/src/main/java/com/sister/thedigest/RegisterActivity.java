package com.sister.thedigest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private Activity activity = this;
    private TextInputEditText etregisteremail;
    private TextInputEditText etregisterpassword;
    private TextInputEditText etregisterconfpassword;
    private Button btregisterregister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_registerfigma);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);

         etregisteremail = findViewById(R.id.etregisteremail);
         etregisterpassword = findViewById(R.id.etregisterpassword);
         etregisterconfpassword = findViewById(R.id.etregisterconfpassword);
         btregisterregister = findViewById(R.id.btregisterregister);

         btregisterregister.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btregisterregister:{
                userRegister();
            }
        }
    }

    private void userRegister() {
        String email = etregisteremail.getText().toString();
        String password = etregisterpassword.getText().toString();
        String confpassword = etregisterconfpassword.getText().toString();

        if (email.isEmpty() || password.isEmpty() || confpassword.isEmpty()) {
            Toast.makeText(activity, "Insert your email and password, then confirm your password.", Toast.LENGTH_LONG).show();
        } if (!password.equals(confpassword)) {
            Toast.makeText(activity, "Password not match, please try again.", Toast.LENGTH_LONG).show();
        } else {
            Retrofit retrofit = new Retrofit.Builder().baseUrl("https://the-digest-app.herokuapp.com/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            JsonApi jsonApi = retrofit.create(JsonApi.class);
            User user = new User(email, password);
            RootUser rootUser = new RootUser(user);
            Call<RootResponseLogin> call = jsonApi.userRegister(rootUser);

            call.enqueue(new Callback<RootResponseLogin>() {
                @Override
                public void onResponse(Call<RootResponseLogin> call, Response<RootResponseLogin> response) {
                    if (response.code() != 200) {
                        Toast.makeText(RegisterActivity.this, "Insert valid email and password must be at least 8 characters.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    RootResponseLogin rootResponseLogin = response.body();
                    ResponseLogin responseLogin = new ResponseLogin("hold","hold");
                    responseLogin.setToken(rootResponseLogin.getData().getToken());
                    responseLogin.setRenew_token(rootResponseLogin.getData().getRenew_token());
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    intent.putExtra("Token", responseLogin.getToken());
                    startActivity(intent);
                    Toast.makeText(RegisterActivity.this, "Register Successful.", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Call<RootResponseLogin> call, Throwable t) {
                    Toast.makeText(RegisterActivity.this, "Error", Toast.LENGTH_LONG).show();
                }
            });

        }
    }
}