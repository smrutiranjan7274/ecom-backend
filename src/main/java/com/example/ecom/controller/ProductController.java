package com.example.ecom.controller;

import com.example.ecom.model.Product;
import com.example.ecom.repository.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;

/**
 * REST Controller for managing Product operations.
 * Handles CRUD operations and search functionality for products.
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductRepository productRepository;

    /**
     * Constructor for ProductController
     * 
     * @param productRepository repository for Product entity
     */
    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Retrieve all products from the database
     * 
     * @return List of all products
     */
    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Retrieve a specific product by its ID
     * 
     * @param id The ID of the product to retrieve
     * @return ResponseEntity containing the product if found, or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable String id) {
        Optional<Product> product = productRepository.findById(id);
        return product.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Retrieve all products belonging to a specific category
     * 
     * @param category The category to filter products by
     * @return List of products in the specified category
     */
    @GetMapping("/category/{category}")
    public List<Product> getProductsByCategory(@PathVariable String category) {
        return productRepository.findByCategory(category);
    }

    /**
     * Search for products by name (case-insensitive)
     * 
     * @param query The search query string
     * @return List of products matching the search query
     */
    @GetMapping("/search")
    public List<Product> searchProducts(@RequestParam String query) {
        return productRepository.findByNameContainingIgnoreCase(query);
    }

    /**
     * Create a new product
     * 
     * @param product The product object to create
     * @return The created product with generated ID
     */
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productRepository.save(product);
    }

    /**
     * Update an existing product
     * 
     * @param id             The ID of the product to update
     * @param productDetails The updated product details
     * @return ResponseEntity containing the updated product if found, or 404 if not
     *         found
     */
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable String id, @RequestBody Product productDetails) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    existingProduct.setName(productDetails.getName());
                    existingProduct.setDescription(productDetails.getDescription());
                    existingProduct.setPrice(productDetails.getPrice());
                    existingProduct.setImageUrl(productDetails.getImageUrl());
                    existingProduct.setStockQuantity(productDetails.getStockQuantity());
                    existingProduct.setCategory(productDetails.getCategory());
                    Product updatedProduct = productRepository.save(existingProduct);
                    return ResponseEntity.ok(updatedProduct);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Delete a product by its ID
     * 
     * @param id The ID of the product to delete
     * @return ResponseEntity with a message if deleted, or 404 if not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable String id) {
        Map<String, String> response = new HashMap<>();
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            response.put("message", "Product deleted successfully");
            return ResponseEntity.ok(response);
        }
        response.put("message", "Product not found");
        return ResponseEntity.status(404).body(response);
    }
}