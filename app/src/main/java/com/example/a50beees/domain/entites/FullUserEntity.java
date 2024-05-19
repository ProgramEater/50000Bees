package com.example.a50beees.domain.entites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FullUserEntity {
    @NonNull
    private final String id;
    @NonNull
    private final String username;
    @Nullable
    private final String photoUrl;
    @Nullable
    private final String email;

    public FullUserEntity(
            @NonNull String id,
            @NonNull String username,
            @Nullable String photoUrl,
            @Nullable String email
    ) {
        this.id = id;
        this.username = username;
        this.photoUrl = photoUrl;
        this.email = email;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getUsername() {
        return username;
    }

    @Nullable
    public String getPhotoUrl() {
        return photoUrl;
    }

    @Nullable
    public String getEmail() {
        return email;
    }
}
