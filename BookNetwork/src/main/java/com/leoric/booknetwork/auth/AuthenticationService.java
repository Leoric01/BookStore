package com.leoric.booknetwork.auth;

import com.leoric.booknetwork.repositories.RoleRepository;
import com.leoric.booknetwork.repositories.TokenRepository;
import com.leoric.booknetwork.repositories.UserRepository;
import com.leoric.booknetwork.role.Role;
import com.leoric.booknetwork.user.Token;
import com.leoric.booknetwork.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    int length = 6;


    public void register(RegistrationRequest request) {
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new IllegalArgumentException("ROLE USER was not initialized"));
        User user = User.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getLastname())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(userRole))
                .build();
        userRepository.save(user);
        sendValidationEmail(user);
    }

    private void sendValidationEmail(User user) {
        String newToken = generateAndSaveActivationToken(user);
        //TODO: SEND EMAIL
    }

    private String  generateAndSaveActivationToken(User user) {
        String generatedToken = generateActivationCode(length);
        Token token = Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        tokenRepository.save(token);
        return generatedToken;
    }

    private String generateActivationCode(int length) {
        String chars = "0123456789";
        StringBuilder activationCode = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < length; i++) {
            activationCode.append(chars.charAt(random.nextInt(chars.length())));
        }
        return activationCode.toString();
    }
}
