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
            1L, "Mouse", "Electronics", "Ergonomic mouse", new BigDecimal("29.99"), "https://example.com/mouse.jpg");

        when(productService.createProduct(any())).thenReturn(created);

        String body = """
            {
              "name": "Mouse",
              "category": "Electronics",
              "description": "Ergonomic mouse",
              "price": 29.99,
              "imageUrl": "https://example.com/mouse.jpg"
            }
            """;

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("Mouse"));
    }

    @Test
    void validatesCreateProductRequest() throws Exception {
        String body = """
            {
              "name": "",
              "category": "Electronics",
              "description": "desc",
              "price": 10.00,
              "imageUrl": "https://example.com/mouse.jpg"
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
            1L, "Keyboard", "Electronics", "Mechanical", new BigDecimal("80.00"), "https://example.com/keyboard.jpg");
        when(productService.getProducts(eq(PageRequest.of(0, 1))))
            .thenReturn(new PageImpl<>(List.of(product), PageRequest.of(0, 1), 1));

        mockMvc.perform(get("/products?page=0&size=1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].name").value("Keyboard"))
            .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void returnsNotFoundForMissingId() throws Exception {
        when(productService.getProductById(99L)).thenThrow(new NoSuchElementException("Product not found"));
        mockMvc.perform(get("/products/99"))
            .andExpect(status().isNotFound());
    }
}
