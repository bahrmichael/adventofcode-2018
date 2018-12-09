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

        // pick the first marble
        Node current = new Node(currentMarble);
        currentPlayer++;

        while (currentMarble <= lastMarble) {
            // pick the next marble
            final Node node = new Node(++currentMarble);
            // and insert it
            current = current.insert(node, currentPlayer);

            currentPlayer = (currentPlayer + 1) % players;
        }

        return getTopScore();
    }

    private long getTopScore() {
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
        Node(int id) {
            this.id = id;
        }

        private Node move(int steps) {
            Node current = this;
            if (steps < 0) {
                current = left;
                for (int i = -1; i > steps; i--) {
                    current = current.left;
                }
                return current;
            } else if (steps > 0) {
                current = right;
                for (int i = 1; i < steps; i++) {
                    current = current.right;
                }
                return current;
            }
            return current;
        }

        Node insert(Node node, int currentPlayer) {
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
                final Node removable = move(-7);
                // -8 shall right point to -6 and vice versa
                move(-8).right = move(-6);
                move(-6).left = move(-8);
                // and also added to the current player's score.
                playerScores[currentPlayer] += removable.id;
                // The marble located immediately clockwise of the marble that was removed becomes the new current marble.
                return move(-6);
            } else {
                // remember the second marble to the right
                final Node temp = move(2);
                // replace the second marble to the right
                move(1).right = node;
                // have the second point to the old second (now third)
                node.right = temp;

                // let the nodes point left
                node.left = this.right;
                move(3).left = node;
            }
            return node;
        }
    }


}
