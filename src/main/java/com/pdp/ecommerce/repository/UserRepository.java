package com.pdp.ecommerce.repository;

import com.pdp.ecommerce.entity.User;
import com.pdp.ecommerce.model.projection.ProductProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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


    @Query(value = """
        SELECT p.id as id, p.name as name, p.description as description, p.image as image
        FROM users_favourite_products ufp
        JOIN product p ON ufp.favourite_products_id = p.id
        WHERE ufp.user_id = :userId
    """, nativeQuery = true)
    List<ProductProjection> getUserFavouriteProducts(@Param("userId") UUID userId);
}