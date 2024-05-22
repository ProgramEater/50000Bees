package com.example.a50beees.domain;

import androidx.annotation.NonNull;

import com.example.a50beees.domain.entites.FullUserEntity;
import com.example.a50beees.domain.entites.Status;

import java.util.function.Consumer;

public class GetUserByUsernameUseCase {
    private final UserRepository repo;

    public GetUserByUsernameUseCase(UserRepository repo) {
        this.repo = repo;
    }

    public void execute(@NonNull String username, @NonNull Consumer<Status<FullUserEntity>> callback) {
        repo.getUserByUsername(username, callback);
    }
}
