package com.bahr.adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

class Day10Test {

    @Test
    void run() throws IOException {
        final String s = Files.readString(Paths.get("./src/test/resources/day10.txt"));
        doStuff(s.split("\n"));
    }

    private void doStuff(String[] lines) {
        final List<Star> stars = getStars(lines);

        int smallestSize = Integer.MAX_VALUE;
        int smallestStep = 0;

        int xMin = 0;
        int xMax = 0;
        int yMin = 0;
        int yMax = 0;

        for (int i = 0; i < 20000; i++) {
            final int seconds = i;
            xMin = stars.stream().mapToInt(s -> s.x + s.dX * seconds).min().orElse(0);
            xMax = stars.stream().mapToInt(s -> s.x + s.dX * seconds).max().orElse(0);
            yMin = stars.stream().mapToInt(s -> s.y + s.dY * seconds).min().orElse(0);
            yMax = stars.stream().mapToInt(s -> s.y + s.dY * seconds).max().orElse(0);

            int size = xMax - xMin + yMax - yMin;
            if (size < smallestSize) {
                smallestSize = size;
                smallestStep = i;
            } else if (size > smallestSize) {
                break;
            }
        }

        final int height = Math.abs(yMax);
        final int width = Math.abs(xMax);
        String[][] grid = new String[height][width];

        int finalSmallestStep = smallestStep;
        int finalYMin = yMin;
        int finalXMin = xMin;
        stars.forEach(s -> {
            s.y += s.dY * finalSmallestStep - finalYMin;
            s.x += s.dX * finalSmallestStep - finalXMin;
            grid[s.y][s.x] = "#";
        });

        final StringBuilder builder = new StringBuilder();
        for (int y = 0; y < height - finalYMin; y++) {
            for (int x = 0; x < width - finalXMin; x++) {
                if (grid[y][x] != null) {
                    builder.append(grid[y][x]);
                } else {
                    builder.append(".");
                }
            }
            builder.append("\n");
        }

        System.out.println(builder.toString());
        System.out.println(smallestStep);
    }

    private List<Star> getStars(String[] lines) {
        final List<Star> stars = new ArrayList<>();
        for (String line : lines) {
            final int x = Integer.parseInt(line.split("<")[1].split(",")[0].trim());
            final int y = Integer.parseInt(line.split(",")[1].split(">")[0].trim());
            final int dX = Integer.parseInt(line.split("velocity")[1].split("<")[1].split(",")[0].trim());
            final int dY = Integer.parseInt(line.split("velocity")[1].split(",")[1].split(">")[0].trim());

            final Star star = new Star(x, y, dX, dY);
            stars.add(star);
        }
        return stars;
    }

    private class Star {
        int x;
        int y;
        final int dX;
        final int dY;

        Star(int x, int y, int dX, int dY) {
            this.x = x;
            this.y = y;
            this.dX = dX;
            this.dY = dY;
        }
    }
}
