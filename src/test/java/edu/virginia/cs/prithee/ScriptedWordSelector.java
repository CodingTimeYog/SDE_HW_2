package edu.virginia.cs.prithee;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * A {@link WordSelector} that hands back a fixed sequence of word indexes, so tests can
 * play a game whose hidden words are known in advance. It also records the exclusion set
 * it was given each time, which lets a test check that the game never reuses a word.
 */
class ScriptedWordSelector implements WordSelector {

    private final int[] indexes;
    private final List<Set<Integer>> exclusionsSeen = new ArrayList<>();
    private int callCount;

    ScriptedWordSelector(int... indexes) {
        this.indexes = indexes;
    }

    @Override
    public int selectIndex(int wordCount, Set<Integer> exclude) {
        exclusionsSeen.add(Set.copyOf(exclude));
        if (callCount >= indexes.length) {
            throw new IllegalStateException("The script ran out of words after " + callCount + " rounds");
        }
        return indexes[callCount++];
    }

    int callCount() {
        return callCount;
    }

    List<Set<Integer>> exclusionsSeen() {
        return exclusionsSeen;
    }
}
