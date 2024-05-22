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
import com.example.a50beees.domain.sign.CreateUserUseCase;
import com.example.a50beees.domain.sign.IsUserExistUseCase;


public class RegistrationActivity extends AppCompatActivity {
    EditText login_edit, password_edit, password_edit2;
    CreateUserUseCase createUserUseCase = new CreateUserUseCase(UserRepositoryImpl.getInstance());
    IsUserExistUseCase isUserExistUseCase = new IsUserExistUseCase(UserRepositoryImpl.getInstance());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        login_edit = (EditText) findViewById(R.id.userLogin);
        password_edit = (EditText) findViewById(R.id.userPassword);
        password_edit2 = (EditText) findViewById(R.id.userPassword2);

        login_edit.setText(getIntent().getStringExtra("login"));
        password_edit.setText(getIntent().getStringExtra("password"));

        Button btn = (Button) findViewById(R.id.registrationButton);
        btn.setOnClickListener(v -> {
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
                        Toast.makeText(this, "User with that username already exists", Toast.LENGTH_SHORT).show();
                    } else {
                        createUserUseCase.execute(login, password, createStatus -> {
                            if (createStatus.getStatusCode() == 201 && createStatus.getErrors() == null) {
                                Intent i = new Intent(this, LoginActivity.class);
                                i.putExtra("login", login_edit.getText().toString());
                                i.putExtra("password", password_edit.getText().toString());
                                startActivity(i);
                            } else {
                                Toast.makeText(this, "Failed to register new user", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
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

        if (!password_edit.getText().toString().equals(password_edit2.getText().toString())) {
            Toast.makeText(this, "Passwords don't match", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
