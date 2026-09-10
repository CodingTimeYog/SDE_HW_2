package edu.virginia.cs.prithee;

/**
 * Decides whether what the player typed counts as the missing word.
 *
 * <p>The player is prompted in a hurry, mid-performance, so the comparison is forgiving:
 * surrounding whitespace and punctuation are ignored, case is ignored, and an apostrophe
 * may be left out ("owst" is accepted for "ow'st"). Everything else must match,
 * including digits and where an apostrophe falls.</p>
 */
public final class GuessEvaluator {

    private GuessEvaluator() {
    }

    /**
     * Reduces a word to the form used for comparison: lower case, with everything
     * that is not a letter or a digit trimmed from both ends. Digits are kept, so
     * "compare1" stays "compare1" and does not match "compare". Apostrophes inside
     * the word are kept; quote marks around it are trimmed.
     *
     * @param word the word to normalize; may be null
     * @return the normalized word, or an empty string if the input was null or held
     *         no letters or digits
     */
    public static String normalize(String word) {
        if (word == null) {
            return "";
        }
        return word.trim()
                .toLowerCase()
                .replaceAll("^[^\\p{L}\\p{N}]+", "")
                .replaceAll("[^\\p{L}\\p{N}]+$", "");
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
        // Only the hidden word loses its apostrophes, so "owst" matches "ow'st"
        // but an apostrophe in the wrong place ("o'wst") does not.
        return normalizedGuess.equals(normalizedExpected)
                || normalizedGuess.equals(withoutApostrophes(normalizedExpected));
    }

    private static String withoutApostrophes(String word) {
        return word.replace("'", "");
    }
}
