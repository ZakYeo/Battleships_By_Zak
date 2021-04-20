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
 *
 */
class BattleshipGameOpponentView : BaseGameView {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
            context,
            attrs,
            defStyleAttr
    )

    private var offsetY: Float = 0f
    private var offsetX: Float = 0f
    var opponent = StudentBattleshipOpponent(opponentGrid.opponent.ships, columnSize, rowSize)



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

                val shipInfo: BattleshipOpponent.ShipInfo<Ship>? = opponent.shipAt(col, row)

                //TODO Use a when statement instead of if
                if(opponentGrid[col, row] == GuessCell.UNSET){
                    canvas.drawRect(top, left, bot, right, noPlayerPaint)
                } else if(opponentGrid[col, row] == GuessCell.MISS){
                    canvas.drawRect(top, left, bot, right, missPaint)
                } else if (shipInfo != null) {
                    if(opponentGrid[col, row] == GuessCell.HIT(shipInfo.index)){
                        canvas.drawRect(top, left, bot, right, hitPaint)
                    }else if(opponentGrid[col, row] == GuessCell.SUNK(shipInfo.index)){
                        canvas.drawRect(top, left, bot, right, sunkPaint)
                    }
                }
            }
        }
    }
}