package com.bahr.adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day3ATest {

    @Test
    void run() throws IOException {
        final String input = "#1 @ 1,3: 4x4\n" +
                "#2 @ 3,1: 4x4\n" +
                "#3 @ 5,5: 2x2";
        final int overlaps = doTheLoops(input.split("\n"));
        assertEquals(4, overlaps);

        final String s = Files.readString(Paths.get("./src/test/resources/day3.txt"));
        final String[] lines = s.split("\n");
        final int result = doTheLoops(lines);
        System.out.println(result);
    }

    private int doTheLoops(final String[] lines) {
        final int bounds = 1300;
        final int[][] grid = new int[bounds][bounds];

        Stream.of(lines).map(this::getRectangle).forEach(rectangle -> {
            final Point start = rectangle.getPoint();
            for (int x = 0; x < rectangle.getWidth(); x++) {
                for (int y = 0; y < rectangle.getHeight(); y++) {
                    grid[start.x + x][start.y + y] += 1;
                }
            }
        });

        return getOverlaps(bounds, grid);
    }

    private int getOverlaps(int bounds, int[][] grid) {
        int total = 0;
        for (int x = 0; x < bounds; x++) {
            for (int y = 0; y < bounds; y++) {
                if (grid[x][y] > 1) {
                    total += 1;
                }
            }
        }
        return total;
    }

    private Rectangle getRectangle(String line) {
        // #14 @ 181,225: 21x21
        final String[] split = line.split(" ");
        final String[] coordinates = split[2].replace(":", "").split(",");
        final String[] area = split[3].split("x");
        return new Rectangle(
                new Point(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1])),
                Integer.parseInt(area[0]), Integer.parseInt(area[1]));
    }

    @Test
    void overlaps() {
        final ThreadLocalRandom r = ThreadLocalRandom.current();
        int amount = r.nextInt(10);
        int[][] grid = new int[100][100];
        for (int i = 0; i < amount; i++) {
            grid[r.nextInt(100)][r.nextInt(100)] = 2;
        }
        final int overlaps = getOverlaps(100, grid);
        assertEquals(amount, overlaps);
    }

    private class Rectangle {
        private final Point point;
        private final int width;
        private final int height;

        public Rectangle(Point point, int width, int height) {
            this.point = point;
            this.width = width;
            this.height = height;
        }

        public Point getPoint() {
            return point;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }

    private class Point {
        private int x;
        private int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
