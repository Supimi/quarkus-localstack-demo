package com.dgsp.demo.service;

import com.dgsp.demo.dao.UserData;
import com.dgsp.demo.dto.User;
import com.dgsp.demo.dto.UserResponse;
import com.dgsp.demo.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

@Slf4j
@ApplicationScoped
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse saveUser(User user) {
        try {
            UserData userData = mapUserToUserData(user);
            userRepository.saveNonNull(userData);
            User savedUser = mapUserDataToUser(userData);
            return UserResponse.builder()
                    .success(true)
                    .body(List.of(savedUser))
                    .build();
        } catch (Exception e) {
            log.error("Error in saving user", e);
            return UserResponse.builder()
                    .success(false)
                    .build();
        }

    }

    public UserResponse getAllUsers() {
        try {
            List<User> users = userRepository.getAll().stream().map(this::mapUserDataToUser).toList();
            log.info("User list :: {}",users);
            return UserResponse.builder()
                    .success(true)
                    .body(users)
                    .build();
        } catch (Exception e) {
            log.error("Error in retrieving all users", e);
            return UserResponse.builder()
                    .success(false)
                    .build();
        }
    }

    public UserResponse getUser(String userId) {
        try {
            List<User> users = userRepository.getUser(userId).stream().map(this::mapUserDataToUser).toList();
            return UserResponse.builder()
                    .success(true)
                    .body(users)
                    .build();
        } catch (Exception e) {
            return UserResponse.builder()
                    .success(false)
                    .build();
        }
    }

    private User mapUserDataToUser(UserData userData) {
        return User.builder()
                .userId(userData.getUserId())
                .firstname(userData.getFirstname())
                .lastName(userData.getLastName())
                .age(userData.getAge())
                .build();
    }

    private UserData mapUserToUserData(User user) {
        return UserData.builder()
                .userId(String.valueOf(UUID.randomUUID()))
                .firstname(user.firstname())
                .lastName(user.lastName())
                .age(user.age())
                .build();
    }

}
