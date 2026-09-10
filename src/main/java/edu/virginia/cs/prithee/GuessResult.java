package edu.virginia.cs.prithee;

/**
 * The outcome of one guess.
 *
 * @param correct  whether the guess named the hidden word
 * @param guess    what the player typed
 * @param expected the word that was hidden
 * @param status   where the game stands after this guess
 */
public record GuessResult(boolean correct, String guess, String expected, GameStatus status) {
}
