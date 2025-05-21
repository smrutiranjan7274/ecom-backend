package com.example.ecom.controller;

import com.example.ecom.model.Product;
import com.example.ecom.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductControllerTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductController productController;

    private Product sampleProduct;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleProduct = Product.builder()
                .id("1") // Changed from 1L to "1"
                .name("Test Product")
                .description("Description")
                .price(BigDecimal.valueOf(100))
                .imageUrl("http://img")
                .stockQuantity(10)
                .category("TestCat")
                .build();
    }

    @Test
    void getAllProducts_returnsList() {
        when(productRepository.findAll()).thenReturn(List.of(sampleProduct));
        List<Product> result = productController.getAllProducts();
        assertThat(result).hasSize(1).contains(sampleProduct);
    }

    @Test
    void getProductById_found() {
        when(productRepository.findById("1")).thenReturn(Optional.of(sampleProduct)); // Changed 1L to "1"
        ResponseEntity<Product> response = productController.getProductById("1"); // Changed 1L to "1"
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(sampleProduct);
    }

    @Test
    void getProductById_notFound() {
        when(productRepository.findById("2")).thenReturn(Optional.empty()); // Changed 2L to "2"
        ResponseEntity<Product> response = productController.getProductById("2"); // Changed 2L to "2"
        assertThat(response.getStatusCode().value()).isEqualTo(404);
    }

    @Test
    void createProduct_savesProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(sampleProduct);
        Product created = productController.createProduct(sampleProduct);
        assertThat(created).isEqualTo(sampleProduct);
    }

    @SuppressWarnings("null")
    @Test
    void updateProduct_found() {
        Product updatedDetails = Product.builder()
                .name("Updated")
                .description("desc")
                .price(BigDecimal.valueOf(200))
                .imageUrl("img")
                .stockQuantity(5)
                .category("Cat")
                .build();
        when(productRepository.findById("1")).thenReturn(Optional.of(sampleProduct)); // Changed 1L to "1"
        when(productRepository.save(any(Product.class))).thenReturn(updatedDetails);
        ResponseEntity<Product> response = productController.updateProduct("1", updatedDetails); // Changed 1L to "1"
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Updated");
    }

    @Test
    void updateProduct_notFound() {
        when(productRepository.findById("2")).thenReturn(Optional.empty()); // Changed 2L to "2"
        ResponseEntity<Product> response = productController.updateProduct("2", sampleProduct); // Changed 2L to "2"
        assertThat(response.getStatusCode().value()).isEqualTo(404);
    }

    @Test
    void deleteProduct_found() {
        when(productRepository.existsById("1")).thenReturn(true); // Changed 1L to "1"
        doNothing().when(productRepository).deleteById("1"); // Changed 1L to "1"
        ResponseEntity<Map<String, String>> response = productController.deleteProduct("1"); // Changed 1L to "1"
        assertThat(response.getStatusCode().value()).isEqualTo(204);
    }

    @Test
    void deleteProduct_notFound() {
        when(productRepository.existsById("2")).thenReturn(false); // Changed 2L to "2"
        ResponseEntity<Map<String, String>> response = productController.deleteProduct("2"); // Changed 2L to "2"
        assertThat(response.getStatusCode().value()).isEqualTo(404);
    }
}
