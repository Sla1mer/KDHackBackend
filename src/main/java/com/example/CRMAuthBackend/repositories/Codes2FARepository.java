package com.example.CRMAuthBackend.repositories;

import com.example.CRMAuthBackend.dto.entities.Codes2FADto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Codes2FARepository extends JpaRepository<Codes2FADto, Integer> {
    Codes2FADto findByPhoneNumberAndCode(String mobilePhone, String code);
}
