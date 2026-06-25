package com.productcatalog.search;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class DamerauLevenshteinTest {

    private final DamerauLevenshtein distance = new DamerauLevenshtein();

    @Test
    void returnsZeroForEqualStrings() {
        assertEquals(0, distance.distance("roam", "roam"));
    }

    @Test
    void returnsOneForSingleEdit() {
        assertEquals(1, distance.distance("roam", "foam"));
    }

    @Test
    void supportsTranspositionAsSingleEdit() {
        assertEquals(1, distance.distance("roam", "roma"));
    }
}
