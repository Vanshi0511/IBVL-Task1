package com.vanshika.ibvltask1.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vanshika.ibvltask1.R;
import com.vanshika.ibvltask1.utils.TokenManager;
import com.vanshika.ibvltask1.model.LoginRequest;
import com.vanshika.ibvltask1.model.LoginResponse;
import com.vanshika.ibvltask1.network.ApiService;
import com.vanshika.ibvltask1.network.RetrofitClient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText emailEt, passwordEt;
    Button loginBtn;
    TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEt = findViewById(R.id.email);
        passwordEt = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginButton);

        try {
            tokenManager = new TokenManager(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        loginBtn.setOnClickListener(v -> {
            String email = emailEt.getText().toString();
            String password = passwordEt.getText().toString();
            login(email, password);
        });
    }

    private void login(String email, String password)


    {
        LoginRequest request = new LoginRequest(email, password);
        ApiService api = RetrofitClient.getApiService();

        Log.d("LoginRequest", "Attempting login with: " + email);

        api.login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();

                if (response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();
                    String json = gson.toJson(loginResponse); // convert response body to JSON string
                    Log.d("LoginSuccess", "Response JSON:\n" + json);

                    tokenManager.saveToken(loginResponse.getToken());
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    try {
                        ResponseBody errorBody = response.errorBody();
                        if (errorBody != null) {
                            String errorJson = errorBody.string();
                            Log.e("LoginFailed", "Error JSON:\n" + errorJson);
                        }
                    } catch (Exception e) {
                        Log.e("LoginFailed", "Error reading error body", e);
                    }
                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("LoginError", "Login API call failed: " + t.getMessage(), t);
                Toast.makeText(LoginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
