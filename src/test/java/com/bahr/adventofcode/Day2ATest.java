package com.bahr.adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class Day2ATest {

    @Test
    void prep() throws IOException {
        final String s = Files.readString(Paths.get("./src/test/resources/day2Aprep.txt"));
        final String[] lines = s.split("\n");

        int result = getChecksum(lines);
        assertEquals(12, result);
    }

    private int getChecksum(String[] lines) {
        int twoLetters = 0;
        int threeLetters = 0;
        for (String line : lines) {
            boolean twoSet = false;
            boolean threeSet = false;
            Map<Character, Integer> map = new HashMap<>();
            for (char c : line.toCharArray()) {
                map.computeIfPresent(c, (character, integer) -> integer + 1);
                map.putIfAbsent(c, 1);
            }
            for (Map.Entry<Character, Integer> entry : map.entrySet()) {
                if (!twoSet && entry.getValue() == 2) {
                    twoLetters++;
                    twoSet = true;
                } else if (!threeSet && entry.getValue() == 3) {
                    threeLetters++;
                    threeSet = true;
                }
            }

        }
        return twoLetters * threeLetters;
    }


    @Test
    void run() throws IOException {
        final String s = Files.readString(Paths.get("./src/test/resources/day2A.txt"));
        final String[] lines = s.split("\n");

        final int result = getChecksum(lines);
        System.out.println(result);
    }
}
