package edu.virginia.cs.prithee;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Word")
class WordTest {

    @Test
    @DisplayName("keeps a plain word as it is")
    void answerOfPlainWord() {
        assertEquals("Shall", new Word(0, 0, 0, "Shall").answer());
    }

    @Test
    @DisplayName("drops trailing punctuation")
    void answerDropsTrailingPunctuation() {
        assertEquals("day", new Word(0, 0, 0, "day?").answer());
        assertEquals("temperate", new Word(0, 0, 0, "temperate:").answer());
        assertEquals("May", new Word(0, 0, 0, "May,").answer());
    }

    @Test
    @DisplayName("keeps an apostrophe inside a word")
    void answerKeepsInteriorApostrophe() {
        assertEquals("untrimm'd", new Word(0, 0, 0, "untrimm'd;").answer());
        assertEquals("summer's", new Word(0, 0, 0, "summer's").answer());
    }

    @Test
    @DisplayName("blanks one underscore per letter of the answer, not the punctuation")
    void blankMatchesAnswerLength() {
        assertEquals("_____", new Word(0, 0, 0, "Shall").blank());
        assertEquals("___", new Word(0, 0, 0, "day?").blank());
        assertEquals("_________", new Word(0, 0, 0, "untrimm'd;").blank());
    }
}
