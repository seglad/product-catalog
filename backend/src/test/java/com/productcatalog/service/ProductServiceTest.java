package com.productcatalog.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.productcatalog.dto.CreateProductRequest;
import com.productcatalog.dto.ProductResponse;
import com.productcatalog.entity.Product;
import com.productcatalog.repository.ProductRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void createsProduct() {
        CreateProductRequest request = new CreateProductRequest();
        request.setName("Modern Sectional Sofa");
        request.setCategory("Living Room");
        request.setDescription("L-shaped fabric sofa with chaise lounge in charcoal grey");
        request.setPrice(new BigDecimal("899.00"));
        request.setImageUrl("https://example.com/images/sectional-sofa.jpg");

        Product saved = new Product();
        saved.setId(10L);
        saved.setName(request.getName());
        saved.setCategory(request.getCategory());
        saved.setDescription(request.getDescription());
        saved.setPrice(request.getPrice());
        saved.setImageUrl(request.getImageUrl());

        when(productRepository.save(any(Product.class))).thenReturn(saved);

        ProductResponse response = productService.createProduct(request);
        assertEquals(10L, response.getId());
        assertEquals("Modern Sectional Sofa", response.getName());
    }

    @Test
    void returnsPagedProducts() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Oak Coffee Table");
        product.setCategory("Living Room");
        product.setDescription("Solid oak coffee table with lower shelf storage");
        product.setPrice(new BigDecimal("249.99"));
        product.setImageUrl("https://example.com/images/coffee-table.jpg");

        when(productRepository.findAll(PageRequest.of(0, 1)))
            .thenReturn(new PageImpl<>(List.of(product), PageRequest.of(0, 1), 1));

        var page = productService.getProducts(PageRequest.of(0, 1));
        assertEquals(1, page.getTotalElements());
        assertEquals("Oak Coffee Table", page.getContent().getFirst().getName());
    }

    @Test
    void throwsWhenProductNotFound() {
        when(productRepository.findById(100L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> productService.getProductById(100L));
    }
}
