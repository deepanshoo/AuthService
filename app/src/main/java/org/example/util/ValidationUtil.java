package org.example.util;

import org.example.model.UserInfoDto;

public class ValidationUtil {

    public static boolean validateUser(UserInfoDto userInfoDto) {

        String email = userInfoDto.getUsername(); // or getEmail() if you have it
        String password = userInfoDto.getPassword();

        // Username must not be null or empty
        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        // Password must be at least 8 chars
        if (password == null || password.length() < 8) {
            return false;
        }

        return true;
    }
}
