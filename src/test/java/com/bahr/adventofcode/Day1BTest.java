package com.bahr.adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class Day1BTest {

    @Test
    void prep() {
        prepCheck(1, -1, 0);
        prepCheck(3, 3, 4, -2, -4, 10);
        prepCheck(-6, 3, 8,5 ,-6, 5);
        prepCheck(7, 7, -2, -7, -4, 14);
    }

    void prepCheck(Integer ... n) {
        int expected = n[n.length - 1];
        Integer[] input = Arrays.copyOfRange(n, 0, n.length - 1);
        final int result = run(input);
        assertEquals(expected, result);
    }

    private int run(Integer[] input) {
        int result = 0;
        final Set<Integer> knownValues = new HashSet<>();
        for (int j = 0; j < 1000; j++) {
            for (final int integer : input) {
                knownValues.add(result);
                result += integer;
                System.out.println(result);
                if (knownValues.contains(result)) {
                    return result;
                }
            }
        }
        throw new RuntimeException("No sequence was found.");
    }

    @Test
    void run() throws IOException {
        final String s = Files.readString(Paths.get("./src/test/resources/day1B.txt"));
        final String[] lines = s.split("\n");
        final List<Integer> ints = Stream.of(lines).map(Integer::parseInt).collect(Collectors.toList());

        Integer[] input = new Integer[ints.size()];
        input = ints.toArray(input);

        final int result = run(input);
        System.out.println(result);
    }
}
