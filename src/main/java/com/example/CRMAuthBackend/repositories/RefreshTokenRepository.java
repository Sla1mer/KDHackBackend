package com.example.CRMAuthBackend.repositories;

import com.example.CRMAuthBackend.dto.entities.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {

    RefreshToken findByUserId(long userId);
    RefreshToken findByToken(String token);

}
