package com.bahr.adventofcode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Day8BTest {

    int counter = 0;
    int id = 1;
    Node root;

    @Test
    void run() throws IOException {
        final String testInput = "2 3 0 3 10 11 12 1 1 0 1 99 2 1 1 2";

        final int testResult = getValue(testInput);
        assertEquals(66, testResult);

        counter = 0;
        final String s = Files.readString(Paths.get("./src/test/resources/day8.txt"));
        final int result = getValue(s.trim().replace("\n", ""));
        assertTrue(result < 38567);
        System.out.println(result);
    }

    private int getValue(String input) {
        final String[] s = input.split(" ");
        final Integer[] ints = convertToInts(s);
        root = createNode(ints);
        return root.getValue();
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

        public int getValue() {
            if (childCount == 0) {
                return metadataEntries.stream().mapToInt(i -> i).sum();
            } else {
                return metadataEntries.stream().mapToInt(this::getValueOfChildNode).sum();
            }
        }

        public Node findNode(int id) {
            if (this.id == id) {
                return this;
            } else if (childCount > 0) {
                for (Node subNode : subNodes) {
                    final Node node = subNode.findNode(id);
                    if (node != null) {
                        return node;
                    }
                }
            }
            return null;
        }

        private int getValueOfChildNode(int childNode) {
            if (childNode == 0) {
                return 0;
            } else {
                if (childNode > subNodes.size()) {
                    return 0;
                }
                Node node = subNodes.get(childNode - 1);
                if (node == null) {
                    return 0;
                }
                return node.getValue();
            }
        }

    }

}
