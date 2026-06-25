package com.productcatalog.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.productcatalog.dto.ProductResponse;
import com.productcatalog.entity.Product;
import com.productcatalog.repository.ProductRepository;
import com.productcatalog.search.FuzzyMatcher;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FuzzySearchServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Test
    void returnsRankedMatches() {
        FuzzyMatcher fuzzyMatcher = new FuzzyMatcher();
        ProductService productService = new ProductService(productRepository);
        FuzzySearchService fuzzySearchService = new FuzzySearchService(productRepository, fuzzyMatcher, productService);

        Product nearMatch = product(1L, "Buffet Cabnet", "Storage", "Sideboard with sliding doors and adjustable shelves");
        Product exact = product(2L, "Buffet Cabinet", "Storage", "Sideboard with sliding doors and adjustable shelves");
        Product unrelated = product(3L, "Queen Platform Bed", "Bedroom", "Low-profile platform bed frame with headboard");

        when(productRepository.findAll()).thenReturn(List.of(nearMatch, unrelated, exact));

        List<ProductResponse> result = fuzzySearchService.search("buffet cabinet", 2);
        assertEquals(2, result.size());
        assertEquals("Buffet Cabinet", result.getFirst().getName());
        assertEquals("Buffet Cabnet", result.get(1).getName());
    }

    private Product product(Long id, String name, String category, String description) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setCategory(category);
        product.setDescription(description);
        product.setPrice(new BigDecimal("459.00"));
        product.setImageUrl("https://example.com/images/buffet-cabinet.jpg");
        return product;
    }
}
