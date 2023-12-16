package com.example.CRMAuthBackend.dto.auth;

import com.example.CRMAuthBackend.dto.entities.User;
import lombok.*;


@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationDto {
    private User user;
    private TokenPayloadDto tokenPayloadDto;
}
