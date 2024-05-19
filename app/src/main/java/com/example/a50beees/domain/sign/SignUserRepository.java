package com.example.a50beees.domain.sign;

import androidx.annotation.NonNull;

import com.example.a50beees.domain.entites.Status;

import java.util.function.Consumer;



public interface SignUserRepository {
    void isExistUser(@NonNull String login, Consumer<Status<Void>> callback);
    void createAccount(
            @NonNull String login,
            @NonNull String password,
            Consumer<Status<Void>> callback
    );
    void login(
            @NonNull String login,
            @NonNull String password,
            Consumer<Status<Void>> callback
    );

    void logout();
}
