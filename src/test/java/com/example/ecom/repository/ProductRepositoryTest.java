package com.example.ecom.repository;

import com.example.ecom.model.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataMongoTest
@TestPropertySource(properties = "spring.data.mongodb.uri=mongodb://localhost:27017/test")
class ProductRepositoryTest {

        @Autowired
        private ProductRepository productRepository;

        @BeforeEach
        void setUp() {
                productRepository.deleteAll();
        }

        @Test
        void testSaveProduct() {
                Product product = new Product();
                product.setName("Test Product");
                Product saved = productRepository.save(product);
                assertThat(saved.getId()).isNotNull();
        }

        @Test
        void testFindById() {
                Product product = new Product();
                product.setName("Test Product");
                Product saved = productRepository.save(product);
                Optional<Product> found = productRepository.findById(saved.getId());
                assertThat(found).isPresent();
                assertThat(found.get().getName()).isEqualTo("Test Product");
        }

}
