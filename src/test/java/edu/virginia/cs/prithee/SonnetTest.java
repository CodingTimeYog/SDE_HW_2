package edu.virginia.cs.prithee;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Sonnet")
class SonnetTest {

    private Sonnet sonnet;

    @BeforeEach
    void setUp() {
        sonnet = Sonnet.shallICompareThee();
    }

    @Test
    @DisplayName("holds all fourteen lines")
    void hasFourteenLines() {
        assertEquals(14, sonnet.lines().size());
    }

    @Test
    @DisplayName("indexes every word in the poem")
    void indexesEveryWord() {
        assertEquals(114, sonnet.wordCount());
        assertEquals("Shall", sonnet.word(0).raw());
        assertEquals("thee.", sonnet.word(sonnet.wordCount() - 1).raw());
        assertEquals("thee", sonnet.word(sonnet.wordCount() - 1).answer());
    }

    @Test
    @DisplayName("records where each word sits in the poem")
    void recordsWordPositions() {
        Word third = sonnet.word(2);
        assertEquals(2, third.index());
        assertEquals(0, third.lineNumber());
        assertEquals(2, third.positionInLine());
        assertEquals("compare", third.raw());
    }

    @Test
    @DisplayName("blanks the very first word and prints nothing else")
    void blanksFirstWord() {
        assertEquals("_____", sonnet.textUpToBlank(0));
    }

    @Test
    @DisplayName("prints the words before the blank on the blank's own line")
    void blanksWordInTheMiddleOfALine() {
        String text = sonnet.textUpToBlank(3);
        assertEquals("Shall I compare ____", text);
    }

    @Test
    @DisplayName("prints every earlier line in full, then stops at the blank")
    void blanksWordOnALaterLine() {
        int index = indexOfFirstWordOnLine(2);
        String text = sonnet.textUpToBlank(index);
        List<String> printedLines = List.of(text.split("\\R"));

        assertEquals(3, printedLines.size());
        assertEquals(sonnet.lines().get(0), printedLines.get(0));
        assertEquals(sonnet.lines().get(1), printedLines.get(1));
        assertEquals("_____", printedLines.get(2));
    }

    @Test
    @DisplayName("keeps the indentation of the couplet")
    void keepsIndentation() {
        int index = indexOfFirstWordOnLine(12);
        String text = sonnet.textUpToBlank(index);
        String lastLine = text.substring(text.lastIndexOf('\n') + 1);
        assertTrue(lastLine.startsWith("   _"), "expected the couplet's indent, got: " + lastLine);
    }

    @Test
    @DisplayName("never leaks the rest of the poem after the blank")
    void printsNothingAfterTheBlank() {
        String text = sonnet.textUpToBlank(0);
        assertFalse(text.contains("compare"));
        assertTrue(text.endsWith("_"));
    }

    @Test
    @DisplayName("rejects a poem with no words")
    void rejectsEmptyPoem() {
        assertThrows(IllegalArgumentException.class, () -> new Sonnet(List.of("", "   ")));
    }

    @Test
    @DisplayName("prints the whole poem when asked for it")
    void fullTextContainsEveryLine() {
        String full = sonnet.fullText();
        for (String line : sonnet.lines()) {
            assertTrue(full.contains(line.trim()), "full text is missing: " + line);
        }
    }

    private int indexOfFirstWordOnLine(int lineNumber) {
        return sonnet.words().stream()
                .filter(word -> word.lineNumber() == lineNumber && word.positionInLine() == 0)
                .findFirst()
                .orElseThrow()
                .index();
    }
}
