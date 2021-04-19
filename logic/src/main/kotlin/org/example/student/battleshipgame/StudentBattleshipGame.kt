package org.example.student.battleshipgame

import uk.ac.bournemouth.ap.battleshiplib.BattleshipGame
import uk.ac.bournemouth.ap.battleshiplib.BattleshipGrid

/**
 * Implements BattleshipGame that contains the grids of the game
 */
open class StudentBattleshipGame(gridList: List<StudentBattleshipGrid>) : BattleshipGame {
    private val _grids = gridList
    override val grids: List<BattleshipGrid>
        get() = this._grids
}