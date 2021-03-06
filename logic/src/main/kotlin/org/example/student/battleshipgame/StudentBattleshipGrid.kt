package org.example.student.battleshipgame

import uk.ac.bournemouth.ap.battleshiplib.*
import uk.ac.bournemouth.ap.battleshiplib.BattleshipGrid.BattleshipGridListener
import uk.ac.bournemouth.ap.lib.matrix.Matrix
import uk.ac.bournemouth.ap.lib.matrix.MutableMatrix
import java.lang.IllegalArgumentException
import kotlin.math.log10
import kotlin.random.Random

/**
 * This grid class describes the state of current guesses. It records which ships were sunk, where
 * shots were placed (with what results). It also records the [opponent](StudentBattleshipOpponent)
 *
 * @constructor    Constructor that represents the actual state, this is needed when saving/loading
 *                 the state.
 * @param guesses  The information on the ships in the grid.
 * @param opponent The actual opponent information.
 */
open class StudentBattleshipGrid protected constructor(
    guesses: Matrix<GuessCell>,
    override val opponent: StudentBattleshipOpponent,
) : BattleshipGrid {

    /**
     * Helper constructor for a fresh new game
     */
    constructor(opponent: StudentBattleshipOpponent = StudentBattleshipOpponent()) : this(
        MutableMatrix(
            opponent.columns,
            opponent.rows
        ) { _, _ -> GuessCell.UNSET }, opponent
    )

    /**
     * Acts as a tally to keep "score"
     */
    var score: Int = 0
        set(value){
        if(value < 0){
            throw Exception("Score cannot be lower than 0.")
        }
        field = value
    }

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
    //Initialise with a mutable matrix that has the values taken from the guesses constructor parameter
    private val guesses: MutableMatrix<GuessCell> = MutableMatrix(guesses)

    /**
     * Helper property to get the width of the game.
     */
    //Get the width of the grid from another property such as opponent or guesses
    override val columns: Int get() = opponent.columns

    /**
     * Helper property to get the height of the game.
     */
    //Get the height of the grid from another property such as opponent or guesses
    override val rows: Int get() = opponent.rows

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
     * The get operator allows retrieving the guesses at a location
     */
    override operator fun get(column: Int, row: Int): GuessCell = guesses[column, row]

    /**
     * This method is core to the game as it implements the actual gameplay (after initial setup).
     * @param column The column to shoot at
     * @param row The row to shoot at
     *
     * @return GuessResult The result of the state of the particular cell that has been shot.
     *                     If no ship has been hit, GuessResult.MISS will be returned.
     *                     Otherwise, GuessResult.HIT or .SUNK will be returned depending on
     *                     if the ship is now sunk.
     */
    override fun shootAt(column: Int, row: Int): GuessResult {

        //Check that the coordinates are in range
        if(column > this.columns || row > this.rows){
            throw IllegalArgumentException("Coordinates not in range.")
        }
        //Check that the coordinate has not been tried already for this game
        if(guesses[column, row] != GuessCell.UNSET){
            throw IllegalArgumentException("Coordinate already guessed!")
        }

        //Determine from the opponent which ship (or none) is at the location
        val shipInfo: BattleshipOpponent.ShipInfo<Ship>? = opponent.shipAt(column, row)

        //Update the grid state, remembering that if a ship is sunk, all its cells should be sunk
        return if(shipInfo != null){ //Ship hit
            shipHit(shipInfo, column, row)
        } else{ //Missed ship
            guesses[column, row] = GuessCell.MISS
            this.fireOnGridChangeEvent(column, row)
            GuessResult.MISS
        }

    }

    //Private function to handle the hitting of a ship
    private fun shipHit(shipHitInfo: BattleshipOpponent.ShipInfo<Ship>, column: Int, row: Int): GuessResult{
        guesses[column, row] = GuessCell.HIT(shipHitInfo.index)

        for(shipRow in opponent.ships[shipHitInfo.index].rowIndices){
            for(shipColumn in opponent.ships[shipHitInfo.index].columnIndices){
                //Check all cells are marked as HIT. If they aren't, we can mark it as HIT.
                if(guesses[shipColumn, shipRow] != GuessCell.HIT(shipHitInfo.index)){
                    this.fireOnGridChangeEvent(column, row)
                    return GuessResult.HIT(shipHitInfo.index)
                }
            }
        }
        //If all cells of the ship are marked as HIT, we should sink the ship.
        for(shipRow in opponent.ships[shipHitInfo.index].rowIndices){
            for(shipColumn in opponent.ships[shipHitInfo.index].columnIndices) {
                guesses[shipColumn, shipRow] = GuessCell.SUNK(shipHitInfo.index)
            }
        }
        this.shipsSunk[shipHitInfo.index] = true
        this.fireOnGridChangeEvent(column, row)
        return GuessResult.SUNK(shipHitInfo.index)
    }

    /**
     * Shoots at a random place on the grid. Works recursively. Can be called through playMove(1)
     *
     * @return GuessResult Same return type as shootAt, except it may be null.
     */
    private fun playRandomMove(): GuessResult?{
        val columnTarget = (0 until columns).random()
        val rowTarget = (0 until rows).random()
        return try{
            shootAt(columnTarget, rowTarget)
        } catch(e: IllegalArgumentException){ //Randomly generated move is illegal, retry
            playRandomMove()
        } catch(e: Exception){ //Some kind of other error has occurred, so exit.
            null
        }
    }

    /**
     * This function acts as an "AI". Increased difficulty means a higher probability the "AI" will
     * guess a correct cell. The probability is calculated using a simple formula involving log10.
     * This means difficulty must be bigger than or equal to 1, and less than or equal to 10.
     *
     * @param difficulty 1 represents perfectly random, 10 represents perfect playing. Anything
     *                   in-between has a probable outcome.
     *
     * @return GuessResult Same return type as shootAt, except it may be null.
     */
    fun playMove(difficulty: Int): GuessResult? {

        if(difficulty < 1 || difficulty > 10){
            throw Exception("Difficulty must be between 1 and 10.")
        }

        //This variable represents the probability the AI will guess a correct cell
        //Based on the difficulty level
        val probabilityOfGuaranteedCorrectGuess = 1-log10(11-difficulty.toDouble())
        val random = Random.nextFloat() //Random double between 0 and 1.

        //guesses.all{ it is GuessCell.UNSET || it is GuessCell.MISS} ||
        if(random > probabilityOfGuaranteedCorrectGuess){
            //Unsuccessful probable outcome; play a random move.
            return playRandomMove()
        }

        //Successful probable outcome:
        for(column in 0 until columns){
            for(row in 0 until rows){
                val shipInfo: BattleshipOpponent.ShipInfo<Ship>? = opponent.shipAt(column, row)
                if(shipInfo != null && guesses[column, row] is GuessCell.UNSET){ //ship found
                    return shootAt(column, row)
                }
            }
        }
        return null
    }
}
