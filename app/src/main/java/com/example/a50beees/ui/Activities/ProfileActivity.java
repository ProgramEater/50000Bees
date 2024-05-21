package com.example.a50beees.ui.Activities;

import androidx.appcompat.app.AppCompatActivity;

import com.example.a50beees.data.UserRepositoryImpl;
import com.example.a50beees.domain.GetUserByIdUseCase;
import com.example.a50beees.domain.UserRepository;

public class ProfileActivity extends AppCompatActivity {
    public ProfileActivity() {
        GetUserByIdUseCase getUserByIdUseCase = new GetUserByIdUseCase(UserRepositoryImpl.getInstance().getUser();)
    }
}
