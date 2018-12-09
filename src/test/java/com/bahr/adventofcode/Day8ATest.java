package com.bahr.adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day8ATest {

    int counter = 0;
    char id = 'A';

    @Test
    void run() throws IOException {
        final String testInput = "2 3 0 3 10 11 12 1 1 0 1 99 2 1 1 2";

        final int testResult = getMetadataCount(testInput);
        assertEquals(138, testResult);

        counter = 0;
        final String s = Files.readString(Paths.get("./src/test/resources/day8.txt"));
        final int result = getMetadataCount(s.trim().replace("\n", ""));
        System.out.println(result);
    }

    private int getMetadataCount(String input) {
        final String[] s = input.split(" ");
        final Integer[] ints = convertToInts(s);
        final Node root = createNode(ints);

        return root.getSubMetadataCount();
    }

    private Node createNode(Integer[] entries) {
        final Node node = new Node(id++);
        // process headers
        final Integer childrenCount = entries[counter++];
        node.setChildCount(childrenCount);
        final Integer metadataCount = entries[counter++];
        node.setMetadataCount(metadataCount);
        // process children
        for (int i = 0; i < childrenCount; i++) {
            node.addNode(createNode(entries));
        }
        // process metadata
        for (int i = 0; i < metadataCount; i++) {
            node.addMetadata(entries[counter++]);
        }
        return node;
    }

    private Integer[] convertToInts(String[] s) {
        Integer[] result = new Integer[s.length];
        for (int i = 0; i < s.length; i++) {
            result[i] = Integer.parseInt(s[i]);
        }
        return result;
    }

    private class Node {
        private int id;
        private int childCount;
        private int metadataCount;
        private List<Integer> metadataEntries = new ArrayList<>();
        private List<Node> subNodes = new ArrayList<>();

        public Node(int id) {
            this.id = id;
        }

        public void setChildCount(int childCount) {
            this.childCount = childCount;
        }

        public void setMetadataCount(int metadataCount) {
            this.metadataCount = metadataCount;
        }

        public int getId() {
            return id;
        }

        public int getChildCount() {
            return childCount;
        }

        public int getMetadataCount() {
            return metadataCount;
        }

        public List<Node> getSubNodes() {
            return subNodes;
        }

        public void addNode(Node node) {
            subNodes.add(node);
        }

        public void addMetadata(int metadata) {
            metadataEntries.add(metadata);
        }

        public int getSubMetadataCount() {
            int result = metadataEntries.stream().mapToInt(i -> i).sum();
            for (Node subNode : subNodes) {
                result += subNode.getSubMetadataCount();
            }
            return result;
        }
    }

}
