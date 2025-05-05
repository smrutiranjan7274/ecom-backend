package com.example.ecom.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Entity class representing a product in the e-commerce system.
 * Uses JPA annotations for database mapping and Lombok for boilerplate code
 * generation.
 */
@Entity // Marks this class as a JPA entity
@Table(name = "products") // Specifies the database table name
@Data // Lombok annotation to generate getters, setters, toString, equals, and
      // hashCode
@NoArgsConstructor // Generates a no-args constructor
@AllArgsConstructor // Generates a constructor with all arguments
@Builder // Implements the Builder pattern for object creation
public class Product {

    /** Unique identifier for the product */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Name of the product - required field */
    @Column(nullable = false)
    private String name;

    /** Detailed description of the product - optional */
    private String description;

    /** Price of the product in decimal format - required field */
    @Column(nullable = false)
    private BigDecimal price;

    /** URL pointing to the product's image - optional */
    private String imageUrl;

    /** Current quantity available in stock - required field */
    @Column(nullable = false)
    private Integer stockQuantity;

    /**
     * Product category - simplified as a String.
     * Consider refactoring to a separate Category entity with a proper relationship
     * mapping
     */
    private String category;

    /**
     * Note: Additional fields that could be added in future:
     * - ratings (average rating)
     * - reviews (One-to-Many relationship)
     * - createdAt (timestamp)
     * - updatedAt (timestamp)
     * - brand
     * - weight
     * - dimensions
     */
}