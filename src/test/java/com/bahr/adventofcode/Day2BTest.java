package com.bahr.adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day2BTest {
    @Test
    void prep() {
        final String input = "abcde\n" +
                "fghij\n" +
                "klmno\n" +
                "pqrst\n" +
                "fguij\n" +
                "axcye\n" +
                "wvxyz";

        final String result = getCommonLettersOfClosestMatch(input.split("\n"));

        assertEquals("fgij", result);
    }

    private String getCommonLettersOfClosestMatch(String[] input) {
        int bestMatch = 0;
        String result = "";
        for (String a : input) {
            for (String b : input) {
                if (a.equals(b)) {
                    continue;
                }
                int matchScore = 0;
                final char[] cA = a.toCharArray();
                final char[] cB = b.toCharArray();
                for (int i = 0; i < cA.length; i++) {
                    if (cA[i] == cB[i]) {
                        matchScore++;
                    }
                }

                if (matchScore > bestMatch) {
                    bestMatch = matchScore;

                    result = "";
                    for (int i = 0; i < cA.length; i++) {
                        if (cA[i] == cB[i]) {
                            result += cA[i];
                        }
                    }
                }
            }

        }

        return result;
    }

    @Test
    void run() throws IOException {
        final String s = Files.readString(Paths.get("./src/test/resources/day2B.txt"));
        final String[] lines = s.split("\n");

        final String result = getCommonLettersOfClosestMatch(lines);
        System.out.println(result);
    }
}
