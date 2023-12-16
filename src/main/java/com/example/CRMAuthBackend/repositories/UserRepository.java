package com.example.CRMAuthBackend.repositories;

import com.example.CRMAuthBackend.dto.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByLoginAndPassword(String numberPhone, String password);
    User findByLogin(String numberPhone);
}
