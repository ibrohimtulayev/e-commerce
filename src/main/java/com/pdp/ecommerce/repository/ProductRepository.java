package com.pdp.ecommerce.repository;

import com.pdp.ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    @Query(value = "SELECT * FROM product LIMIT :amount", nativeQuery = true)
    List<Product> getRandomProducts(@Param("amount") int amount);

    @Query(value = """
        SELECT *
        FROM product
        WHERE category_id = :id
        ORDER BY RANDOM()
        LIMIT :amount
    """, nativeQuery = true)
    List<Product> getRandomProductsByCategoryId(@Param("id") UUID id, Integer amount);

    @Query("SELECT p FROM User u JOIN u.favouriteProducts p WHERE u.id = :userId ORDER BY FUNCTION('RANDOM') LIMIT 1")
    Optional<Product> findOneFavouriteProductByUserId(@Param("userId") UUID userId);


    @Query(value = """
            SELECT p.*FROM product p
            JOIN product_details pd ON p.product_details_id = pd.id
            WHERE pd.gender = CAST(:gender AS VARCHAR)
            AND SIMILARITY(p.name, :keyword) > 0.2;
            """,nativeQuery = true)
    List<Product> findByNameAndGender(@Param("keyword") String keyword,@Param("gender") String gender);

    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId AND p.id != :productId ORDER BY FUNCTION('RANDOM') limit 4")
    List<Product> getProductsWithFavouriteType(@Param("categoryId") UUID categoryId, @Param("productId") UUID productId);

}