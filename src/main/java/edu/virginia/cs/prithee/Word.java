package edu.virginia.cs.prithee;

/**
 * A single word of the sonnet, along with where it sits in the poem.
 *
 * @param index          position of this word among all words in the sonnet, starting at 0
 * @param lineNumber     zero-based index of the line this word appears on
 * @param positionInLine zero-based index of this word within its own line
 * @param raw            the word exactly as it appears in the sonnet, punctuation included
 */
public record Word(int index, int lineNumber, int positionInLine, String raw) {

    /**
     * The word with leading and trailing punctuation removed. This is what the player
     * is expected to type, so "day?" is answered with "day" and "untrimm'd;" with
     * "untrimm'd" (the interior apostrophe is part of the word).
     *
     * @return the raw word stripped of surrounding punctuation
     */
    public String answer() {
        return raw.replaceAll("^[^\\p{L}']+", "").replaceAll("[^\\p{L}']+$", "");
    }

    /**
     * The blank shown to the player in place of this word: one underscore per letter,
     * so the length of the missing word is a hint but the word itself is not.
     *
     * @return a run of underscores as long as {@link #answer()}
     */
    public String blank() {
        return "_".repeat(answer().length());
    }
}
