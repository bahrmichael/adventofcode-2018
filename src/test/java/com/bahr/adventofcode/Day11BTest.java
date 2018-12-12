package com.bahr.adventofcode;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day11BTest {

    @Test
    void run() {
        assertEquals("Total 113 at 90,269,16", getResult(18));
        assertEquals("Total 119 at 232,251,12", getResult(42));
        System.out.println(getResult(4151));
    }

    private String getResult(int gridSerial) {
        final long[][] grid = buildGrid(gridSerial);

        long largestTotal = 0;
        int largestX = -1;
        int largestY = -1;
        int largestLocalGridSize = 0;
        for (int localGridSize = 1; localGridSize <= 300; localGridSize++) {
            for (int y = 0; y < 300 - (localGridSize - 1); y++) {
                for (int x = 0; x < 300 - (localGridSize - 1); x++) {

                    long totalPower = getTotalPower(grid, y, x, localGridSize);

                    if (totalPower > largestTotal) {
                        largestX = x;
                        largestY = y;
                        largestLocalGridSize = localGridSize;
                        largestTotal = totalPower;
                    }
                }
            }
            System.out.println("Completed " + localGridSize + "/" + 300);
        }

        return "Total " + largestTotal + " at " + (largestX) + "," + (largestY) + "," + largestLocalGridSize;
    }

    private long getTotalPower(long[][] grid, int y, int x, int localGridSize) {
        long power = 0;
        for (int i = 0; i < localGridSize; i++) {
            for (int j = 0; j < localGridSize; j++) {
                power += grid[y + i][x + j];
            }
        }
        return power;
    }

    private long[][] buildGrid(int gridSerial) {
        final long[][] grid = new long[300][300];
        for (int y = 0; y < 300; y++) {
            for (int x = 0; x < 300; x++) {
                grid[y][x] = getPowerLevel(gridSerial, y, x);
            }
        }
        return grid;
    }

    @Test
    void testPowerLevel() {
        assertEquals(4, getPowerLevel(8, 5, 3));
        assertEquals(-5, getPowerLevel(57, 79, 122));
        assertEquals(0, getPowerLevel(39, 196, 217));
        assertEquals(4, getPowerLevel(71, 153, 101));
    }

    private long getPowerLevel(int gridSerial, int y, int x) {
        long rackId = x + 10;
        long powerLevel = rackId * y;
        powerLevel += gridSerial;
        powerLevel *= rackId;
        if (powerLevel < 100) {
            powerLevel = 0;
        } else {
            final String powerLevelStr = String.valueOf(powerLevel);
            powerLevel = Long.parseLong(String.valueOf(powerLevelStr.charAt(powerLevelStr.length() - 3)));
        }
        powerLevel -= 5;
        return powerLevel;
    }

}
