package uk.ac.bournemouth.ap.battleships

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import org.example.student.battleshipgame.StudentBattleshipGrid
import org.example.student.battleshipgame.StudentBattleshipOpponent
import uk.ac.bournemouth.ap.battleshiplib.BattleshipGrid
import uk.ac.bournemouth.ap.battleshiplib.BattleshipOpponent
import uk.ac.bournemouth.ap.battleshiplib.GuessCell
import uk.ac.bournemouth.ap.battleshiplib.Ship


/**
 * Acts as a base view class that the two grid custom views inherit
 *
 * @property grid The user's battleship grid.
 * @property opponentGrid The opponent's battleship grid.
 * @property difficulty Represents the difficulty the game is currently set to.
 * @property columnSize The current number of columns the game is using
 * @property rowSize The current number of rows the game is using
 */
open class BaseGameView : View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val pref: SharedPreferences = context.getSharedPreferences("BattleshipsPref", 0)

    val difficulty = pref.getInt("difficulty", Context.MODE_PRIVATE)

    var columnSize =  pref.getInt("column_size", 10)
    var rowSize = pref.getInt("row_size", 10)

    protected var offsetX: Float = 0f
    protected var offsetY: Float = 0f

    protected val gridPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply{
        style = Paint.Style.FILL
        color = Color.DKGRAY
    }

    protected val noPlayerPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply{
        style = Paint.Style.FILL
        color = Color.GRAY
    }

    protected val missPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply{
        style = Paint.Style.FILL
        color = Color.RED
    }

    protected val hitPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply{
        style = Paint.Style.FILL
        color = Color.GREEN
    }

    protected val sunkPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply{
        style = Paint.Style.FILL
        color = Color.TRANSPARENT
    }


    //Create a listener for our grid
    private val listener: BattleshipGrid.BattleshipGridListener =
            BattleshipGrid.BattleshipGridListener { _, _, _ -> invalidate() }

    private val opponent = StudentBattleshipOpponent(rowSize, columnSize)

    //Initialise the player's grid
    var grid: StudentBattleshipGrid =
        StudentBattleshipGrid(opponent).apply {
        addOnGridChangeListener(listener)
    }
        set(value) {
            field.removeOnGridChangeListener(listener)
            value.addOnGridChangeListener(listener)
            field = value
            onSizeChanged(width, height, width, height)
            invalidate()
        }

    //Initialise the opponent's grid
    var opponentGrid: StudentBattleshipGrid =
        StudentBattleshipGrid(opponent).apply {
                addOnGridChangeListener(listener)
            }

    //Variables for drawing the grid
    protected var squareLength: Float = 0f
    protected var squareSpacingRatio = ((grid.columns + grid.rows) / 100f) * 1.2f
    protected var squareSpacing: Float = 0f



}