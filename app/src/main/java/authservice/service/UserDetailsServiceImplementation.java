package authservice.service;

import authservice.eventProducer.UserInfoEvent;
import authservice.eventProducer.UserInfoProducer;
import lombok.AllArgsConstructor;
import lombok.Data;
import authservice.entities.UserInfo;
import authservice.model.UserInfoDto;
import authservice.repository.UserRepository;
import authservice.util.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
@Data
public class UserDetailsServiceImplementation implements UserDetailsService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final UserInfoProducer userInfoProducer;

    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImplementation.class);

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
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);
        userInfo.setUsername(userInfoDto.getUsername());
        userInfo.setPassword(userInfoDto.getPassword());
//        userInfo.setRoles(new HashSet<>());
        userRepository.save(userInfo);
        System.out.println("=== USER SAVED TO AUTH DB ===");
        
        UserInfoEvent event = userInfoEventToPublish(userInfoDto, userId);
        System.out.println("=== SENDING KAFKA EVENT ===");
        System.out.println("Event to send: " + event);
        
        try {
            userInfoProducer.sendEventToKafka(event);
            System.out.println("=== KAFKA EVENT SENT SUCCESSFULLY ===");
        } catch (Exception e) {
            System.err.println("=== FAILED TO SEND KAFKA EVENT ===");
            e.printStackTrace();
        }
        
        return true;
    }
    public String getUserByUsername(String userName){
        return Optional.of(userRepository.findByUsername(userName)).map(UserInfo::getUserId).orElse(null);
    }

    public Boolean validateUserCredentials(UserInfoDto userInfoDto) {
        UserInfo user = userRepository.findByUsername(userInfoDto.getUsername());
        if (user == null) {
            return false;
        }
        return passwordEncoder.matches(userInfoDto.getPassword(), user.getPassword());
    }

    public UserInfoEvent userInfoEventToPublish(UserInfoDto userInfoDto, String userId){
        return UserInfoEvent.builder()
                .userId(userId)
                .firstName(userInfoDto.getFirstName())
                .lastName(userInfoDto.getLastName())
                .email(userInfoDto.getEmail())
                .phoneNumber(userInfoDto.getPhoneNumber())
                .build();
    }
}
