package com.example.CRMAuthBackend.services;

import com.example.CRMAuthBackend.dto.auth.CredentialsDto;
import com.example.CRMAuthBackend.dto.entities.RefreshToken;
import com.example.CRMAuthBackend.dto.entities.User;
import com.example.CRMAuthBackend.dto.exceptions.InvalidEmailOrPasswordException;
import com.example.CRMAuthBackend.dto.exceptions.NotActiveAccountException;
import com.example.CRMAuthBackend.repositories.RefreshTokenRepository;
import com.example.CRMAuthBackend.repositories.UserRepository;
import com.example.CRMAuthBackend.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.concurrent.CompletableFuture;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private RefreshTokenRepository refreshTokenRepository;


    @Value("${security.jwt.token.secret-key:secret-key}")
    private String secretKey;

    @Async
    public CompletableFuture<User> authenticate(CredentialsDto credentialsDto) throws NoSuchAlgorithmException, NotActiveAccountException, InvalidEmailOrPasswordException {
        String encodePassword = PasswordUtils.toHexString(PasswordUtils.getSHA(credentialsDto.getPassword()));

        User user = userRepository.findByLoginAndPassword(credentialsDto.getEmail(), encodePassword);

        if (user == null) throw new InvalidEmailOrPasswordException("Не правильный логин или пароль");

        return CompletableFuture.completedFuture(user);
    }

    @Async
    public CompletableFuture<User> registrationUser(User credentialsDto) throws NoSuchAlgorithmException {
        String encodePassword = PasswordUtils.toHexString(PasswordUtils.getSHA(credentialsDto.getPassword()));

        credentialsDto.setPassword(encodePassword);

        userRepository.save(credentialsDto);

        User newUser = userRepository.findByLoginAndPassword(credentialsDto.getLogin(), encodePassword);

        return CompletableFuture.completedFuture(newUser);
    }

    @Async
    public CompletableFuture<RefreshToken> findRefreshTokenByUserId(long userId) {
        return CompletableFuture.completedFuture(refreshTokenRepository.findByUserId(userId));
    }

    @Async
    public CompletableFuture<RefreshToken> findRefreshTokenByToken(String token) {
        return CompletableFuture.completedFuture(refreshTokenRepository.findByToken(token));
    }

    @Async
    public void saveRefreshToken(RefreshToken refreshToken) {
        refreshTokenRepository.save(refreshToken);
    }

    @Async
    public CompletableFuture<User> findByLogin(String email) {
        User user = userRepository.findByLogin(email);

        return CompletableFuture.completedFuture(user);
    }

}
