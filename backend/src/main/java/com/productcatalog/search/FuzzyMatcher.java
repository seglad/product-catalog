package com.productcatalog.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class FuzzyMatcher {

    private final DamerauLevenshtein damerauLevenshtein = new DamerauLevenshtein();

    public Integer score(String query, String searchableText, int maxDistance) {
        if (query == null || searchableText == null || query.isBlank()) {
            return null;
        }
        if (maxDistance < 0 || maxDistance > 2) {
            throw new IllegalArgumentException("distance must be between 0 and 2");
        }

        List<String> queryTerms = tokenize(query);
        List<String> targetTerms = tokenize(searchableText);
        if (queryTerms.isEmpty() || targetTerms.isEmpty()) {
            return null;
        }

        int totalScore = 0;
        for (String queryTerm : queryTerms) {
            Integer bestDistance = bestDistance(queryTerm, targetTerms);
            if (bestDistance == null || bestDistance > maxDistance) {
                return null;
            }
            totalScore += bestDistance;
        }

        return totalScore;
    }

    private Integer bestDistance(String queryTerm, List<String> targetTerms) {
        Integer best = null;
        for (String targetTerm : targetTerms) {
            int distance = damerauLevenshtein.distance(queryTerm, targetTerm);
            if (best == null || distance < best) {
                best = distance;
            }
        }
        return best;
    }

    private List<String> tokenize(String text) {
        String normalized = text.toLowerCase(Locale.ROOT).trim();
        if (normalized.isEmpty()) {
            return List.of();
        }

        String[] rawTokens = normalized.split("[^a-z0-9]+");
        List<String> tokens = new ArrayList<>(Arrays.asList(rawTokens));
        tokens.removeIf(token -> Objects.equals(token, ""));
        return tokens;
    }
}
