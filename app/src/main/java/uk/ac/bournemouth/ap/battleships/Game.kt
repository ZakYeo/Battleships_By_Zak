package uk.ac.bournemouth.ap.battleships

import android.content.Context
import android.util.AttributeSet
import android.view.View
import org.example.student.battleshipgame.StudentBattleshipGrid
import org.example.student.battleshipgame.StudentBattleshipOpponent
import org.example.student.battleshipgame.StudentShip
import uk.ac.bournemouth.ap.battleshiplib.BattleshipGrid
import kotlin.random.Random

open class Game : View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
            context,
            attrs,
            defStyleAttr
    )

    private val colCount = 10
    private val rowCount = 10


    private val listener: BattleshipGrid.BattleshipGridListener =
            BattleshipGrid.BattleshipGridListener { grid, column, row -> invalidate() }


    public var grid: StudentBattleshipGrid = StudentBattleshipGrid(
            StudentBattleshipOpponent(
                    rowCount, colCount, intArrayOf(6, 6), Random
            )
    ).apply {
        addOnGridChangeListener(listener)
    }
        set(value) {
            field.removeOnGridChangeListener(listener)
            value.addOnGridChangeListener(listener)
            field = value
            onSizeChanged(width, height, width, height)
            invalidate()
        }

    public var opponentGrid: StudentBattleshipGrid = StudentBattleshipGrid(
            StudentBattleshipOpponent(
                    rowCount, colCount, intArrayOf(6, 6), Random
            )).apply {
                addOnGridChangeListener(listener)
            }
        set(value) {
            field.removeOnGridChangeListener(listener)
            field = value
            value.addOnGridChangeListener(listener)
            onSizeChanged(width, height, width, height)
            invalidate()
        }

    fun shoot(column: Int, row: Int) {
        var columnTarget = (0 until grid.columns).random()
        var rowTarget = (0 until grid.rows).random()

        try {
            grid.shootAt(column, row)

        } catch (e: Exception) {
            println("user Exception")
        }

        try {
            opponentGrid.shootAt(columnTarget, rowTarget)
        } catch (e: Exception) {
            println("opponent exception")
        }
    }


}