package uk.ac.bournemouth.ap.battleships

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.core.view.GestureDetectorCompat
import org.example.student.battleshipgame.StudentBattleshipOpponent
import org.example.student.battleshipgame.StudentShip
import uk.ac.bournemouth.ap.battleshiplib.BattleshipOpponent
import uk.ac.bournemouth.ap.battleshiplib.GuessCell
import uk.ac.bournemouth.ap.battleshiplib.Ship

/**
 * This custom view represents the opponent
 */
class BattleshipGameOpponentView : BaseGameView {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
            context,
            attrs,
            defStyleAttr
    )

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


    override fun onDraw(canvas: Canvas) {
        canvas.translate(offsetX, offsetY)
        val gameWidth: Float = grid.columns * (squareLength+squareSpacing) + squareSpacing
        val gameHeight: Float = grid.rows * (squareLength+squareSpacing) + squareSpacing
        canvas.drawRect(0f, 0f, gameWidth, gameHeight, gridPaint)


        for (col in 0 until grid.columns) {
            for (row in 0 until grid.rows) {
                val top = (0 + (squareSpacing * (col+1) + (squareLength * col)))
                val left = (0 + (squareSpacing * (row+1) + (squareLength * row)))
                val bot = top + squareLength
                val right = left + squareLength

                when(opponentGrid[col, row]) {
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