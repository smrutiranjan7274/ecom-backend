package com.example.ecom.repository;

import com.example.ecom.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repository interface for Product entity.
 * Extends JpaRepository to inherit basic CRUD operations and pagination
 * support.
 */
@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

    /**
     * Find all products belonging to a specific category
     * 
     * @param category the category to search for
     * @return list of products in the category
     */
    List<Product> findByCategory(String category);

    /**
     * Find products within a price range
     * 
     * @param minPrice minimum price (inclusive)
     * @param maxPrice maximum price (inclusive)
     * @return list of products within the price range
     */
    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    /**
     * Search products by name (case-insensitive partial match)
     * 
     * @param name the name to search for
     * @return list of products matching the name pattern
     */
    List<Product> findByNameContainingIgnoreCase(String name);

    /**
     * Find products with stock quantity below the specified threshold
     * 
     * @param threshold minimum stock quantity
     * @return list of products with low stock
     */
    List<Product> findByStockQuantityLessThan(Integer threshold);

    /**
     * Find products by category sorted by price in ascending order
     * 
     * @param category the category to search for
     * @return sorted list of products
     */
    List<Product> findByCategoryOrderByPriceAsc(String category);

    /**
     * Check if a product exists by its name (case insensitive)
     * 
     * @param name product name to check
     * @return true if product exists, false otherwise
     */
    boolean existsByNameIgnoreCase(String name);
}