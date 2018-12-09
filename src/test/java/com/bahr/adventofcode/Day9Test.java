package com.bahr.adventofcode;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day9Test {

    private long[] playerScores;

    @Test
    void run() {
        assertEquals(32, getScore(9, 25));
        assertEquals(8317, getScore(10, 1618));
        assertEquals(146373, getScore(13, 7999));
        assertEquals(2764, getScore(17, 1104));
        assertEquals(54718, getScore(21, 6111));
        assertEquals(37305, getScore(30, 5807));

        System.out.println(getScore(423, 71944));
        System.out.println(getScore(423, 71944 * 100));
    }

    private long getScore(int players, int lastMarble) {
        playerScores = new long[players];
        int currentPlayer = 0;

        int currentMarble = 0;
        Node current = new Node(currentMarble);
        currentPlayer++;

        while (currentMarble <= lastMarble) {
            final Node node = new Node(++currentMarble);
            current = current.insert(node, currentPlayer);

            currentPlayer = (currentPlayer + 1) % players;
        }

        long result = 0;
        for (long playerScore : playerScores) {
            if (result < playerScore) {
                result = playerScore;
            }
        }
        return result;
    }

    private class Node {
        private Node left;

        private Node right;
        private int id;
        public Node(int id) {
            this.id = id;
        }

        private Node getLeft(int steps) {
            Node current = left;
            for (int i = 1; i < steps; i++) {
                current = current.left;
            }
            return current;
        }

        public Node insert(Node node, int currentPlayer) {
            if (null == right) {
                // have A point to B
                right = node;
                left = node;
                // and B point to A
                node.right = this;
                node.left = this;
            } else if (node.id % 23 == 0) {
                // First, the current player keeps the marble they would have placed, adding it to their score.
                playerScores[currentPlayer] += node.id;
                // In addition, the marble 7 marbles counter-clockwise from the current marble is removed from the circle
                final Node removable = getLeft(7);
                // -8 shall right point to -6 and vice versa
                getLeft(8).right = getLeft(6);
                getLeft(6).left = getLeft(8);
                // and also added to the current player's score.
                playerScores[currentPlayer] += removable.id;
                // The marble located immediately clockwise of the marble that was removed becomes the new current marble.
                return getLeft(6);
            } else {
                // remember the second marble to the right
                final Node temp = this.right.right;
                // replace the second marble to the right
                this.right.right = node;
                // have the second point to the old second (now third)
                node.right = temp;

                // let the nodes point left
                node.left = this.right;
                this.right.right.right.left = node;
            }
            return node;
        }
    }


}
