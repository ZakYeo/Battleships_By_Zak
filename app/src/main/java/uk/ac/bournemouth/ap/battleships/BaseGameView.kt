package uk.ac.bournemouth.ap.battleships

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import org.example.student.battleshipgame.StudentBattleshipGrid
import org.example.student.battleshipgame.StudentBattleshipOpponent
import uk.ac.bournemouth.ap.battleshiplib.BattleshipGrid
import uk.ac.bournemouth.ap.battleshiplib.GuessCell


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

    //Shared preferences allows for saving key variables
    protected val pref: SharedPreferences = context.getSharedPreferences("BattleshipsPref", 0)

    val difficulty = pref.getInt("difficulty", Context.MODE_PRIVATE)

    var columnSize =  pref.getInt("column_size", 10)
    var rowSize = pref.getInt("row_size", 10)

    protected var offsetX: Float = 0f
    protected var offsetY: Float = 0f

    private val gridPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply{
        style = Paint.Style.FILL
        color = Color.DKGRAY
    }

    private val noPlayerPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply{
        style = Paint.Style.FILL
        color = Color.GRAY
    }

    private val missPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply{
        style = Paint.Style.FILL
        color = Color.RED
    }

    private val hitPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply{
        style = Paint.Style.FILL
        color = Color.GREEN
    }

    private val sunkPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply{
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

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int){
        val cellW = (w / (grid.columns + (grid.columns + 1) * squareSpacingRatio))
        val cellH = (h / (grid.rows + (grid.rows + 1) * squareSpacingRatio))

        squareLength = minOf(cellW, cellH)
        squareSpacing = squareLength*squareSpacingRatio
        val gridWidth = grid.columns*(squareLength+squareSpacing)+squareSpacing
        val gridHeight = grid.rows*(squareLength+squareSpacing)+squareSpacing
        offsetX = (width-gridWidth) / 2
        offsetY = (height-gridHeight) / 2
    }

    /**
     * Actually draws a grid onto the screen. Uses rectangles and paints them a certain colour
     * to represent the state of that cell.
     *
     * @param canvas The Canvas used to draw
     * @param gridToUse The grid used for painting the cells
     */
    fun drawGrid(canvas: Canvas, gridToUse: StudentBattleshipGrid){
        canvas.translate(offsetX, offsetY)
        val gameWidth: Float = gridToUse.columns * (squareLength+squareSpacing) + squareSpacing
        val gameHeight: Float = gridToUse.rows * (squareLength+squareSpacing) + squareSpacing
        canvas.drawRect(0f, 0f, gameWidth, gameHeight, gridPaint)


        for (col in 0 until gridToUse.columns) {
            for (row in 0 until gridToUse.rows) {
                val top = (0 + (squareSpacing * (col+1) + (squareLength * col)))
                val left = (0 + (squareSpacing * (row+1) + (squareLength * row)))
                val bot = top + squareLength
                val right = left + squareLength

                when(gridToUse[col, row]) {
                    is GuessCell.UNSET -> canvas.drawRect(top, left, bot, right, noPlayerPaint)
                    is GuessCell.MISS -> canvas.drawRect(top, left, bot, right, missPaint)
                    is GuessCell.HIT -> canvas.drawRect(top, left, bot, right, hitPaint)
                    is GuessCell.SUNK -> canvas.drawRect(top, left, bot, right, sunkPaint)
                    else -> continue
                }

            }
        }
    }



}