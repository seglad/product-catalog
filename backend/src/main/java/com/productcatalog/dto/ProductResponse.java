package com.productcatalog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

public class ProductResponse {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "Modern Sectional Sofa")
    private String name;

    @Schema(example = "Living Room")
    private String category;

    @Schema(example = "L-shaped fabric sofa with chaise lounge in charcoal grey")
    private String description;

    @Schema(example = "899.00")
    private BigDecimal price;

    @Schema(example = "https://example.com/images/sectional-sofa.jpg")
    private String imageUrl;

    public ProductResponse() {
    }

    public ProductResponse(Long id, String name, String category, String description, BigDecimal price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
