package com.test.edicourier.gui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.test.edicourier.Constants;
import com.test.edicourier.NetworkEngine;
import com.test.edicourier.R;
import com.test.edicourier.SessionToken;
import com.test.edicourier.StorageService;
import com.test.edicourier.TypedNetworkCallback;
import com.test.edicourier.model.Token;
import com.test.edicourier.utils.Utilities;

public class LoginActivity extends AppCompatActivity {

    private final static String TAG = "SUPERLOG_LoginActivity";
    private String login = null;
    private String password = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText editTextLogin = findViewById(R.id.editTextLogin);
        EditText editTextPassword = findViewById(R.id.editTextPassword);
        Button buttonSignIn = findViewById(R.id.buttonSing);

        String loginStorage = StorageService.getData(LoginActivity.this, Constants.APP_PREFERENCES_LOGIN);
        String passwordStorage = StorageService.getData(LoginActivity.this, Constants.APP_PREFERENCES_PASSWORD);

        if (!"".equals(loginStorage) && !"".equals(passwordStorage)) {
            editTextLogin.setText(loginStorage);
            editTextPassword.setText(passwordStorage);
        }

        buttonSignIn.setOnClickListener(v -> {
            if (Utilities.getInstance().isOnline(LoginActivity.this)) {
                login = editTextLogin.getText().toString();
                password = editTextPassword.getText().toString();
                NetworkEngine.getInstance().loadToken(login, password, getTokenCallback());
            } else {
                Toast.makeText(getApplicationContext(), getText(R.string.no_internet), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private TypedNetworkCallback<Token> getTokenCallback() {
        return new TypedNetworkCallback<Token>() {
            @Override
            public void onLoad(Token token) {
                SessionToken.setToken(token.getToken()); // Храним токен
                StorageService.saveData(login, LoginActivity.this, Constants.APP_PREFERENCES_LOGIN);
                StorageService.saveData(password, LoginActivity.this, Constants.APP_PREFERENCES_PASSWORD);
                Intent intent = new Intent(LoginActivity.this, ApplicationActivity.class);
                startActivity(intent);
            }

            @Override
            public void onError(String errorString) {
                Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
            }
        };
    }
}
