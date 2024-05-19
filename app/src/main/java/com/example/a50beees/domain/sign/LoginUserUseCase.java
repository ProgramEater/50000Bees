package com.example.a50beees.domain.sign;

import androidx.annotation.NonNull;

import com.example.a50beees.domain.entites.Status;

import java.util.function.Consumer;



public class LoginUserUseCase {
    private final SignUserRepository repo;

    public LoginUserUseCase(SignUserRepository repo) {
        this.repo = repo;
    }

    public void execute(
            @NonNull String login,
            @NonNull String password,
            Consumer<Status<Void>> callback
    ) {
        repo.login(login, password, (status) -> {
            if (status.getStatusCode() != 200) repo.logout();
            callback.accept(status);
        });

    }
}
