package com.pdp.ecommerce.repository;

import com.pdp.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query(value = """
        select u.*
        from users u
                 join users_roles ur
                      on u.id = ur.user_id
                 join roles r on ur.roles_id = r.id
        where r.name = :roleUser
""", nativeQuery = true)
    List<User> findByRoles(String roleUser);
}