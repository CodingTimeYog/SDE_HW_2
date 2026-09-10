package edu.virginia.cs.prithee;

import java.util.Scanner;

/**
 * Entry point for Prithee. Wires the sonnet, a random word selector, and the console
 * together, then plays one game.
 */
public class Main {

    private Main() {
    }

    /**
     * Starts the game.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        Sonnet sonnet = Sonnet.shallICompareThee();
        PritheeGame game = new PritheeGame(sonnet, new RandomWordSelector());
        try (Scanner input = new Scanner(System.in)) {
            new ConsoleGame(game, input, System.out).run();
        }
    }
}
