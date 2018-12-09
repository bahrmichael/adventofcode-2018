package com.bahr.adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day6BTest {

    @Test
    void run() throws IOException {
        final String testInput = "1, 1\n" +
                "1, 6\n" +
                "8, 3\n" +
                "3, 4\n" +
                "5, 5\n" +
                "8, 9";

        final int testResult = getRegionSize(testInput.split("\n"), 32, 10);
        assertEquals(16, testResult);

        final String s = Files.readString(Paths.get("./src/test/resources/day6.txt"));
        final int result = getRegionSize(s.split("\n"), 10000, 400);
        System.out.println(result);
    }

    private int getRegionSize(final String[] split, final int maxDistance, final int gridSize) {
        final List<Point> points = new ArrayList<>();
        for (final String line : split) {
            final String[] coordinates = line.split(", ");
            final int x = Integer.parseInt(coordinates[0]);
            final int y = Integer.parseInt(coordinates[1]);
            points.add(new Point(x, y));
        }

        int result = 0;
        for (int y = 0; y < gridSize; y++) {
            for (int x = 0; x < gridSize; x++) {
                int distance = 0;
                for (Point point : points) {
                    final int ld = Math.abs(point.getX() - x) + Math.abs(point.getY() - y);
                    distance += ld;
                }
                if (distance < maxDistance) {
                    result++;
                }
            }
        }

        return result;
    }

    private class Point {
        private final int x;
        private final int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }
}
