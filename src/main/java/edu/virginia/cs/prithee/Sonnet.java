package edu.virginia.cs.prithee;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An immutable poem, indexed word by word so any single word can be blanked out.
 *
 * <p>The sonnet is stored as its original lines. On construction those lines are also
 * flattened into a list of {@link Word}s; a word's index in that flat list is the handle
 * the rest of the program uses to talk about "the word we are hiding".</p>
 */
public final class Sonnet {

    private final List<String> lines;
    private final List<Word> words;

    /**
     * Builds a sonnet from its lines.
     *
     * @param lines the lines of the poem, in order; must contain at least one word
     * @throws IllegalArgumentException if the poem has no words
     */
    public Sonnet(List<String> lines) {
        this.lines = List.copyOf(lines);
        this.words = Collections.unmodifiableList(indexWords(this.lines));
        if (this.words.isEmpty()) {
            throw new IllegalArgumentException("A sonnet must contain at least one word");
        }
    }

    /**
     * The sonnet used by the game: Shakespeare's Sonnet 18.
     *
     * <p>Typographic apostrophes in the original are written here as plain ASCII
     * apostrophes so the poem prints correctly on every console.</p>
     *
     * @return Sonnet 18, "Shall I compare thee to a summer's day?"
     */
    public static Sonnet shallICompareThee() {
        return new Sonnet(List.of(
                "Shall I compare thee to a summer's day?",
                "Thou art more lovely and more temperate:",
                "Rough winds do shake the darling buds of May,",
                "And summer's lease hath all too short a date;",
                "Sometime too hot the eye of heaven shines,",
                "And often is his gold complexion dimm'd;",
                "And every fair from fair sometime declines,",
                "By chance or nature's changing course untrimm'd;",
                "But thy eternal summer shall not fade,",
                "Nor lose possession of that fair thou ow'st;",
                "Nor shall death brag thou wander'st in his shade,",
                "When in eternal lines to time thou grow'st:",
                "   So long as men can breathe or eyes can see,",
                "   So long lives this, and this gives life to thee."
        ));
    }

    /**
     * The lines of the poem, in order.
     *
     * @return the lines of the poem, in order
     */
    public List<String> lines() {
        return lines;
    }

    /**
     * Every word of the poem, in reading order.
     *
     * @return every word of the poem, in reading order
     */
    public List<Word> words() {
        return words;
    }

    /**
     * How many words the poem contains.
     *
     * @return how many words the poem contains
     */
    public int wordCount() {
        return words.size();
    }

    /**
     * The word at a given position in the poem.
     *
     * @param index position of the word in reading order
     * @return the word at that position
     * @throws IndexOutOfBoundsException if the index is not a word of this sonnet
     */
    public Word word(int index) {
        return words.get(index);
    }

    /**
     * The whole poem as one printable block of text.
     *
     * @return every line joined by newlines
     */
    public String fullText() {
        return String.join(System.lineSeparator(), lines);
    }

    /**
     * The poem as the player sees it during a round: everything up to the hidden word,
     * with that word replaced by underscores, and nothing after it.
     *
     * @param blankIndex position of the word to hide
     * @return the truncated, blanked poem
     * @throws IndexOutOfBoundsException if the index is not a word of this sonnet
     */
    public String textUpToBlank(int blankIndex) {
        Word blank = word(blankIndex);
        StringBuilder text = new StringBuilder();

        for (int i = 0; i < blank.lineNumber(); i++) {
            text.append(lines.get(i)).append(System.lineSeparator());
        }

        String blankedLine = lines.get(blank.lineNumber());
        text.append(indentOf(blankedLine));
        List<String> tokens = tokensOf(blankedLine);
        for (int i = 0; i < blank.positionInLine(); i++) {
            text.append(tokens.get(i)).append(' ');
        }
        text.append(blank.blank());

        return text.toString();
    }

    private static List<Word> indexWords(List<String> lines) {
        List<Word> indexed = new ArrayList<>();
        for (int lineNumber = 0; lineNumber < lines.size(); lineNumber++) {
            List<String> tokens = tokensOf(lines.get(lineNumber));
            for (int position = 0; position < tokens.size(); position++) {
                indexed.add(new Word(indexed.size(), lineNumber, position, tokens.get(position)));
            }
        }
        return indexed;
    }

    private static List<String> tokensOf(String line) {
        String trimmed = line.trim();
        if (trimmed.isEmpty()) {
            return List.of();
        }
        return List.of(trimmed.split("\\s+"));
    }

    private static String indentOf(String line) {
        int firstNonSpace = 0;
        while (firstNonSpace < line.length() && Character.isWhitespace(line.charAt(firstNonSpace))) {
            firstNonSpace++;
        }
        return line.substring(0, firstNonSpace);
    }
}
