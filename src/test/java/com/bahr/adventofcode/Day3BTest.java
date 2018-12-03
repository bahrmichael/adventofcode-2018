package com.bahr.adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Day3BTest {

    @Test
    void run() throws IOException {
        final String input = "#1 @ 1,3: 4x4\n" +
                "#2 @ 3,1: 4x4\n" +
                "#3 @ 5,5: 2x2";
        final String result1 = getNonOverlap(input.split("\n"));
        assertEquals("#3", result1);

        final String s = Files.readString(Paths.get("./src/test/resources/day3.txt"));
        final String[] lines = s.split("\n");
        final String result = getNonOverlap(lines);
        System.out.println(result);
    }

    private String getNonOverlap(String[] lines) {
        final int bounds = 1300;
        final int[][] grid = new int[bounds][bounds];

        final List<Rectangle> rectangles = Stream.of(lines).map(this::getRectangle).collect(Collectors.toList());
        rectangles.forEach(rectangle -> {
            final Point start = rectangle.getPoint();
            for (int x = 0; x < rectangle.getWidth(); x++) {
                for (int y = 0; y < rectangle.getHeight(); y++) {
                    grid[start.x + x][start.y + y] += 1;
                }
            }
        });

        for (Rectangle rectangle : rectangles) {
            boolean nextRectangle = false;
            final Point start = rectangle.getPoint();
            for (int x = 0; x < rectangle.getWidth(); x++) {
                for (int y = 0; y < rectangle.getHeight(); y++) {
                    if (grid[start.x + x][start.y + y] > 1) {
                        nextRectangle = true;
                        break;
                    }
                }
                if (nextRectangle) {
                    break;
                }
            }
            if (!nextRectangle) {
                return rectangle.getId();
            }
        }

        throw new RuntimeException("No result was found.");
    }

    private Rectangle getRectangle(String line) {
        // #14 @ 181,225: 21x21
        final String[] split = line.split(" ");
        final String[] coordinates = split[2].replace(":", "").split(",");
        final String[] area = split[3].split("x");
        return new Rectangle(split[0],
                new Point(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1])),
                Integer.parseInt(area[0]), Integer.parseInt(area[1]));
    }

    private class Rectangle {
        private final String id;
        private final Point point;
        private final int width;
        private final int height;

        public Rectangle(String id, Point point, int width, int height) {
            this.id = id;
            this.point = point;
            this.width = width;
            this.height = height;
        }

        public String getId() {
            return id;
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
