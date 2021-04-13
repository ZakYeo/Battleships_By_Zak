package org.example.student.battleshipgame

import uk.ac.bournemouth.ap.battleshiplib.Ship


/** A simple implementation of ship. You could change this if you want to, or add functionality.*/
open class StudentShip(override val top: Int, override val left: Int, override val bottom: Int,
                       override val right: Int): Ship {



    init {
        if(bottom < top || right < left){ //Inverted dimensions not allowed
            throw Exception("Inverted dimensions not allowed")
        }
    }
}

fun Ship.overlaps(shipToCompare: Ship): Boolean{
    return (this.rowIndices.any { it in shipToCompare.rowIndices} && this.columnIndices.any { it in shipToCompare.columnIndices})
}

fun Ship.verify(rows: Int, columns: Int): Boolean {
    if (this.bottom > rows - 1 || this.top < 0 || this.left < 0 || this.right > columns - 1) {
        return false
    } else if (this.width != 1 && this.height != 1) { //Width or height must be 1
        return false
    }
    return true
}