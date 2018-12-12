package com.bahr.adventofcode;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day11ATest {

    @Test
    void run() {
        assertEquals("Total 29 at 33,45", getResult(18));
        assertEquals("Total 30 at 21,61", getResult(42));
        System.out.println(getResult(4151));
    }

    private String getResult(int gridSerial) {
        final long[][] grid = buildGrid(gridSerial);

        long largestTotal = 0;
        int largestX = -1;
        int largestY = -1;
        for (int y = 1; y < 300 - 1; y++) {
            for (int x = 1; x < 300 - 1; x++) {

                long totalPower = getTotalPower(grid, y, x);

                if (totalPower > largestTotal) {
                    largestX = x;
                    largestY = y;
                    largestTotal = totalPower;
                }
            }
        }

        return "Total " + largestTotal + " at " + (largestX - 1) + "," + (largestY - 1);
    }

    private long getTotalPower(long[][] grid, int y, int x) {
        long power = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
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
        assertEquals(0, getPowerLevel(39, 196,217));
        assertEquals(4, getPowerLevel(71, 153,101));
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
