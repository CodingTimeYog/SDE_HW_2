package edu.virginia.cs.prithee;

import java.util.Set;

/**
 * Chooses which word the game hides next.
 *
 * <p>This is an interface so the game's rules can be tested without randomness: the real
 * game uses {@link RandomWordSelector}, while tests hand in a selector that returns a
 * known sequence of words.</p>
 */
@FunctionalInterface
public interface WordSelector {

    /**
     * Picks the index of the next word to hide.
     *
     * @param wordCount how many words the sonnet has; valid indexes are 0 to wordCount - 1
     * @param exclude   indexes already used this game, which must not be chosen again
     * @return the index of the word to hide
     * @throws IllegalStateException if every word has already been used
     */
    int selectIndex(int wordCount, Set<Integer> exclude);
}
