package edu.virginia.cs.prithee;

/**
 * One turn of the game: a hidden word and the partial sonnet leading up to it.
 *
 * @param hiddenWord the word the player must supply
 * @param prompt     the sonnet printed up to, and including, the blank
 */
public record Round(Word hiddenWord, String prompt) {
}
