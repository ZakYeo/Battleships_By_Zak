package org.example.student.battleshipgame


import uk.ac.bournemouth.ap.battleshiplib.BattleshipGrid.Companion.DEFAULT_COLUMNS
import uk.ac.bournemouth.ap.battleshiplib.BattleshipGrid.Companion.DEFAULT_ROWS
import uk.ac.bournemouth.ap.battleshiplib.BattleshipGrid.Companion.DEFAULT_SHIP_SIZES
import uk.ac.bournemouth.ap.battleshiplib.BattleshipOpponent
import uk.ac.bournemouth.ap.battleshiplib.BattleshipOpponent.ShipInfo
import kotlin.random.Random

/**
 * This opponent class handles the opponent side of things. Has game creation capabilities and holds
 * the list of ships.
 *
 * @constructor   Constructor that allows for game creation given dimensions and a list of placed ships
 *
 * @param ships   A list of all ships
 * @param rows    Represents the number of rows in the grid
 * @param columns Represents the number of columns in the grid
 */
open class StudentBattleshipOpponent(ships: List<StudentShip>,
                                     rows: Int = DEFAULT_ROWS,
                                     columns: Int = DEFAULT_COLUMNS) : BattleshipOpponent {

    private val _rows: Int = rows
    private val _columns: Int = columns
    private var _ships: List<StudentShip> = ships

    override val rows: Int get() = this._rows
    override val columns: Int get() = this._columns
    override val ships: List<StudentShip> get() = this._ships


    init{
        //Using a nested for loop and extension functions, verify the positions of the ships.
        //No two ships may overlap, and all ships must be placed within the boundaries of the grid.
        for(index in this._ships.indices){
            if(!this._ships[index].checkFitsInGrid(this._rows, this._columns)){
                throw Exception("Ship out of bounds.")
            }

            for(indexTwo in 0 until index){
                if(index == indexTwo){
                    continue //The ship should not compare with itself, so skip.
                }
                if(this._ships[index].overlaps(this._ships[indexTwo])){
                    throw Exception("Ship overlaps.")
                }
            }

        }
    }

    /**
    * @constructor     Using the secondary constructor gives the ability to generate a random game
    *                  by generating and placing ships in a brute-force fashion.
    *
    * @param rows      Represents the number of rows in the grid
    * @param columns   Represents the number of columns in the grid
    * @param shipSizes An int array filled with the sizes of each ship that should be placed.
    *                  For example, a ship of size 5 will cover 5 squares on a grid.
    * @param random    Allows for repeatable testing
    */
    constructor(rows: Int = DEFAULT_ROWS,
                columns: Int = DEFAULT_COLUMNS,
                shipSizes: IntArray = DEFAULT_SHIP_SIZES,
                random: Random = Random): this(emptyList(), rows, columns) {

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

        //Brute force the placement of ships until an arrangement is found that works.
        for (shipSize in shipSizes) {

            isVertical = when {
                shipSize >= columns -> true
                shipSize >= rows -> false
                else -> randomizer.nextBoolean()
            }

            do{
                if(shipSize < rows && shipSize < columns){
                    isVertical = randomizer.nextBoolean()
                }

                if(isVertical){
                    left = random.nextInt(0, columns)
                    right = left //Vertical ship
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
            //Successful placement, so add our new ship into the list of placed ships.
            placedShips.add(newShip)
        }
        this._ships = placedShips.toList()
    }


    /**
     * Determine whether there is a ship at the given coordinate
     *
     * @param column Represents the number of columns in the grid
     * @param row    Represents the number of rows in the grid
     *
     * @return ShipInfo if found, otherwise null.
     */
    override fun shipAt(column: Int, row: Int): ShipInfo<StudentShip>? {
        ships.forEachIndexed { i, ship ->
            if (column in ship.columnIndices && row in ship.rowIndices) { // Ship hit
                return ShipInfo(i, ship) //Allows O(1) indexing
            }
        }
        return null
    }
}

