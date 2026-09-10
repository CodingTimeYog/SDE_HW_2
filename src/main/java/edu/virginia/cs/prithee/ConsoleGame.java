package edu.virginia.cs.prithee;

import java.io.PrintStream;
import java.util.Optional;
import java.util.Scanner;

/**
 * Runs a {@link PritheeGame} in the console: prints each round's partial sonnet, reads
 * the player's word, and reports the result.
 *
 * <p>All input and output goes through the {@link Scanner} and {@link PrintStream} handed
 * to the constructor, so a test can run a whole game over strings instead of a terminal.</p>
 */
public class ConsoleGame {

    private static final String QUIT_COMMAND = "quit";

    private final PritheeGame game;
    private final Scanner input;
    private final PrintStream output;

    /**
     * Wires a game to a pair of streams.
     *
     * @param game   the game to play
     * @param input  where the player's words are read from
     * @param output where the sonnet and the results are printed
     */
    public ConsoleGame(PritheeGame game, Scanner input, PrintStream output) {
        this.game = game;
        this.input = input;
        this.output = output;
    }

    /**
     * Plays rounds until the game is won, lost, or the player quits.
     */
    public void run() {
        printWelcome();
        while (!game.isOver()) {
            Round round = game.nextRound();
            printRound(round);
            Optional<String> guess = readGuess();
            if (guess.isEmpty()) {
                output.println();
                output.println("The prompter has left the wings. Farewell!");
                return;
            }
            printResult(game.submitGuess(guess.get()));
        }
        printFarewell();
    }

    private void printWelcome() {
        output.println("=================================================");
        output.println("  PRITHEE - Blackfriars Playhouse, preview night");
        output.println("=================================================");
        output.println("An actor has forgotten the line. You are in the wings with the script.");
        output.println("The sonnet plays until it reaches a blank; supply the missing word.");
        output.println();
        output.println("Supply " + PritheeGame.ROUNDS_TO_WIN + " words correctly and the show is saved.");
        output.println("Miss " + PritheeGame.ROUNDS_TO_LOSE + " and the curtain falls.");
        output.println("Type '" + QUIT_COMMAND + "' at any prompt to leave the theatre.");
    }

    private void printRound(Round round) {
        output.println();
        output.println("--- Round " + (game.roundsPlayed() + 1)
                + "  (correct: " + game.correctCount()
                + ", missed: " + game.incorrectCount() + ") ---");
        output.println();
        output.println(round.prompt());
        output.println();
        output.print("Prithee, what is the word? ");
        output.flush();
    }

    private Optional<String> readGuess() {
        while (input.hasNextLine()) {
            String line = input.nextLine().trim();
            if (line.equalsIgnoreCase(QUIT_COMMAND)) {
                return Optional.empty();
            }
            if (!line.isEmpty()) {
                return Optional.of(line);
            }
            output.print("Speak up, prithee. What is the word? ");
            output.flush();
        }
        return Optional.empty();
    }

    private void printResult(GuessResult result) {
        output.println();
        if (result.correct()) {
            output.println("CORRECT - '" + result.expected() + "' it is. The player carries on.");
        } else {
            output.println("ERROR - the word was '" + result.expected()
                    + "', not '" + result.guess() + "'.");
        }
    }

    private void printFarewell() {
        output.println();
        output.println("=================================================");
        if (game.status() == GameStatus.WON) {
            output.println("You fed the players " + game.correctCount()
                    + " lines. The preview is saved!");
        } else {
            output.println("Three lines lost. The curtain falls on this preview.");
        }
        output.println("Final tally - correct: " + game.correctCount()
                + ", missed: " + game.incorrectCount() + ".");
        output.println("=================================================");
        output.println();
        output.println("The sonnet, entire:");
        output.println();
        output.println(game.sonnet().fullText());
    }
}
