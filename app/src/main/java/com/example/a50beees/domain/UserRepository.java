package com.example.a50beees.domain;

import androidx.annotation.NonNull;

import com.example.a50beees.domain.entites.FullUserEntity;
import com.example.a50beees.domain.entites.ItemUserEntity;
import com.example.a50beees.domain.entites.Status;

import java.util.List;
import java.util.function.Consumer;

public interface UserRepository {
    void getAllUsers(@NonNull Consumer<Status<List<ItemUserEntity>>> callback);

    void getUser(@NonNull String id, @NonNull Consumer<Status<FullUserEntity>> callback);
}
