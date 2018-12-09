package com.bahr.adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.bahr.adventofcode.Day7ATest.buildGraph;
import static com.bahr.adventofcode.Day7ATest.hasPendingUpstream;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day7BTest {

    int seconds = 0;
    List<Worker> workers = new ArrayList<>();
    int workerCount = 0;
    int baseDelay = 0;

    @Test
    void run() throws IOException {
        final String testInput = "Step C must be finished before step A can begin.\n" +
                "Step C must be finished before step F can begin.\n" +
                "Step A must be finished before step B can begin.\n" +
                "Step A must be finished before step D can begin.\n" +
                "Step B must be finished before step E can begin.\n" +
                "Step D must be finished before step E can begin.\n" +
                "Step F must be finished before step E can begin.\n";

        baseDelay = 0;
        workerCount = 2;
        final String testResult = getTimedOrder(testInput.split("\n"));
        assertEquals("CABFDE", testResult);
        assertEquals(15, seconds);

        seconds = 0;
        baseDelay = 60;
        workerCount = 6;
        final String s = Files.readString(Paths.get("./src/test/resources/day7.txt"));
        final String result = getTimedOrder(s.split("\n"));
        System.out.println(result);
        System.out.println(seconds);
    }

    private String getTimedOrder(String[] lines) {
        final List<Day7ATest.Node> graph = buildGraph(lines);
        graph.sort(Day7ATest::sort);
        final StringBuilder builder = new StringBuilder();
        List<Day7ATest.Node> availableSteps = graph.stream()
                .filter(n -> n.getUp().isEmpty())
                .sorted((o1, o2) -> o1.getId().compareToIgnoreCase(o2.getId()))
                .collect(Collectors.toList());

        while (!availableSteps.isEmpty() || !workers.isEmpty()) {
            availableSteps = createWorker(builder, availableSteps);
            seconds++;
            availableSteps = processWorkers(builder, availableSteps);
        }

        return builder.toString();
    }

    private List<Day7ATest.Node> processWorkers(StringBuilder builder, List<Day7ATest.Node> availableSteps) {
        workers = workers.stream().sequential()
                .filter(w -> {
                    if (w.isDone()) {
                        builder.append(w.getStep().getId());
                        availableSteps.addAll(w.getStep().getDown());
                        return false;
                    } else {
                        return true;
                    }
                }).collect(Collectors.toList());

        return availableSteps.stream()
                .sorted((o1, o2) -> o1.getId().compareToIgnoreCase(o2.getId()))
                .collect(Collectors.toList());
    }

    private List<Day7ATest.Node> createWorker(StringBuilder builder, List<Day7ATest.Node> availableSteps) {
        if (availableSteps.isEmpty()) {
            return availableSteps;
        }
        final StringBuilder newNodesInProgress = new StringBuilder();

        // add new worker
        for (int i = 0; i < availableSteps.size() && workers.size() < workerCount; i++) {
            final Day7ATest.Node node = availableSteps.get(i);
            if (!hasPendingUpstream(node.getUp(), builder.toString()) && !newNodesInProgress.toString().contains(node.getId())) {
                workers.add(new Worker(node, seconds));
                newNodesInProgress.append(node.getId());
            }
        }

        availableSteps = availableSteps.stream()
                // leave out the current node
                .filter(n -> !newNodesInProgress.toString().contains(n.getId()))
                .sorted((o1, o2) -> o1.getId().compareToIgnoreCase(o2.getId()))
                .collect(Collectors.toList());
        return availableSteps;
    }

    private class Worker {
        private Day7ATest.Node step;
        private int started;

        public Worker(Day7ATest.Node step, int started) {
            this.step = step;
            this.started = started;
        }

        public boolean isDone() {
            return started + getDuration() <= seconds;
        }

        private int getDuration() {
            return step.getId().charAt(0) - 'A' + 1 + baseDelay;
        }

        public Day7ATest.Node getStep() {
            return step;
        }
    }
}
