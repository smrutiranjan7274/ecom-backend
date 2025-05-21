package com.example.ecom.repository;

import com.example.ecom.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    // @Test
    // void saveAndFindByCategory() {
    // // Arrange: Create a product and save it to the database.
    // Product p = Product.builder()
    // .name("Test")
    // .price(BigDecimal.TEN)
    // .stockQuantity(5)
    // .category("Cat1")
    // .build();
    // productRepository.save(p);

    // // Act: Find products by category.
    // List<Product> found = productRepository.findByCategory("Cat1");

    // // Assert: Verify that the product was found and has the correct name.
    // assertThat(found).isNotEmpty();
    // assertThat(found.get(0).getName()).isEqualTo("Test");
    // }
}
