package org.example.student.battleshipgame

import uk.ac.bournemouth.ap.battleshiplib.Ship

/** A simple implementation of ship. You could change this if you want to, or add functionality.*/
open class StudentShip: Ship {
    override val top: Int = TODO("Store or calculate the top position")
    override val left: Int = TODO("Store or calculate the left position")
    override val bottom: Int = TODO("Store or calculate the bottom position")
    override val right: Int = TODO("Store or calculate the right position")

    init {
        /* TODO Make sure to check that the arguments are valid: left<=right, top<=bottom and the
         * ship is only 1 wide */
    }
}
