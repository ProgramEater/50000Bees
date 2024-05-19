package com.example.a50beees.data.source;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import okhttp3.Credentials;

public class CredentialsDataSource {
    private static CredentialsDataSource INSTANCE;

    private CredentialsDataSource() {}

    public static synchronized CredentialsDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CredentialsDataSource();
        }
        return INSTANCE;
    }

    @Nullable
    private String authData = null;

    @Nullable
    private String login = null;

    @Nullable
    public String getAuthData() {
        return authData;
    }

    @Nullable
    public String getLogin() {
        return login;
    }

    public void updateLogin(@NonNull String login, @NonNull String password) {
        this.login = login;
        authData = Credentials.basic(login, password);
    }

    public void logout() {
        login = null;
        authData = null;
    }
}