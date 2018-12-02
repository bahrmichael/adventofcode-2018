package com.bahr.adventofcode;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day1ATest {

    @ParameterizedTest
    @CsvSource({ "+1, +1, +1, 3", "+1, +1, -2, 0", "-1, -2, -3, -6"})
    void prep(int a, int b, int c, int expected) {
        int result = a + b + c;
        assertEquals(expected, result);
    }

    @Test
    void run() throws IOException {
        final String s = Files.readString(Paths.get("./src/test/resources/day1A.txt"));
        final String[] lines = s.split("\n");
        int result = 0;
        for (String line : lines) {
            result += Integer.parseInt(line);
        }
        System.out.println(result);
    }
}
