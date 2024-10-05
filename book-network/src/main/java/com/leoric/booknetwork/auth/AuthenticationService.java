package com.leoric.booknetwork.auth;

import com.leoric.booknetwork.email.EmailService;
import com.leoric.booknetwork.email.EmailTemplateName;
import com.leoric.booknetwork.repositories.RoleRepository;
import com.leoric.booknetwork.repositories.TokenRepository;
import com.leoric.booknetwork.repositories.UserRepository;
import com.leoric.booknetwork.role.Role;
import com.leoric.booknetwork.security.JwtService;
import com.leoric.booknetwork.user.Token;
import com.leoric.booknetwork.user.User;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Value("${mailing.frontend.activation-url}")
    String activationUrl;


    public void register(RegistrationRequest request) throws MessagingException {
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new IllegalArgumentException("ROLE USER was not initialized"));
        User user = User.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(userRole))
                .build();
        userRepository.save(user);
        sendValidationEmail(user);
    }

    private void sendValidationEmail(User user) throws MessagingException {
        String newToken = generateAndSaveActivationToken(user);

        emailService.sendEmail(
                user.getEmail(),
                user.getFullname(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Account activation"
        );
    }

    private String  generateAndSaveActivationToken(User user) {
        int length = 6;
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
        final String chars = "0123456789";
        StringBuilder activationCode = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < length; i++) {
            activationCode.append(chars.charAt(random.nextInt(chars.length())));
        }
        return activationCode.toString();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        Map<String, Object> claims = new HashMap<>();
        User user = (User) auth.getPrincipal();
        claims.put("fullName", user.getFullname());
        var jwtToken = jwtService.generateToken(claims, user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    @Transactional
    public void activateAccount(String token) throws MessagingException {
        Token savedToken = tokenRepository.findByToken(token)
                //todo define exception
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));
        if (savedToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            sendValidationEmail(savedToken.getUser());
            throw new RuntimeException("Activation token has expired, new token has been sent.");
        }
        User user = userRepository.findById(savedToken.getUser().getId()).orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setEnabled(true);
        userRepository.save(user);
        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);
    }
}
