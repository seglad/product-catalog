package com.productcatalog.service;

import com.productcatalog.dto.ProductResponse;
import com.productcatalog.entity.Product;
import com.productcatalog.repository.ProductRepository;
import com.productcatalog.search.FuzzyMatcher;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class FuzzySearchService {

    private final ProductRepository productRepository;
    private final FuzzyMatcher fuzzyMatcher;
    private final ProductService productService;

    public FuzzySearchService(
        ProductRepository productRepository,
        FuzzyMatcher fuzzyMatcher,
        ProductService productService
    ) {
        this.productRepository = productRepository;
        this.fuzzyMatcher = fuzzyMatcher;
        this.productService = productService;
    }

    public List<ProductResponse> search(String query, int distance) {
        return productRepository.findAll().stream()
            .map(product -> toScoredProduct(product, query, distance))
            .filter(scored -> scored.score() != null) // drop non-matches
            .sorted(Comparator.comparingInt(ScoredProduct::score)) // closest matches first
            .map(scored -> productService.toResponse(scored.product()))
            .toList();
    }

    private ScoredProduct toScoredProduct(Product product, String query, int distance) {
        // Search across name, category, and description as one token stream
        String searchableText = String.join(" ",
            safe(product.getName()),
            safe(product.getCategory()),
            safe(product.getDescription()));
        Integer score = fuzzyMatcher.score(query, searchableText, distance);
        return new ScoredProduct(product, score);
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    private record ScoredProduct(Product product, Integer score) {
    }
}
