package com.productcatalog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public class CreateProductRequest {

    @Schema(example = "Modern Sectional Sofa")
    @NotBlank
    private String name;

    @Schema(example = "Living Room")
    @NotBlank
    private String category;

    @Schema(example = "L-shaped fabric sofa with chaise lounge in charcoal grey")
    @NotBlank
    private String description;

    @Schema(example = "899.00")
    @NotNull
    @Positive
    private BigDecimal price;

    @Schema(example = "https://example.com/images/sectional-sofa.jpg")
    @NotBlank
    private String imageUrl;

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
