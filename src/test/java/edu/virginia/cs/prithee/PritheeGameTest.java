package edu.virginia.cs.prithee;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("PritheeGame")
class PritheeGameTest {

    private Sonnet sonnet;

    @BeforeEach
    void setUp() {
        sonnet = Sonnet.shallICompareThee();
    }

    @Test
    @DisplayName("starts with an empty tally and no winner")
    void startsClean() {
        PritheeGame game = new PritheeGame(sonnet, new ScriptedWordSelector(0));
        assertEquals(0, game.correctCount());
        assertEquals(0, game.incorrectCount());
        assertEquals(GameStatus.IN_PROGRESS, game.status());
        assertFalse(game.isOver());
    }

    @Test
    @DisplayName("hides the word the selector chose and shows the poem up to it")
    void hidesTheSelectedWord() {
        PritheeGame game = new PritheeGame(sonnet, new ScriptedWordSelector(2));
        Round round = game.nextRound();

        assertEquals("compare", round.hiddenWord().answer());
        assertEquals("Shall I _______", round.prompt());
    }

    @Test
    @DisplayName("counts a right answer and keeps the game going")
    void countsCorrectGuess() {
        PritheeGame game = new PritheeGame(sonnet, new ScriptedWordSelector(2));
        game.nextRound();
        GuessResult result = game.submitGuess("compare");

        assertTrue(result.correct());
        assertEquals("compare", result.expected());
        assertEquals(GameStatus.IN_PROGRESS, result.status());
        assertEquals(1, game.correctCount());
        assertEquals(0, game.incorrectCount());
    }

    @Test
    @DisplayName("counts a wrong answer and reports the word that was missed")
    void countsIncorrectGuess() {
        PritheeGame game = new PritheeGame(sonnet, new ScriptedWordSelector(2));
        game.nextRound();
        GuessResult result = game.submitGuess("contrast");

        assertFalse(result.correct());
        assertEquals("compare", result.expected());
        assertEquals("contrast", result.guess());
        assertEquals(0, game.correctCount());
        assertEquals(1, game.incorrectCount());
    }

    @Test
    @DisplayName("is won after three correct words")
    void wonAfterThreeCorrect() {
        PritheeGame game = new PritheeGame(sonnet, new ScriptedWordSelector(0, 1, 2));
        playRounds(game, true, true, true);

        assertEquals(GameStatus.WON, game.status());
        assertTrue(game.isOver());
        assertEquals(3, game.correctCount());
    }

    @Test
    @DisplayName("is lost after three missed words")
    void lostAfterThreeIncorrect() {
        PritheeGame game = new PritheeGame(sonnet, new ScriptedWordSelector(0, 1, 2));
        playRounds(game, false, false, false);

        assertEquals(GameStatus.LOST, game.status());
        assertTrue(game.isOver());
        assertEquals(3, game.incorrectCount());
    }

    @Test
    @DisplayName("keeps going while neither tally has reached three")
    void continuesWhileMixed() {
        PritheeGame game = new PritheeGame(sonnet, new ScriptedWordSelector(0, 1, 2, 3, 4));
        playRounds(game, true, false, true, false);

        assertEquals(GameStatus.IN_PROGRESS, game.status());
        assertFalse(game.isOver());
        assertEquals(4, game.roundsPlayed());
    }

    @Test
    @DisplayName("plays past three rounds when the tallies stay split")
    void playsAFifthRound() {
        PritheeGame game = new PritheeGame(sonnet, new ScriptedWordSelector(0, 1, 2, 3, 4));
        playRounds(game, true, false, true, false, true);

        assertEquals(GameStatus.WON, game.status());
        assertEquals(3, game.correctCount());
        assertEquals(2, game.incorrectCount());
    }

    @Test
    @DisplayName("never offers the same word twice")
    void neverRepeatsAWord() {
        ScriptedWordSelector selector = new ScriptedWordSelector(10, 20, 30, 40, 50);
        PritheeGame game = new PritheeGame(sonnet, selector);
        playRounds(game, true, false, true, false, true);

        assertEquals(Set.of(), selector.exclusionsSeen().get(0));
        assertEquals(Set.of(10), selector.exclusionsSeen().get(1));
        assertEquals(Set.of(10, 20), selector.exclusionsSeen().get(2));
        assertEquals(Set.of(10, 20, 30, 40), selector.exclusionsSeen().get(4));
    }

    @Test
    @DisplayName("refuses a selector that hands back an already-used word")
    void refusesRepeatedWord() {
        PritheeGame game = new PritheeGame(sonnet, new ScriptedWordSelector(7, 7));
        game.nextRound();
        game.submitGuess("no");

        assertThrows(IllegalStateException.class, game::nextRound);
    }

    @Test
    @DisplayName("accepts an answer in any case, with stray punctuation")
    void acceptsForgivingAnswer() {
        PritheeGame game = new PritheeGame(sonnet, new ScriptedWordSelector(2));
        game.nextRound();

        assertTrue(game.submitGuess("  COMPARE! ").correct());
    }

    @Test
    @DisplayName("refuses a guess when no round is in progress")
    void refusesGuessWithoutRound() {
        PritheeGame game = new PritheeGame(sonnet, new ScriptedWordSelector(0));
        assertThrows(IllegalStateException.class, () -> game.submitGuess("shall"));
    }

    @Test
    @DisplayName("refuses a new round once the game is over")
    void refusesRoundAfterGameOver() {
        PritheeGame game = new PritheeGame(sonnet, new ScriptedWordSelector(0, 1, 2, 3));
        playRounds(game, true, true, true);

        assertThrows(IllegalStateException.class, game::nextRound);
    }

    @Test
    @DisplayName("draws its words from the sonnet it was given")
    void usesTheSonnetItWasGiven() {
        Set<Integer> hidden = new HashSet<>();
        PritheeGame game = new PritheeGame(sonnet, new RandomWordSelector());
        for (int round = 0; round < 3; round++) {
            Round played = game.nextRound();
            assertTrue(hidden.add(played.hiddenWord().index()), "the same word was hidden twice");
            assertEquals(sonnet.word(played.hiddenWord().index()), played.hiddenWord());
            game.submitGuess("wrong");
        }
        assertSame(sonnet, game.sonnet());
    }

    private static void playRounds(PritheeGame game, boolean... answerCorrectly) {
        for (boolean correct : answerCorrectly) {
            Round round = game.nextRound();
            game.submitGuess(correct ? round.hiddenWord().answer() : "not-the-word");
        }
    }
}
