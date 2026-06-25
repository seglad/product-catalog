package com.productcatalog.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.productcatalog.dto.ProductResponse;
import com.productcatalog.service.FuzzySearchService;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(SearchController.class)
class SearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FuzzySearchService fuzzySearchService;

    @Test
    void searchesByQuery() throws Exception {
        ProductResponse match = new ProductResponse(
            1L, "Wireles Headphones", "Electronics", "Noise cancelling", new BigDecimal("129.99"),
            "https://example.com/images/headphones.jpg");
        when(fuzzySearchService.search("headphnes", 2)).thenReturn(List.of(match));

        mockMvc.perform(get("/search?q=headphnes"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("Wireles Headphones"));
    }

    @Test
    void validatesDistanceRange() throws Exception {
        mockMvc.perform(get("/search?q=headphnes&distance=3"))
            .andExpect(status().isBadRequest());
    }
}
