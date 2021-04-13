package org.example.student.battleshipgame

import sun.security.ec.point.ProjectivePoint
import uk.ac.bournemouth.ap.battleshiplib.BattleshipOpponent
import uk.ac.bournemouth.ap.battleshiplib.BattleshipOpponent.ShipInfo
import uk.ac.bournemouth.ap.battleshiplib.GuessCell
import uk.ac.bournemouth.ap.battleshiplib.Ship
import uk.ac.bournemouth.ap.lib.matrix.*
import java.lang.IllegalArgumentException
import kotlin.random.Random

/**
 * Suggested starting point for implementing an opponent class. Please note that your constructor
 * should test that it gets correct parameters. These tests should not be done in the test driver,
 * but actually here.
 *
 * TODO Create a constructor that creates a game given dimensions and a list of placed ships
 * TODO Create a way to generate a random game
 */
open class StudentBattleshipOpponent(rows: Int, columns: Int, ships: List<StudentShip>) : BattleshipOpponent {

    private val _rows: Int = rows
    private val _columns: Int = columns
    private var _ships: List<StudentShip> = ships

    override val rows: Int get() = this._rows
    override val columns: Int get() = this._columns
    override val ships: List<StudentShip> get() = this._ships


    init{
        //Using a nested for loop and extension functions, verify the positions and sizes of the
        //ships.
        for(index in this._ships.indices){
            if(!this._ships[index].verify(this._rows, this._columns)){
                throw Exception("Ship out of bounds or incorrect width.")
            }


            for(indexTwo in 0 until index){
                if(index == indexTwo){
                    continue
                }
                if(this._ships[index].overlaps(this._ships[indexTwo])){
                    throw Exception("Ship overlaps.")
                }
            }

        }

    }

    constructor(rows: Int, columns: Int, shipSizes: IntArray, random: Random): this(rows, columns, emptyList()) {

        if(rows <= 0 || columns <= 0){
            throw Exception("Invalid grid size.")
        }

        val placedShips = mutableListOf<StudentShip>()
        val randomizer = Random

        var newShip: StudentShip
        var top: Int
        var left: Int
        var bottom: Int
        var right: Int

        var isVertical: Boolean


        for (shipSize in shipSizes) {
            do{

                isVertical = if(shipSize < rows && shipSize < columns){
                    randomizer.nextBoolean() //Random orientation
                }else if(shipSize >= columns){
                    true //Force vertical
                }else if(shipSize >= rows){
                    false //Force horizontal
                }else{
                    throw Exception("Ship cannot fit.")
                }

                if(isVertical){
                    left = random.nextInt(0, columns)
                    right = left
                    top = random.nextInt(0, (rows-shipSize)+1)
                    bottom = top+shipSize-1
                } else{
                    top = random.nextInt(0, rows)
                    bottom = top //Horizontal Ship
                    left = random.nextInt(0, (columns-shipSize)+1)
                    right = left+shipSize-1
                }


                newShip = StudentShip(top, left, bottom, right)

            }while(placedShips.any{it.overlaps(newShip)})
            placedShips.add(newShip)
        }
        this._ships = placedShips.toList()
    }


    /**
     * Determine whether there is a ship at the given coordinate. If so, provide the shipInfo (index+ship)
     * otherwise `null`.
     */
    //TODO("find which ship is at the coordinate. You can either search through the ships or look it up in a precalculated matrix")
    override fun shipAt(column: Int, row: Int): ShipInfo<StudentShip>? {
        ships.forEachIndexed { i, ship ->
            if (column in ship.columnIndices && row in ship.rowIndices) { // Ship hit
                return ShipInfo(i, ship) //O(1) indexing
            }
        }
        return null
    }

}

