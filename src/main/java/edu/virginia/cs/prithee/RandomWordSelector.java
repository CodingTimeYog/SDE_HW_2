package edu.virginia.cs.prithee;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Picks a word uniformly at random from the words not yet used this game.
 */
public class RandomWordSelector implements WordSelector {

    private final Random random;

    /**
     * Creates a selector seeded from the system clock.
     */
    public RandomWordSelector() {
        this(new Random());
    }

    /**
     * Creates a selector backed by a caller-supplied source of randomness, which lets a
     * test seed it and get a repeatable sequence of words.
     *
     * @param random the source of randomness
     */
    public RandomWordSelector(Random random) {
        this.random = random;
    }

    @Override
    public int selectIndex(int wordCount, Set<Integer> exclude) {
        List<Integer> available = new ArrayList<>();
        for (int index = 0; index < wordCount; index++) {
            if (!exclude.contains(index)) {
                available.add(index);
            }
        }
        if (available.isEmpty()) {
            throw new IllegalStateException("Every word in the sonnet has already been used");
        }
        return available.get(random.nextInt(available.size()));
    }
}
