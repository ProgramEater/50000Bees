package com.example.a50beees.ui.Activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.a50beees.R;
import com.example.a50beees.data.UserRepositoryImpl;
import com.example.a50beees.data.source.CredentialsDataSource;
import com.example.a50beees.domain.GetUserByUsernameUseCase;
import com.example.a50beees.domain.entites.FullUserEntity;

public class ProfileActivity extends AppCompatActivity {
    FullUserEntity user;
    TextView username_view, email_view;
    ImageView pfp_view;
    Button logout_button;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        GetUserByUsernameUseCase getUserByUsernameUseCase = new GetUserByUsernameUseCase(UserRepositoryImpl.getInstance());

        if (CredentialsDataSource.getInstance().getLogin() != null) {
            getUserByUsernameUseCase.execute(CredentialsDataSource.getInstance().getLogin(), status -> {
                if (status.getStatusCode() == 200 && status.getErrors() == null) {
                    user = status.getValue();
                    username_view = findViewById(R.id.username_textview);
                    email_view = findViewById(R.id.email_textview);
                    pfp_view = findViewById(R.id.profile_image_self);

                    username_view.setText(user.getUsername());
                    email_view.setText(user.getEmail());
                } else {
                    Log.i("ProfileActivity", String.format("failed to load user with username %s", CredentialsDataSource.getInstance().getLogin()));
                    Toast.makeText(this, "Failed to load user", Toast.LENGTH_SHORT).show();
                }
            });
        }

        logout_button = findViewById(R.id.logout_btn_profile);
        logout_button.setOnClickListener(v -> {
            CredentialsDataSource.getInstance().logout();
            finish();
        });

    }
}
