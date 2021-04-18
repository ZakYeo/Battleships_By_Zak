package uk.ac.bournemouth.ap.battleships

import android.content.Context
import android.util.AttributeSet
import android.view.View
import org.example.student.battleshipgame.StudentBattleshipGrid
import org.example.student.battleshipgame.StudentBattleshipOpponent
import org.example.student.battleshipgame.StudentShip
import uk.ac.bournemouth.ap.battleshiplib.BattleshipGrid
import kotlin.random.Random

open class BaseGameView : View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
            context,
            attrs,
            defStyleAttr
    )

    private val listener: BattleshipGrid.BattleshipGridListener =
            BattleshipGrid.BattleshipGridListener { grid, column, row -> invalidate() }


    var grid: StudentBattleshipGrid = StudentBattleshipGrid().apply {
        addOnGridChangeListener(listener)
    }
        set(value) {
            field.removeOnGridChangeListener(listener)
            value.addOnGridChangeListener(listener)
            field = value
            onSizeChanged(width, height, width, height)
            invalidate()
        }

    var opponentGrid: StudentBattleshipGrid = StudentBattleshipGrid().apply {
                addOnGridChangeListener(listener)
            }

        set(value) {
            field.removeOnGridChangeListener(listener)
            field = value
            value.addOnGridChangeListener(listener)
            onSizeChanged(width, height, width, height)
            invalidate()
        }

    var squareLength: Float = 0f
    var squareSpacingRatio = ((grid.columns + grid.rows) / 100f) * 1.2f
    var squareSpacing: Float = 0f

    @Deprecated("Outdated")
    fun shoot(column: Int, row: Int): Boolean {
        val columnTarget = (0 until grid.columns).random()
        val rowTarget = (0 until grid.rows).random()

        try {
            grid.shootAt(column, row)

        } catch (e: Exception) {
            println("Exception from user grid")
            return false //Unsuccessful turn
        }

        try {
            opponentGrid.shootAt(columnTarget, rowTarget)
        } catch (e: Exception) {
            println("opponent exception")
            return false //Unsuccessful turn
        }

        return true //Successful turn
    }


}