package com.example.a50beees.ui.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.a50beees.R;
import com.example.a50beees.data.UserRepositoryImpl;
import com.example.a50beees.data.source.CredentialsDataSource;
import com.example.a50beees.domain.sign.IsUserExistUseCase;
import com.example.a50beees.domain.sign.LoginUserUseCase;

public class LoginActivity extends AppCompatActivity {
    EditText login_edit, password_edit;
    LoginUserUseCase loginUserUseCase = new LoginUserUseCase(UserRepositoryImpl.getInstance());
    IsUserExistUseCase isUserExistUseCase = new IsUserExistUseCase(UserRepositoryImpl.getInstance());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_edit = (EditText) findViewById(R.id.username);
        password_edit = (EditText) findViewById(R.id.password);

        // if executed from successful registration
        login_edit.setText(getIntent().getStringExtra("login"));
        password_edit.setText(getIntent().getStringExtra("password"));

        if (CredentialsDataSource.getInstance().getAuthData() != null) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }

        Button accept_btn = (Button) findViewById(R.id.loginButton);
        accept_btn.setOnClickListener(v -> {
            final String login = login_edit.getText().toString();
            final String password = password_edit.getText().toString();
            if (checkAuth()) {
                isUserExistUseCase.execute(login, status -> {
                    if (status.getValue() == null || status.getErrors() != null) {
                        Toast.makeText(this, "Something wrong with the server. Try later =(", Toast.LENGTH_SHORT).show();
                        Log.i("11", status.getErrors() == null ? String.valueOf(status.getStatusCode()) : status.getErrors().toString());
                        return;
                    }
                    // exists
                    if (status.getValue()) {
                        loginUser(login, password);
                    } else {
                        // doesn't exist
                        Intent i = new Intent(this, RegistrationActivity.class);
                        i.putExtra("login", login_edit.getText().toString());
                        i.putExtra("password", password_edit.getText().toString());
                        startActivity(i);
                    }
                });
            }
        });

        Button registration_btn = (Button) findViewById(R.id.signupButton);
        registration_btn.setOnClickListener(v -> {
            Intent i = new Intent(this, RegistrationActivity.class);
            i.putExtra("login", login_edit.getText().toString());
            i.putExtra("password", password_edit.getText().toString());
            startActivity(i);
        });
    }

    private void loginUser(String login, String password) {
        loginUserUseCase.execute(login, password, logStatus -> {
            if (logStatus.getStatusCode() == 200 && logStatus.getErrors() == null) {
                // login is successful => to forms activity
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
            } else {
                Toast.makeText(this, "login or password data is wrong", Toast.LENGTH_SHORT).show();
                Log.i("111", logStatus.toString());
            }
        });
    }

    private boolean checkAuth() {
        if (login_edit.getText().toString().isEmpty()) {
            Toast.makeText(this, "Login can't be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password_edit.getText().toString().isEmpty()) {
            Toast.makeText(this, "Password can't be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
