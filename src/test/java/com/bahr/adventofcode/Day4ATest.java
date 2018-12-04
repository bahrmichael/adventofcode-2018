package com.bahr.adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day4ATest {

    final DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final String example = "[1518-11-01 00:00] Guard #10 begins shift\n" +
            "[1518-11-01 00:05] falls asleep\n" +
            "[1518-11-01 00:25] wakes up\n" +
            "[1518-11-01 00:30] falls asleep\n" +
            "[1518-11-01 00:55] wakes up\n" +
            "[1518-11-01 23:58] Guard #99 begins shift\n" +
            "[1518-11-02 00:40] falls asleep\n" +
            "[1518-11-02 00:50] wakes up\n" +
            "[1518-11-03 00:05] Guard #10 begins shift\n" +
            "[1518-11-03 00:24] falls asleep\n" +
            "[1518-11-03 00:29] wakes up\n" +
            "[1518-11-04 00:02] Guard #99 begins shift\n" +
            "[1518-11-04 00:36] falls asleep\n" +
            "[1518-11-04 00:46] wakes up\n" +
            "[1518-11-05 00:03] Guard #99 begins shift\n" +
            "[1518-11-05 00:45] falls asleep\n" +
            "[1518-11-05 00:55] wakes up";

    @Test
    void test() throws IOException {
        final int result = getResult(example.split("\n"));
        assertEquals(240, result);

        final String s = Files.readString(Paths.get("./src/test/resources/day4.txt"));
        final String[] lines = s.split("\n");
        final int solution = getResult(lines);
        System.out.println(solution);
    }

    private int getResult(String[] lines) {

        final List<String> sortedLines = Stream.of(lines).sorted(this::sortLines).collect(Collectors.toList());

        String currentGuard = "";
        LocalDateTime startSleep = null;
        Map<String, Integer[]> map = new HashMap<>();
        for (String line : sortedLines) {
            final String[] s = line.split(" ");
            final LocalDateTime dateTime = getTime(line);
            final String info = getInfo(s);

            if (info.startsWith("#")) {
                currentGuard = info;
                if (!map.containsKey(currentGuard)) {
                    map.put(currentGuard, initArray());
                }
            } else if (info.equals("falls")) {
                startSleep = dateTime;
            } else if (info.equals("wakes")) {
                final Integer[] asleepTime = map.get(currentGuard);
                for (int i = startSleep.getMinute(); i < dateTime.getMinute(); i++) {
                    asleepTime[i] += 1;
                }
            }
        }

        final String guardMostAsleep = findGuardMostAsleep(map);
        int maxAsleep = 0;
        int minute = -1;
        for (int i = 0; i < map.get(guardMostAsleep).length; i++) {
            final Integer m = map.get(guardMostAsleep)[i];
            if (m > maxAsleep) {
                minute = i;
                maxAsleep = m;
            }
        }

        return Integer.parseInt(guardMostAsleep.replace("#", "")) * minute;
    }

    private int sortLines(String a, String b) {
        return getTime(a).compareTo(getTime(b));
    }

    private LocalDateTime getTime(String s) {
        final String[] split = s.split(" ");
        final String time = (split[0] + " " + split[1]).replace("[", "").replace("]", "");
        return LocalDateTime.from(f.parse(time));
    }

    private Integer[] initArray() {
        final Integer[] array = new Integer[60];
        for (int i = 0; i < array.length; i++) {
            array[i] = 0;
        }
        return array;
    }

    private String findGuardMostAsleep(Map<String, Integer[]> map) {
        String guard = "";
        int minutes = 0;
        for (Map.Entry<String, Integer[]> entry : map.entrySet()) {
            final int sum = Stream.of(entry.getValue()).mapToInt(v -> v).sum();
            if (sum > minutes) {
                minutes = sum;
                guard = entry.getKey();
            }
        }
        return guard;
    }

    private String getInfo(String[] s) {
        if (s[2].equals("Guard")) {
            return s[3];
        } else {
            return s[2];
        }
    }
}
