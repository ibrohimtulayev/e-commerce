package com.pdp.ecommerce.repository;

import com.pdp.ecommerce.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
}