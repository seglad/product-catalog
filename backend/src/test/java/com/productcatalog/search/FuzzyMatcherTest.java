package com.productcatalog.search;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class FuzzyMatcherTest {

    private final FuzzyMatcher matcher = new FuzzyMatcher();

    @Test
    void matchesWithinDefaultDistanceTwo() {
        Integer score = matcher.score("headphnes", "wireless headphones electronics", 2);
        assertNotNull(score);
        assertEquals(1, score);
    }

    @Test
    void doesNotMatchWhenDistanceTooStrict() {
        Integer score = matcher.score("headphnes", "wireless headphones electronics", 0);
        assertNull(score);
    }

    @Test
    void requiresAllTermsToMatch() {
        Integer score = matcher.score("wireles headphnes", "wireless headphones electronics", 2);
        assertNotNull(score);
    }
}
