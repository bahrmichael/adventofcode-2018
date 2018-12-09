package com.bahr.adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day7ATest {

    @Test
    void run() throws IOException {
        final String testInput = "Step C must be finished before step A can begin.\n" +
                "Step C must be finished before step F can begin.\n" +
                "Step A must be finished before step B can begin.\n" +
                "Step A must be finished before step D can begin.\n" +
                "Step B must be finished before step E can begin.\n" +
                "Step D must be finished before step E can begin.\n" +
                "Step F must be finished before step E can begin.\n";

        final String testResult = getOrder(testInput.split("\n"));
        assertEquals("CABDFE", testResult);

        final String s = Files.readString(Paths.get("./src/test/resources/day7.txt"));
        final String result = getOrder(s.split("\n"));
        System.out.println(result);
    }

    private String getOrder(String[] lines) {
        final List<Node> graph = buildGraph(lines);
        graph.sort(Day7ATest::sort);
        final StringBuilder builder = new StringBuilder();
        List<Node> availableSteps = graph.stream()
                .filter(n -> n.getUp().isEmpty())
                .sorted((o1, o2) -> o1.getId().compareToIgnoreCase(o2.getId()))
                .collect(Collectors.toList());

        while (!availableSteps.isEmpty()) {
            availableSteps = processNextNode(builder, availableSteps);
        }

        return builder.toString();
    }

    private List<Node> processNextNode(StringBuilder builder, List<Node> availableSteps) {
        final Node nextNode = availableSteps.get(0);
        builder.append(nextNode.getId());
        availableSteps.addAll(nextNode.getDown());
        availableSteps = availableSteps.stream()
                // leave out the current node
                .filter(n -> !n.getId().equals(nextNode.getId()))
                // clear out nodes that have pending upstreams
                .filter(n -> !hasPendingUpstream(n.getUp(), builder.toString()))
                .collect(Collectors.toList());
        availableSteps.sort((o1, o2) -> o1.getId().compareToIgnoreCase(o2.getId()));
        return availableSteps;
    }

    static boolean hasPendingUpstream(List<Node> upstream, String progress) {
        for (Node up : upstream) {
            if (!progress.contains(up.getId())) {
                return true;
            }
        }
        return false;
    }

    static int sort(Node o1, Node o2) {
        // try to find a downstream chain from one node to the other
        boolean isDownstream = presentInDownstream(o1, o2.getId());
        if (isDownstream) {
            return -1;
        }
        boolean isUpstream = presentInUpstream(o1, o2.getId());
        if (isUpstream) {
            return 1;
        }
        return o1.getId().compareToIgnoreCase(o2.getId());
    }

    private static boolean presentInUpstream(Node o1, String id) {
        if (o1.getId().equals(id)) {
            return true;
        } else if (!o1.getUp().isEmpty()) {
            for (Node node : o1.getUp()) {
                if (presentInUpstream(node, id)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean presentInDownstream(Node o1, String id) {
        if (o1.getId().equals(id)) {
            return true;
        } else if (!o1.getDown().isEmpty()) {
            for (Node node : o1.getDown()) {
                if (presentInDownstream(node, id)) {
                    return true;
                }
            }
        }
        return false;
    }

    static List<Node> buildGraph(final String[] lines) {

        final List<Node> result = new ArrayList<>();
        for (final String line : lines) {
            final String[] split = line.split(" ");
            final String a = split[1];
            final String b = split[7];
            final Optional<Node> optionalA = findNode(a, result);
            final Optional<Node> optionalB = findNode(b, result);
            if (optionalA.isPresent()) {
                final Node nodeA = optionalA.get();

                if (optionalB.isPresent()) {
                    nodeA.getDown().add(optionalB.get());
                    optionalB.get().getUp().add(nodeA);
                } else {
                    final Node nodeB = new Node(b);
                    nodeB.getUp().add(nodeA);
                    nodeA.getDown().add(nodeB);
                    result.add(nodeB);
                }

            } else {
                final Node nodeA = new Node(a);

                if (optionalB.isPresent()) {
                    nodeA.getDown().add(optionalB.get());
                    optionalB.get().getUp().add(nodeA);
                } else {
                    final Node nodeB = new Node(b);
                    nodeB.getUp().add(nodeA);
                    nodeA.getDown().add(nodeB);
                    result.add(nodeB);
                }

                result.add(nodeA);
            }
        }

        return result;
    }

    private static Optional<Node> findNode(String id, List<Node> nodes) {
        return nodes.stream().filter(n -> n.getId().equals(id)).findFirst();
    }

    static class Node {
        private final String id;
        private final List<Node> up = new ArrayList<>();
        private final List<Node> down = new ArrayList<>();

        public Node(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public List<Node> getUp() {
            return up;
        }

        public List<Node> getDown() {
            return down;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "id='" + id + '\'' +
                    ", down=" + down +
                    '}';
        }
    }
}
