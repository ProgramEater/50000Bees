package com.example.a50beees.domain;

import androidx.annotation.NonNull;

import com.example.a50beees.domain.entites.ItemUserEntity;
import com.example.a50beees.domain.entites.Status;

import java.util.List;
import java.util.function.Consumer;

public class GetUsersListUseCase {
    private final UserRepository repo;

    public GetUsersListUseCase(UserRepository repo) {
        this.repo = repo;
    }

    public void execute(@NonNull Consumer<Status<List<ItemUserEntity>>> callback) {
        repo.getAllUsers(callback);
    }
}
