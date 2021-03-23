package org.example.student.battleshipgame

import uk.ac.bournemouth.ap.battleshiplib.BattleshipGame
import uk.ac.bournemouth.ap.battleshiplib.BattleshipGrid

// TODO Create a class that implements BattleshipGame that contains the grids of the game
open class StudentBattleshipGame : BattleshipGame {
    // you need to implement this property, you may want to actually store grids though
    override val grids: List<BattleshipGrid>
        get() = TODO("Not yet implemented")
}