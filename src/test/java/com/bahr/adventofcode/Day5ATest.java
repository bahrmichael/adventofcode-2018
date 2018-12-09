package com.bahr.adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day5ATest {

    @Test
    void run() throws IOException {
        final String testResult1 = reduce2("aabAAB");
        assertEquals("aabAAB".length(), testResult1.length());

        final String testResult2 = reduce2("abBA");
        assertEquals(0, testResult2.length());

        final String input = "dabAcCaCBAcCcaDA";
        final String testResult = reduce2(input);
        assertEquals(10, testResult.length());

        final String s = Files.readString(Paths.get("./src/test/resources/day5.txt"));
        final String result = reduce2(s.trim().replace("\n", ""));
        System.out.println(result.length());
    }

    private String reduce2(final String text) {
        final String reduced = reduceOneStep(text);
        if (reduced.length() != text.length()) {
            System.out.println("Reduced to " + reduced.length());
            return reduce2(reduced);
        } else {
            return reduced;
        }

    }

    private String reduceOneStep(String text) {
        int i = 0;
        while (i < text.length() - 1) {
            final Character c1 = text.charAt(i);
            final Character c2 = text.charAt(i + 1);
            if (c1.toString().toLowerCase().equals(c2.toString().toLowerCase()) && !c1.equals(c2)) {
                final String begin = text.substring(0, i);
                final String end = text.substring(i + 2);
                text = begin + end;

                i = -1;
            }
            i++;
        }
        return text;
    }

}
