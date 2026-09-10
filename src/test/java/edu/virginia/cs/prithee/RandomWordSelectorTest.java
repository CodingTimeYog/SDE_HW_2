package edu.virginia.cs.prithee;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("RandomWordSelector")
class RandomWordSelectorTest {

    @Test
    @DisplayName("only ever picks a word that exists in the poem")
    void picksAWordInRange() {
        RandomWordSelector selector = new RandomWordSelector();
        for (int attempt = 0; attempt < 200; attempt++) {
            int index = selector.selectIndex(114, Set.of());
            assertTrue(index >= 0 && index < 114, "index out of range: " + index);
        }
    }

    @Test
    @DisplayName("never picks a word that has already been used")
    void skipsExcludedWords() {
        RandomWordSelector selector = new RandomWordSelector();
        Set<Integer> used = new HashSet<>(Set.of(0, 1, 2, 3, 5, 6, 7, 8, 9));
        for (int attempt = 0; attempt < 100; attempt++) {
            assertEquals(4, selector.selectIndex(10, used));
        }
    }

    @Test
    @DisplayName("spreads its choices over the poem rather than favouring one word")
    void picksDifferentWords() {
        RandomWordSelector selector = new RandomWordSelector(new Random(42));
        Set<Integer> chosen = new HashSet<>();
        for (int attempt = 0; attempt < 50; attempt++) {
            chosen.add(selector.selectIndex(114, Set.of()));
        }
        assertTrue(chosen.size() > 1, "the selector kept returning the same word");
    }

    @Test
    @DisplayName("is repeatable when seeded, so a game can be replayed in a test")
    void isRepeatableWhenSeeded() {
        RandomWordSelector first = new RandomWordSelector(new Random(2026));
        RandomWordSelector second = new RandomWordSelector(new Random(2026));
        for (int attempt = 0; attempt < 20; attempt++) {
            assertEquals(first.selectIndex(114, Set.of()), second.selectIndex(114, Set.of()));
        }
    }

    @Test
    @DisplayName("complains when every word has been used")
    void complainsWhenNothingIsLeft() {
        RandomWordSelector selector = new RandomWordSelector();
        assertThrows(IllegalStateException.class, () -> selector.selectIndex(3, Set.of(0, 1, 2)));
    }
}
