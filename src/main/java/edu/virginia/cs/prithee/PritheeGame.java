package edu.virginia.cs.prithee;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * The rules of Prithee, with no console in sight.
 *
 * <p>A game is a sequence of rounds. Each round hides a word the game has not hidden
 * before and waits for a guess. The game ends as soon as the player has supplied
 * {@value #ROUNDS_TO_WIN} words correctly or missed {@value #ROUNDS_TO_LOSE}.</p>
 *
 * <p>Keeping the rules free of input and output is what makes them testable: a test
 * drives a whole game through {@link #nextRound()} and {@link #submitGuess(String)}
 * without any terminal involved.</p>
 */
public class PritheeGame {

    /** Correct words needed to win. */
    public static final int ROUNDS_TO_WIN = 3;

    /** Missed words that end the game. */
    public static final int ROUNDS_TO_LOSE = 3;

    private final Sonnet sonnet;
    private final WordSelector wordSelector;
    private final Set<Integer> usedWordIndexes = new LinkedHashSet<>();

    private int correctCount;
    private int incorrectCount;
    private Word currentWord;

    /**
     * Starts a new game over the given sonnet.
     *
     * @param sonnet       the poem to quiz the player on
     * @param wordSelector chooses which word each round hides
     */
    public PritheeGame(Sonnet sonnet, WordSelector wordSelector) {
        this.sonnet = sonnet;
        this.wordSelector = wordSelector;
    }

    /**
     * Begins the next round by hiding a word the game has not used yet.
     *
     * @return the round: the hidden word and the sonnet text to show the player
     * @throws IllegalStateException if the game is already over
     */
    public Round nextRound() {
        if (isOver()) {
            throw new IllegalStateException("The game is over; no further rounds may be played");
        }
        int index = wordSelector.selectIndex(sonnet.wordCount(), Collections.unmodifiableSet(usedWordIndexes));
        if (usedWordIndexes.contains(index)) {
            throw new IllegalStateException("Word " + index + " has already been used this game");
        }
        usedWordIndexes.add(index);
        currentWord = sonnet.word(index);
        return new Round(currentWord, sonnet.textUpToBlank(index));
    }

    /**
     * Scores the player's answer for the round in progress and updates the tally.
     *
     * @param guess what the player typed
     * @return whether the guess was right, the word that was hidden, and the game's new status
     * @throws IllegalStateException if no round is waiting on a guess
     */
    public GuessResult submitGuess(String guess) {
        if (currentWord == null) {
            throw new IllegalStateException("No round is in progress; call nextRound() first");
        }
        String expected = currentWord.answer();
        boolean correct = GuessEvaluator.matches(guess, expected);
        if (correct) {
            correctCount++;
        } else {
            incorrectCount++;
        }
        currentWord = null;
        return new GuessResult(correct, guess, expected, status());
    }

    /**
     * Whether the game has been won, lost, or is still going.
     *
     * @return whether the game has been won, lost, or is still going
     */
    public GameStatus status() {
        if (correctCount >= ROUNDS_TO_WIN) {
            return GameStatus.WON;
        }
        if (incorrectCount >= ROUNDS_TO_LOSE) {
            return GameStatus.LOST;
        }
        return GameStatus.IN_PROGRESS;
    }

    /**
     * True once the game has been won or lost.
     *
     * @return true once the game has been won or lost
     */
    public boolean isOver() {
        return status() != GameStatus.IN_PROGRESS;
    }

    /**
     * How many words the player has supplied correctly.
     *
     * @return how many words the player has supplied correctly
     */
    public int correctCount() {
        return correctCount;
    }

    /**
     * How many words the player has missed.
     *
     * @return how many words the player has missed
     */
    public int incorrectCount() {
        return incorrectCount;
    }

    /**
     * How many rounds have been played, won or lost.
     *
     * @return how many rounds have been played, won or lost
     */
    public int roundsPlayed() {
        return correctCount + incorrectCount;
    }

    /**
     * The sonnet this game is played over.
     *
     * @return the sonnet this game is played over
     */
    public Sonnet sonnet() {
        return sonnet;
    }
}
