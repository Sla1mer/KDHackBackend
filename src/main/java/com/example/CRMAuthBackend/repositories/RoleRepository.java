package com.example.CRMAuthBackend.repositories;

import com.example.CRMAuthBackend.dto.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}
