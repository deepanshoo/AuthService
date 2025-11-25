package org.example.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.Entities.UserInfo;
import org.example.model.UserInfoDto;
import org.example.repository.UserRepository;
import org.example.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

@Component
@AllArgsConstructor
@Data
public class UserDetailsServiceImplementation implements UserDetailsService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        UserInfo user = userRepository.findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("Could not find any user...");
        }
        return new CustomUserDetails(user);
    }

    public UserInfo checkIfUserAlreadyExists(UserInfoDto userInfoDto){
        return userRepository.findByUsername(userInfoDto.getUsername());
    }

    public Boolean signupUser(UserInfoDto userInfoDto){
        //Define a function to check if userEmail, password is correct
        if (!ValidationUtil.validateUser(userInfoDto)) {
            return false;
        }
        userInfoDto.setPassword(passwordEncoder.encode(userInfoDto.getPassword()));
        if(Objects.nonNull(checkIfUserAlreadyExists(userInfoDto))){
            return false;
        }
        String userId= UUID.randomUUID().toString();
        userRepository.save(new UserInfo(userId, userInfoDto.getUsername() , userInfoDto.getPassword(), new HashSet<>()));
        return true;
    }

    public Boolean validateUserCredentials(UserInfoDto userInfoDto) {
        UserInfo user = userRepository.findByUsername(userInfoDto.getUsername());
        if (user == null) {
            return false;
        }
        return passwordEncoder.matches(userInfoDto.getPassword(), user.getPassword());
    }

}
