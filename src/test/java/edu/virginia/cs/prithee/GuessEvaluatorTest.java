package edu.virginia.cs.prithee;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("GuessEvaluator")
class GuessEvaluatorTest {

    @Test
    @DisplayName("accepts the exact word")
    void acceptsExactWord() {
        assertTrue(GuessEvaluator.matches("compare", "compare"));
    }

    @ParameterizedTest(name = "\"{0}\" answers \"summer''s\"")
    @ValueSource(strings = {"summer's", "SUMMER'S", "  summer's  ", "summer's,", "summers"})
    @DisplayName("forgives case, whitespace, punctuation and a dropped apostrophe")
    void forgivesHarmlessDifferences(String guess) {
        assertTrue(GuessEvaluator.matches(guess, "summer's"));
    }

    @ParameterizedTest(name = "\"{0}\" does not answer \"summer''s\"")
    @ValueSource(strings = {"summer", "winter's", "summers day", ""})
    @DisplayName("rejects a different word")
    void rejectsDifferentWord(String guess) {
        assertFalse(GuessEvaluator.matches(guess, "summer's"));
    }

    @Test
    @DisplayName("rejects a null guess without blowing up")
    void rejectsNullGuess() {
        assertFalse(GuessEvaluator.matches(null, "summer's"));
    }

    @ParameterizedTest(name = "normalize(\"{0}\") is \"{1}\"")
    @CsvSource({
            "'Shall', 'shall'",
            "'day?', 'day'",
            "'  May,  ', 'may'",
            "'untrimm''d;', 'untrimm''d'",
            "'   ', ''"
    })
    @DisplayName("normalizes to lower case without surrounding punctuation")
    void normalizesWords(String raw, String expected) {
        assertEquals(expected, GuessEvaluator.normalize(raw));
    }

    @Test
    @DisplayName("normalizes null to an empty string")
    void normalizesNull() {
        assertEquals("", GuessEvaluator.normalize(null));
    }
}
