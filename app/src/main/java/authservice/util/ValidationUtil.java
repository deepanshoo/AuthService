package authservice.util;

import authservice.model.UserInfoDto;

public class ValidationUtil {

    public static boolean validateUser(UserInfoDto userInfoDto) {

        String email = userInfoDto.getEmail(); // or getEmail() if you have it
        String password = userInfoDto.getPassword();

        // Username must not be null or empty
        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        // Password must be at least 6 chars
        if (password == null || password.length() < 6) {
            return false;
        }

        return true;
    }
}
