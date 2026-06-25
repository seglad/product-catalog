package com.productcatalog.search;

public class DamerauLevenshtein {

    public int distance(String left, String right) {
        if (left == null || right == null) {
            throw new IllegalArgumentException("Inputs must not be null");
        }

        int m = left.length();
        int n = right.length();

        if (m == 0) {
            return n;
        }
        if (n == 0) {
            return m;
        }

        int[][] dp = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                int cost = left.charAt(i - 1) == right.charAt(j - 1) ? 0 : 1;
                int insertion = dp[i][j - 1] + 1;
                int deletion = dp[i - 1][j] + 1;
                int substitution = dp[i - 1][j - 1] + cost;
                dp[i][j] = Math.min(Math.min(insertion, deletion), substitution);

                if (i > 1 && j > 1
                    && left.charAt(i - 1) == right.charAt(j - 2)
                    && left.charAt(i - 2) == right.charAt(j - 1)) {
                    dp[i][j] = Math.min(dp[i][j], dp[i - 2][j - 2] + 1);
                }
            }
        }

        return dp[m][n];
    }
}
