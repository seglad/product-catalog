package com.productcatalog.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import org.springframework.stereotype.Component;

/**
 * Matches a search query against product text using Solr-style fuzzy term matching.
 * <p>
 * Behaviour mirrors Solr's {@code term~distance} syntax:
 * <ul>
 *   <li>Query and product text are split into lowercase tokens (words)</li>
 *   <li>Each query token must fuzzy-match at least one product token within {@code maxDistance}</li>
 *   <li>All query tokens must match (AND) — same as {@code q=buffet cabinet} in Solr</li>
 *   <li>Lower total edit distance = better match (used for ranking)</li>
 * </ul>
 * {@code maxDistance} is 0–2; default 2 matches Solr's {@code term~} without an explicit distance.
 */
@Component
public class FuzzyMatcher {

    private final DamerauLevenshtein damerauLevenshtein = new DamerauLevenshtein();

    /**
     * Returns a relevance score (sum of best distances per query term), or {@code null} if no match.
     */
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
            // Each query word must find a close enough token somewhere in the product text
            Integer bestDistance = bestDistance(queryTerm, targetTerms);
            if (bestDistance == null || bestDistance > maxDistance) {
                return null; // this product is excluded
            }
            totalScore += bestDistance;
        }

        return totalScore;
    }

    /** Shortest edit distance from {@code queryTerm} to any token in the product text. */
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

    /** Lowercase and split on non-alphanumeric boundaries, Solr-style tokenization. */
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
