package com.pdp.ecommerce.repository;

import com.pdp.ecommerce.entity.Product;
import com.pdp.ecommerce.model.projection.CategoryProductProjection;
import com.pdp.ecommerce.model.projection.ProductProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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


    //CREATE EXTENSION pg_trgm ;  use this command
    @Query(value = """
            SELECT distinct p.* FROM product p
            JOIN product_product_details ppd on p.id = ppd.product_id 
            JOIN product_details pd ON ppd.product_details_id = pd.id           
            WHERE pd.gender = CAST(:gender AS VARCHAR)
            AND SIMILARITY(p.name, :keyword) > 0.2;
            """, nativeQuery = true)
    List<Product> findByNameAndGender(@Param("keyword") String keyword, @Param("gender") String gender);

    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId AND p.id != :productId ORDER BY FUNCTION('RANDOM') limit 4")
    List<Product> getProductsWithFavouriteType(@Param("categoryId") UUID categoryId, @Param("productId") UUID productId);

    Page<Product> getPagedProductsByCategoryName(String categoryName, Pageable pageable);

    @Query(value = """
            select p.*
            from product p
            where p.category_id=:categoryId
            Order by p.created_at desc
            """,nativeQuery = true)
    List<ProductProjection> filterByNewlyAdded(@Param("categoryId") UUID categoryId);
    @Query(value = """
            select p.*
            from product p
                     join rating r on p.id = r.product_id
            where p.category_id=:categoryId
            group by p.id
            order by max(r.grade) desc
                        """, nativeQuery = true)
    List<ProductProjection> filterByRating(@Param("categoryId") UUID categoryId);

    List<ProductProjection> findByCategoryId(@Param("categoryId") UUID categoryId);

    @Query(value = """
        SELECT json_build_object(
                        'id', p.id,
                        'name', p.name,
                        'category_name', c.name,
                        'discount', CASE WHEN d.id IS NOT NULL AND d.start_date <= NOW() AND d.end_date >= NOW() THEN d.amount END,
                        'average_rating', COALESCE(CAST(SUM(r.grade) AS FLOAT) / NULLIF(COUNT(r.id), 0), 0),
                        'rating_count', COUNT(r.id),
                        'product_details', (
                            SELECT json_agg(
                                json_build_object(
                                    'id', pd.id,
                                    'gender', pd.gender,
                                    'size', pd.size,
                                    'color', pd.color,
                                    'quantity', pd.quantity,
                                    'price', pd.price
                                )
                            )
                            FROM (
                                SELECT DISTINCT pd.id, pd.gender, pd.size, pd.color, pd.quantity, pd.price
                                FROM product_product_details ppd
                                JOIN product_details pd ON ppd.product_details_id = pd.id
                                WHERE ppd.product_id = p.id
                            ) pd
                        )
                    ) AS result
        FROM product p
        JOIN category c ON c.id = p.category_id
        LEFT JOIN discount_products dp ON p.id = dp.products_id
        LEFT JOIN discount d ON dp.discount_id = d.id AND d.start_date <= NOW() AND d.end_date >= NOW()
        LEFT JOIN discount_categories dc ON c.id = dc.categories_id AND dc.discount_id = d.id
        LEFT JOIN rating r ON p.id = r.product_id
        WHERE p.id = :productId
        GROUP BY p.id, p.name, c.name, d.id, d.start_date, d.end_date
        ORDER BY p.name
""", nativeQuery = true)
    String findDetailedProductById(UUID productId);

    @Query(value = """
            SELECT json_build_object(
                           'username', u.email,
                           'rating', r.grade,
                           'comments', COALESCE(json_agg(json_build_object(
                            'id', c.id,
                            'description', c.description
                                                         )) FILTER (WHERE c.id IS NOT NULL), '[]')
                   ) AS product_details
            FROM
                product p
                    JOIN
                rating r ON p.id = r.product_id
                    JOIN
                users u ON r.user_id = u.id
                    LEFT JOIN
                comment c ON p.id = c.product_id AND r.user_id = c.user_id
            WHERE
                p.id = :productId
            GROUP BY
                u.email, r.grade, r.id;
            """, nativeQuery = true)
    List<String> findRatingAndReviewsByProductId(UUID productId);

    @Query(value = "select description from product where id = :productId", nativeQuery = true)
    String findDescriptionById(UUID productId);
    @Query(value = """
            select p.id, p.name,p.image,c.name categoryName
            from product p
            join category c on p.category_id = c.id
            """,nativeQuery = true)
    List<CategoryProductProjection> findAllWithCategory();
}

