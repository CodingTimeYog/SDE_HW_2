package edu.virginia.cs.prithee;

/**
 * Decides whether what the player typed counts as the missing word.
 *
 * <p>The player is prompted in a hurry, mid-performance, so the comparison is forgiving:
 * surrounding whitespace and punctuation are ignored, case is ignored, and an apostrophe
 * may be left out ("owst" is accepted for "ow'st"). Everything else must match.</p>
 */
public final class GuessEvaluator {

    private GuessEvaluator() {
    }

    /**
     * Reduces a word to the form used for comparison: lower case, no surrounding
     * punctuation or whitespace.
     *
     * @param word the word to normalize; may be null
     * @return the normalized word, or an empty string if the input was null or blank
     */
    public static String normalize(String word) {
        if (word == null) {
            return "";
        }
        return word.trim()
                .toLowerCase()
                .replaceAll("^[^\\p{L}']+", "")
                .replaceAll("[^\\p{L}']+$", "");
    }

    /**
     * Decides whether a guess names the hidden word.
     *
     * @param guess    what the player typed
     * @param expected the word that was hidden
     * @return true when the guess names the hidden word
     */
    public static boolean matches(String guess, String expected) {
        String normalizedGuess = normalize(guess);
        String normalizedExpected = normalize(expected);
        if (normalizedExpected.isEmpty()) {
            return false;
        }
        return normalizedGuess.equals(normalizedExpected)
                || withoutApostrophes(normalizedGuess).equals(withoutApostrophes(normalizedExpected));
    }

    private static String withoutApostrophes(String word) {
        return word.replace("'", "");
    }
}
