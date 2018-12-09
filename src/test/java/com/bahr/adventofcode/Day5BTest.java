package com.bahr.adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day5BTest {

    private final ExecutorService executorService = Executors.newFixedThreadPool(100);

    @Test
    void run() throws IOException {
        final int testResult1 = reduce("dabAcCaCBAcCcaDA");
        assertEquals(4, testResult1);

        final String s = Files.readString(Paths.get("./src/test/resources/day5.txt"));
        final int result = reduce(s.trim().replace("\n", ""));
        System.out.println(result);
    }

    private int reduce(final String input) {

        final List<Future<String>> futures = new ArrayList<>();

        int lowest = Integer.MAX_VALUE;
        for (Character i = 'a'; i <= 'z'; i++) {
            final String localInput = input.replace(i.toString(), "").replace(i.toString().toUpperCase(), "");
            futures.add(executorService.submit(new RunTask(localInput)));
        }

        for (Future<String> future : futures) {
            try {
                final String s = future.get();
                if (s.length() < lowest) {
                    lowest = s.length();
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }


        return lowest;
    }


    private String reduce2(final String text) {
        final String reduced = reduceOneStep(text);
        if (reduced.length() != text.length()) {
            return reduce2(reduced);
        } else {
            return reduced;
        }
    }

    private String reduceOneStep(String text) {
        int i = 0;
        while (i < text.length() - 1) {
            final Character c1 = text.charAt(i);
            final Character c2 = text.charAt(i + 1);
            if (c1.toString().toLowerCase().equals(c2.toString().toLowerCase()) && !c1.equals(c2)) {
                final String begin = text.substring(0, i);
                final String end = text.substring(i + 2);
                text = begin + end;
            }
            i++;
        }
        return text;
    }

    private class RunTask implements Callable<String> {

        final String input;

        private RunTask(String input) {
            this.input = input;
        }

        @Override
        public String call() throws Exception {
            return reduce2(input);
        }
    }

}
