package com.productcatalog.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.productcatalog.dto.ProductResponse;
import com.productcatalog.service.ProductService;
import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @Test
    void createsProduct() throws Exception {
        ProductResponse created = new ProductResponse(
            1L,
            "Modern Sectional Sofa",
            "Living Room",
            "L-shaped fabric sofa with chaise lounge in charcoal grey",
            new BigDecimal("899.00"),
            "https://example.com/images/sectional-sofa.jpg");

        when(productService.createProduct(any())).thenReturn(created);

        String body = """
            {
              "name": "Modern Sectional Sofa",
              "category": "Living Room",
              "description": "L-shaped fabric sofa with chaise lounge in charcoal grey",
              "price": 899.00,
              "imageUrl": "https://example.com/images/sectional-sofa.jpg"
            }
            """;

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("Modern Sectional Sofa"));
    }

    @Test
    void validatesCreateProductRequest() throws Exception {
        String body = """
            {
              "name": "",
              "category": "Living Room",
              "description": "L-shaped fabric sofa with chaise lounge in charcoal grey",
              "price": 899.00,
              "imageUrl": "https://example.com/images/sectional-sofa.jpg"
            }
            """;

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isBadRequest());
    }

    @Test
    void returnsPagedProducts() throws Exception {
        ProductResponse product = new ProductResponse(
            2L,
            "Oak Coffee Table",
            "Living Room",
            "Solid oak coffee table with lower shelf storage",
            new BigDecimal("249.99"),
            "https://example.com/images/coffee-table.jpg");
        PageRequest pageRequest = PageRequest.of(0, 1, Sort.by("name").ascending());
        when(productService.getProducts(eq(pageRequest)))
            .thenReturn(new PageImpl<>(List.of(product), pageRequest, 12));

        mockMvc.perform(get("/products?page=0&size=1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].name").value("Oak Coffee Table"))
            .andExpect(jsonPath("$.totalElements").value(12));
    }

    @Test
    void returnsNotFoundForMissingId() throws Exception {
        when(productService.getProductById(99L)).thenThrow(new NoSuchElementException("Product not found"));
        mockMvc.perform(get("/products/99"))
            .andExpect(status().isNotFound());
    }
}
