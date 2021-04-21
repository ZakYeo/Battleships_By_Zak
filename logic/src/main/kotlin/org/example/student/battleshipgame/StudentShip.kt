package org.example.student.battleshipgame

import uk.ac.bournemouth.ap.battleshiplib.Ship


/**
 *  A simple implementation of a ship. Stores the position of a ship and has two extension
 *  functions that allow for verification of the position of a particular ship
 *
 *  @param top    The Top-most cell of the ship
 *  @param left   The left-most cell of the ship
 *  @param bottom The bottom-most cell of the ship
 *  @param right  The right-most cell of the ship.
 */
open class StudentShip(override val top: Int, override val left: Int, override val bottom: Int,
                       override val right: Int): Ship {
    init {
        if(bottom < top || right < left){ //Inverted dimensions not allowed
            throw Exception("Inverted dimensions not allowed")
        }else if(width != 1 && height != 1){ //Width or height must be 1
            throw Exception("Width or height must be 1")
        }
    }
}

/**
 * Compares two ships to check if either of their coordinates are equal to each other (overlap)
 *
 * @param shipToCompare The ship to check for overlap against.
 *
 * @return Boolean to represent if the ships overlap
 */
fun Ship.overlaps(shipToCompare: Ship): Boolean{
    return (this.rowIndices.any { it in shipToCompare.rowIndices} &&
            this.columnIndices.any { it in shipToCompare.columnIndices})
}

/**
 * Checks the ship fits inside a grid given the rows and columns of that grid.
 *
 * @param rows    Represents the number of rows in the grid
 * @param columns Represents the number of columns in the grid
 *
 * @return Boolean to represent if the ship fits inside the given grid size
 */
fun Ship.checkFitsInGrid(rows: Int, columns: Int): Boolean {
    if (this.bottom > rows - 1 || this.top < 0 || this.left < 0 || this.right > columns - 1) {
        println(this.bottom > rows-1)
        println(this.right > columns - 1)
        return false
    }
    return true
}