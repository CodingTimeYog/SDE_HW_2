package edu.virginia.cs.prithee;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("ConsoleGame")
class ConsoleGameTest {

    private static final Sonnet SONNET = Sonnet.shallICompareThee();

    @Test
    @DisplayName("prints the poem up to the blank and nothing after it")
    void printsThePoemUpToTheBlank() {
        String transcript = play(new ScriptedWordSelector(2, 3, 4), "wrong", "wrong", "wrong");

        assertTrue(transcript.contains("Shall I _______"), transcript);
    }

    @Test
    @DisplayName("says CORRECT when the player supplies the word")
    void reportsACorrectWord() {
        String transcript = play(new ScriptedWordSelector(2, 3, 4), "compare", "thee", "to");

        assertEquals(3, countOccurrences(transcript, "CORRECT"));
        assertTrue(transcript.contains("The preview is saved!"), transcript);
    }

    @Test
    @DisplayName("says ERROR and names the missed word")
    void reportsAMissedWord() {
        String transcript = play(new ScriptedWordSelector(2, 3, 4), "contrast", "nope", "neither");

        assertEquals(3, countOccurrences(transcript, "ERROR"));
        assertTrue(transcript.contains("the word was 'compare'"), transcript);
        assertTrue(transcript.contains("The curtain falls"), transcript);
    }

    @Test
    @DisplayName("restarts the sonnet at a different word each round")
    void restartsAtADifferentWordEachRound() {
        String transcript = play(new ScriptedWordSelector(0, 2, 4), "wrong", "wrong", "wrong");

        assertTrue(transcript.contains("Round 1"), transcript);
        assertTrue(transcript.contains("Round 2"), transcript);
        assertTrue(transcript.contains("Round 3"), transcript);
        assertTrue(transcript.contains("_____" + System.lineSeparator()), transcript);
        assertTrue(transcript.contains("Shall I _______"), transcript);
        assertTrue(transcript.contains("Shall I compare thee __"), transcript);
    }

    @Test
    @DisplayName("plays a fifth round when the player is at two correct and two missed")
    void playsUntilOneTallyReachesThree() {
        ScriptedWordSelector selector = new ScriptedWordSelector(0, 1, 2, 3, 4);
        PritheeGame game = new PritheeGame(SONNET, selector);
        String transcript = play(game, "Shall", "nope", "compare", "nope", "to");

        assertEquals(5, selector.callCount());
        assertEquals(3, game.correctCount());
        assertEquals(2, game.incorrectCount());
        assertTrue(transcript.contains("correct: 3, missed: 2"), transcript);
    }

    @Test
    @DisplayName("ignores a blank line and asks again")
    void ignoresBlankInput() {
        String transcript = play(new ScriptedWordSelector(2, 3, 4), "", "  ", "compare", "thee", "to");

        assertTrue(transcript.contains("Speak up, prithee."), transcript);
        assertTrue(transcript.contains("The preview is saved!"), transcript);
    }

    @Test
    @DisplayName("lets the player quit")
    void lettingThePlayerQuit() {
        String transcript = play(new ScriptedWordSelector(2, 3, 4), "quit");

        assertTrue(transcript.contains("Farewell!"), transcript);
        assertFalse(transcript.contains("CORRECT"), transcript);
    }

    @Test
    @DisplayName("stops gracefully when the input runs out")
    void stopsWhenInputRunsOut() {
        String transcript = play(new ScriptedWordSelector(2, 3, 4));

        assertTrue(transcript.contains("Farewell!"), transcript);
    }

    @Test
    @DisplayName("shows the whole sonnet once the game is over")
    void showsTheWholeSonnetAtTheEnd() {
        String transcript = play(new ScriptedWordSelector(2, 3, 4), "compare", "thee", "to");

        assertTrue(transcript.contains("So long lives this, and this gives life to thee."), transcript);
    }

    private static String play(WordSelector selector, String... answers) {
        return play(new PritheeGame(SONNET, selector), answers);
    }

    private static String play(PritheeGame game, String... answers) {
        String input = String.join(System.lineSeparator(), answers);
        ByteArrayOutputStream captured = new ByteArrayOutputStream();
        try (Scanner scanner = new Scanner(input);
             PrintStream output = new PrintStream(captured, true, StandardCharsets.UTF_8)) {
            new ConsoleGame(game, scanner, output).run();
        }
        return captured.toString(StandardCharsets.UTF_8);
    }

    private static int countOccurrences(String text, String needle) {
        int count = 0;
        int from = text.indexOf(needle);
        while (from >= 0) {
            count++;
            from = text.indexOf(needle, from + needle.length());
        }
        return count;
    }
}
