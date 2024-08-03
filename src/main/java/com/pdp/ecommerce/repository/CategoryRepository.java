package com.pdp.ecommerce.repository;

import com.pdp.ecommerce.entity.Category;
import com.pdp.ecommerce.model.projection.CategoryProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    @Query(nativeQuery = true, value = "select * from category where category.parent_category_id IS NULL order by random() limit :amount")
    List<Category> getRandomCategories(@Param("amount") int amount);

    @Query(value = """
            SELECT DISTINCT c.* FROM category c 
            JOIN product p on c.id = p.category_id 
            """, nativeQuery = true)
    List<Category> getVeryChildCategories();

    @Query(value = """
            SELECT c1.id AS category_id,
                   c1.name AS category_name,
                   CASE
                       WHEN COUNT(c2.id) = 0 THEN NULL
                       ELSE ARRAY_AGG(c2.name)
                       END AS subcategories,
                   COUNT(p.id) AS product_count
            FROM category c1
                     LEFT JOIN category c2 ON c1.id = c2.parent_category_id
                     LEFT JOIN product p ON c1.id = p.category_id
            GROUP BY c1.id, c1.name
            ORDER BY c1.id;
                      """, nativeQuery = true)
    List<CategoryProjection> findAllCategories();
}

