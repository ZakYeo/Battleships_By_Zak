package org.example.student.battleshipgame

import uk.ac.bournemouth.ap.battleshiplib.BattleshipGrid
import uk.ac.bournemouth.ap.battleshiplib.BattleshipGrid.BattleshipGridListener
import uk.ac.bournemouth.ap.battleshiplib.GuessCell
import uk.ac.bournemouth.ap.battleshiplib.GuessResult
import uk.ac.bournemouth.ap.lib.matrix.Matrix
import uk.ac.bournemouth.ap.lib.matrix.MutableMatrix

/**
 * This grid class describes the state of current guesses. It records which ships were sunk, where
 * shots were placed (with what results). It also records the [opponent](StudentBattleshipOpponent)
 *
 * @constructor Constructor that represents the actual state, this is needed when saving/loading
 *   the state.
 * @param guesses   The information on the ships in the grid.
 * @param opponent The actual opponent information.
 */
open class StudentBattleshipGrid protected constructor(
    guesses: Matrix<GuessCell>,
    override val opponent: StudentBattleshipOpponent,
) : BattleshipGrid {

    /**
     * Helper constructor for a fresh new game
     */
    constructor(opponent: StudentBattleshipOpponent) : this(
        MutableMatrix(
            opponent.columns,
            opponent.rows
        ) { _, _ -> GuessCell.UNSET }, opponent
    )

    /**
     * A list of listeners that should be informed if the game state changes.
     */
    private val onGridChangeListeners = mutableListOf<BattleshipGridListener>()

    /**
     * An array determining whether the ship with the given index was sunk. This can be used for
     * various purposes, including to determine whether the game has been won.
     *
     * @return An array with the status of sinking of each ship
     */
    override val shipsSunk: BooleanArray by lazy { BooleanArray(opponent.ships.size) }
    // This property is lazy to resolve issues with order of initialization.

    /**
     * A matrix with all guesses made in the game
     */
    private val guesses: MutableMatrix<GuessCell> = TODO("Initialise with a mutable matrix that has the values taken from the guesses constructor parameter")

    /**
     * Helper property to get the width of the game.
     */
    override val columns: Int get() = TODO("Get the width of the grid from another property such as opponent or guesses")

    /**
     * Helper property to get the height of the game.
     */
    override val rows: Int get() = TODO("Get the height of the grid from another property such as opponent or guesses")

    /*
     * Infrastructure to allow listening to game change events (and update the display
     * correspondingly)
     */

    /**
     * Register a listener for game changes
     *
     * @param listener The listener to register.
     */
    override fun addOnGridChangeListener(listener: BattleshipGridListener) {
        if (!onGridChangeListeners.contains(listener)) onGridChangeListeners.add(listener)
    }

    /**
     * Unregister a listener so that it no longer receives notifications of game changes
     *
     * @param listener The listener to unregister.
     */
    override fun removeOnGridChangeListener(listener: BattleshipGridListener) {
        onGridChangeListeners.remove(listener)
    }

    /**
     * Send a game change event to all registered listeners.
     *
     * @param column The column changed
     * @param row    The row changed
     */
    protected fun fireOnGridChangeEvent(column: Int, row: Int) {
        for (listener in onGridChangeListeners) {
            listener.onGridChanged(this, column, row)
        }
    }

    /**
     * The get operator allows retrieving the guesses at a location. You probably want to just look
     * the value up from a property you create (of type `MutableMatrix<GuessCell>`)
     */
    override operator fun get(column: Int, row: Int): GuessCell = TODO("Look up the value from state")

    /**
     * This method is core to the game as it implements the actual gameplay (after initial setup).
     */
    override fun shootAt(column: Int, row: Int): GuessResult {
        TODO("Check that the coordinates are in range")
        TODO("Check that the coordinate has not been tried already for this game")
        TODO("Determine from the opponent which ship (or none) is at the location, the index matches the index in opponent.ships")
        TODO("Update the grid state, remembering that if a ship is sunk, all its cells should be sunk")
        TODO("Return the result of the action as a child of GuessResult")
    }

}
