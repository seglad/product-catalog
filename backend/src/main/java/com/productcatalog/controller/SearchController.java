package com.productcatalog.controller;

import com.productcatalog.dto.ProductResponse;
import com.productcatalog.service.FuzzySearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
@Validated
@Tag(name = "Search")
public class SearchController {

    private final FuzzySearchService fuzzySearchService;

    public SearchController(FuzzySearchService fuzzySearchService) {
        this.fuzzySearchService = fuzzySearchService;
    }

    @GetMapping
    @Operation(summary = "Search products using fuzzy matching")
    public List<ProductResponse> search(
        @Parameter(description = "Search query")
        @RequestParam @NotBlank String q,
        @Parameter(description = "Maximum edit distance (0-2), default 2")
        @RequestParam(defaultValue = "2") @Min(0) @Max(2) int distance
    ) {
        return fuzzySearchService.search(q, distance);
    }
}
