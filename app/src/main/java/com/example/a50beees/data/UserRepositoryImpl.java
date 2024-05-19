package com.example.a50beees.data;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.example.a50beees.data.dto.AccountDto;
import com.example.a50beees.data.dto.UserDto;
import com.example.a50beees.data.network.RetrofitFactory;
import com.example.a50beees.data.source.CredentialsDataSource;
import com.example.a50beees.data.source.UserApi;
import com.example.a50beees.data.utils.CallToConsumer;
import com.example.a50beees.domain.UserRepository;
import com.example.a50beees.domain.entites.FullUserEntity;
import com.example.a50beees.domain.entites.ItemUserEntity;
import com.example.a50beees.domain.entites.Status;
import com.example.a50beees.domain.sign.SignUserRepository;

public class UserRepositoryImpl implements UserRepository, SignUserRepository {
    private static UserRepositoryImpl INSTANCE;
    private UserApi userApi = RetrofitFactory.getInstance().getUserApi();
    private final CredentialsDataSource credentialsDataSource = CredentialsDataSource.getInstance();

    private UserRepositoryImpl() {}

    public static synchronized UserRepositoryImpl getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserRepositoryImpl();
        }
        return INSTANCE;
    }

    @Override
    public void getAllUsers(@NonNull Consumer<Status<List<ItemUserEntity>>> callback) {
        userApi.getAll().enqueue(new CallToConsumer<>(
                callback,
                usersDto -> {
                    ArrayList<ItemUserEntity> result = new ArrayList<>(usersDto.size());
                    for (UserDto user : usersDto) {
                        final String id = user.id;
                        final String username = user.username;
                        if (id != null && username != null) {
                            result.add(new ItemUserEntity(id, username));
                        }
                    }
                    return result;
                }
        ));
    }

    @Override
    public void getUser(@NonNull String id, @NonNull Consumer<Status<FullUserEntity>> callback) {
        userApi.getById(id).enqueue(new CallToConsumer<>(
                callback,
                user -> {
                    final String resultId = user.id;
                    final String name = user.username;
                    if (resultId != null && name != null) {
                        return new FullUserEntity(
                                resultId,
                                user.username,
                                user.photoUrl,
                                user.email
                        );
                    } else {
                        return null;
                    }
                }
        ));

    }

    @Override
    public void isExistUser(@NonNull String login, Consumer<Status<Void>> callback) {
        userApi.isExist(login).enqueue(new CallToConsumer<>(
                callback,
                dto -> null
        ));
    }

    @Override
    public void createAccount(@NonNull String login, @NonNull String password, Consumer<Status<Void>> callback) {
        userApi.register(new AccountDto(login, password)).enqueue(new CallToConsumer<>(
                callback,
                dto -> null
        ));
    }

    @Override
    public void login(@NonNull String login, @NonNull String password, Consumer<Status<Void>> callback) {
        credentialsDataSource.updateLogin(login, password);
        userApi.login().enqueue(new CallToConsumer<>(
                callback,
                dto -> null
        ));
    }

    @Override
    public void logout() {
        credentialsDataSource.logout();
    }
}