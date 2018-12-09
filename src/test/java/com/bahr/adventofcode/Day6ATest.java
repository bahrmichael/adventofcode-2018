package com.bahr.adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day6ATest {

    @Test
    void run() throws IOException {
        final String testInput = "1, 1\n" +
                "1, 6\n" +
                "8, 3\n" +
                "3, 4\n" +
                "5, 5\n" +
                "8, 9";

        final int testResult = getLargestAreaSize(testInput.split("\n"), 10);
        assertEquals(17, testResult);

        final String s = Files.readString(Paths.get("./src/test/resources/day6.txt"));
        final int result = getLargestAreaSize(s.split("\n"), 400);
        System.out.println(result);
    }

    private int getLargestAreaSize(final String[] split, final int gridSize) {
        final List<Point> points = new ArrayList<>();
        final String[][] grid = new String[gridSize][gridSize];
        for (char i = 0; i < split.length; i++) {
            final String[] coordinates = split[i].split(", ");
            final int x = Integer.parseInt(coordinates[0]);
            final int y = Integer.parseInt(coordinates[1]);
            final String id = Character.toString(i + 97).toUpperCase();
            grid[x][y] = id;
            points.add(new Point(x, y, id));
        }

        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                final String cell = grid[x][y];
                if (null == cell) {
                    final String point = calculateClosestPoint(x, y, points);
                    grid[x][y] = point.toLowerCase();
                }
            }
        }

        Map<String, Integer> sizes = new HashMap<>();
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                final String s = grid[x][y] == null ? "." : grid[x][y];
                if (!s.equals(".")) {
                    if (!sizes.containsKey(s.toLowerCase())) {
                        sizes.put(s.toLowerCase(), 0);
                    }
                    sizes.put(s.toLowerCase(), sizes.get(s.toLowerCase()) + 1);
                }
            }
        }

        final Set<String> infinteKeys = new HashSet<>();
        for (int y = 0; y < grid.length; y++) {
            infinteKeys.add(grid[0][y].toLowerCase());
            infinteKeys.add(grid[grid.length - 1][y].toLowerCase());
        }
        for (int x = 0; x < grid.length; x++) {
            infinteKeys.add(grid[x][0].toLowerCase());
            infinteKeys.add(grid[x][grid.length - 1].toLowerCase());
        }

        final OptionalInt result = sizes.entrySet().stream()
                .filter(e -> !infinteKeys.contains(e.getKey()))
                .mapToInt(e -> {
            System.out.println(e.getKey() + ":" + e.getValue());
            return e.getValue();
        }).max();
        return result.getAsInt();
    }

    private String calculateClosestPoint(int x, int y, List<Point> points) {
        int closestDistance = Integer.MAX_VALUE;
        Point result = null;
        for (final Point point : points) {
            int distance = Math.abs(point.getX() - x) + Math.abs(point.getY() - y);
            if (distance < closestDistance) {
                closestDistance = distance;
                result = point;
            } else if (distance == closestDistance) {
                result = null;
            }
        }
        return result == null ? "." : result.getId();
    }

    private class Point {
        private final int x;
        private final int y;
        private final String id;

        public Point(int x, int y, String id) {
            this.x = x;
            this.y = y;
            this.id = id;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public String getId() {
            return id;
        }
    }
}
