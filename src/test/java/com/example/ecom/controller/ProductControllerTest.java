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
                .id(1L)
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
        // Arrange: Mock the productRepository to return a list containing the sampleProduct when findAll() is called.
        when(productRepository.findAll()).thenReturn(List.of(sampleProduct));

        // Act: Call the getAllProducts() method on the productController.
        List<Product> result = productController.getAllProducts();

        // Assert: Verify that the result is a list containing the sampleProduct and has a size of 1.
        assertThat(result).hasSize(1).contains(sampleProduct);
    }

    @Test
    void getProductById_found() {
        // Arrange: Mock the productRepository to return an Optional containing the sampleProduct when findById(1L) is called.
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));

        // Act: Call the getProductById(1L) method on the productController.
        ResponseEntity<Product> response = productController.getProductById(1L);

        // Assert: Verify that the response status code is 200 (OK) and the response body is equal to the sampleProduct.
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(sampleProduct);
    }

    @Test
    void getProductById_notFound() {
        // Arrange: Mock the productRepository to return an empty Optional when findById(2L) is called.
        when(productRepository.findById(2L)).thenReturn(Optional.empty());

        // Act: Call the getProductById(2L) method on the productController.
        ResponseEntity<Product> response = productController.getProductById(2L);

        // Assert: Verify that the response status code is 404 (Not Found).
        assertThat(response.getStatusCode().value()).isEqualTo(404);
    }

    @Test
    void createProduct_savesProduct() {
        // Arrange: Mock the productRepository to return the sampleProduct when save(any(Product.class)) is called.
        when(productRepository.save(any(Product.class))).thenReturn(sampleProduct);

        // Act: Call the createProduct(sampleProduct) method on the productController.
        Product created = productController.createProduct(sampleProduct);

        // Assert: Verify that the created product is equal to the sampleProduct.
        assertThat(created).isEqualTo(sampleProduct);
    }

    @SuppressWarnings("null")
    @Test
    void updateProduct_found() {
        // Arrange: Create an updated Product object and mock the productRepository's behavior.
        Product updatedDetails = Product.builder()
                .name("Updated")
                .description("desc")
                .price(BigDecimal.valueOf(200))
                .imageUrl("img")
                .stockQuantity(5)
                .category("Cat")
                .build();
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));
        when(productRepository.save(any(Product.class))).thenReturn(updatedDetails);

        // Act: Call the updateProduct(1L, updatedDetails) method on the productController.
        ResponseEntity<Product> response = productController.updateProduct(1L, updatedDetails);

        // Assert: Verify that the response status code is 200 (OK), the response body is not null, and the name of the updated product is "Updated".
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Updated");
    }

    @Test
    void updateProduct_notFound() {
        // Arrange: Mock the productRepository to return an empty Optional when findById(2L) is called.
        when(productRepository.findById(2L)).thenReturn(Optional.empty());

        // Act: Call the updateProduct(2L, sampleProduct) method on the productController.
        ResponseEntity<Product> response = productController.updateProduct(2L, sampleProduct);

        // Assert: Verify that the response status code is 404 (Not Found).
        assertThat(response.getStatusCode().value()).isEqualTo(404);
    }

    @Test
    void deleteProduct_found() {
        // Arrange: Mock the productRepository to return true when existsById(1L) is called and do nothing when deleteById(1L) is called.
        when(productRepository.existsById(1L)).thenReturn(true);
        doNothing().when(productRepository).deleteById(1L);

        // Act: Call the deleteProduct(1L) method on the productController.
        ResponseEntity<Void> response = productController.deleteProduct(1L);

        // Assert: Verify that the response status code is 204 (No Content).
        assertThat(response.getStatusCode().value()).isEqualTo(204);
    }

    @Test
    void deleteProduct_notFound() {
        // Arrange: Mock the productRepository to return false when existsById(2L) is called.
        when(productRepository.existsById(2L)).thenReturn(false);

        // Act: Call the deleteProduct(2L) method on the productController.
        ResponseEntity<Void> response = productController.deleteProduct(2L);

        // Assert: Verify that the response status code is 404 (Not Found).
        assertThat(response.getStatusCode().value()).isEqualTo(404);
    }
}
