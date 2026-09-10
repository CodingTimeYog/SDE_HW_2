package edu.virginia.cs.prithee;

/**
 * Where a game stands: still being played, or finished one way or the other.
 */
public enum GameStatus {

    /** Neither three correct nor three incorrect words yet. */
    IN_PROGRESS,

    /** The player supplied three words correctly. */
    WON,

    /** The player missed three words. */
    LOST
}
