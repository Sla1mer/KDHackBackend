package com.example.CRMAuthBackend.controllers;

import com.example.CRMAuthBackend.config.UserAuthenticationProvider;
import com.example.CRMAuthBackend.dto.auth.TokensDto;
import com.example.CRMAuthBackend.dto.entities.User;
import com.example.CRMAuthBackend.dto.exceptions.*;
import com.example.CRMAuthBackend.services.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;

@RestController
@Tag(name = "AuthenticationController", description = "Авторизация/регистрация пользователей")
@RequestMapping("/api")
public class AuthenticationController {

    @Autowired
    private UserAuthenticationProvider userAuthenticationProvider;

    @Autowired
    private AuthenticationService authenticationService;

    @Operation(summary = "Авторизация пользователя", description = "Нужно в body передать json с полями: email, password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Не правильный логин или пароль", content = {
                    @Content(schema = @Schema(implementation = ErrorDto.class))
            })
    })
    @PostMapping("/signIn")
    public ResponseEntity<TokensDto> signIn(@AuthenticationPrincipal User user) throws ExecutionException, InterruptedException, MessagingException, NoSuchAlgorithmException {
        return ResponseEntity.ok(userAuthenticationProvider.createToken(user.getLogin(), null));
    }

    @Operation(summary = "Регистрация пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation")
    })
    @PostMapping("/regUser")
    public ResponseEntity<TokensDto> regUser(@RequestBody User user) throws ExecutionException, InterruptedException, NoSuchAlgorithmException, MessagingException {
        return ResponseEntity.ok(userAuthenticationProvider.createToken(user.getLogin(), user));
    }

    @Operation(summary = "Обновление access и refresh токенов", description = "Нужно в Header (Authorization) передать refresh token в таком формате: Bearer your_token_here")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Не правильный токен или истекло время жизни ключа", content = {
                    @Content(schema = @Schema(implementation = ErrorDto.class))
            })
    })
    @PostMapping("/refreshTokens")
    public ResponseEntity<TokensDto> refreshTokens(HttpServletRequest request) throws ExecutionException, InterruptedException, MessagingException, IncorrectTokenException, NoSuchAlgorithmException {
        return ResponseEntity.ok(userAuthenticationProvider.createNewTokensByRefreshToken(request));
    }

}
