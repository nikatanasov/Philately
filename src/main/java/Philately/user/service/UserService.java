package Philately.user.service;

import Philately.user.model.User;
import Philately.user.repository.UserRepository;
import Philately.web.dto.LoginRequest;
import Philately.web.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(RegisterRequest registerRequest) {
        Optional<User> optionalUser = userRepository.findByUsernameOrEmail(registerRequest.getUsername(), registerRequest.getEmail());
        if(optionalUser.isPresent()){
            throw new RuntimeException("User with this email or username already exist!");
        }
        userRepository.save(initializeUser(registerRequest));
    }



    private User initializeUser(RegisterRequest registerRequest){
        return User.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .email(registerRequest.getEmail())
                .build();
    }

    public User loginUser(LoginRequest loginRequest) {
        Optional<User> optionalUser = userRepository.findByUsername(loginRequest.getUsername());
        if(optionalUser.isEmpty()){
            throw new RuntimeException("User with this username does not exist!");
        }

        User user = optionalUser.get();
        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
            throw new RuntimeException("User with this password does not exist!");
        }
        return user;
    }

    public User getUserById(UUID userId) {
        return userRepository.findById(userId).orElseThrow(()->new RuntimeException("User with id ["+userId+"] does not exist."));
    }
}
