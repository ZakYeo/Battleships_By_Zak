package uk.ac.bournemouth.ap.battleships

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.core.view.GestureDetectorCompat
import com.google.android.material.snackbar.Snackbar
import org.example.student.battleshipgame.StudentBattleshipOpponent
import org.example.student.battleshipgame.StudentShip
import uk.ac.bournemouth.ap.battleshiplib.BattleshipOpponent
import uk.ac.bournemouth.ap.battleshiplib.GuessCell
import uk.ac.bournemouth.ap.battleshiplib.GuessResult
import uk.ac.bournemouth.ap.battleshiplib.Ship
import java.lang.IllegalArgumentException

/**
 * This custom view represent the player's grid. It has on touch functionality that allows cells on
 * the grid to be shot at, and has turn-taking functionality. Also checks if the game has been won,
 * and if it has, disallow the game to continue.
 */
class BattleshipGameView : BaseGameView {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )


    var opponent = StudentBattleshipOpponent(grid.opponent.ships, rowSize, columnSize)


    private val gestureDetector = GestureDetectorCompat(context, object:
    GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        /**
         * When this view is touched by the player, the cell touched will be "shot" at. If there is
         * a ship at this location, score will be increased by a certain amount based on difficulty.
         * After the player has had a turn, the "AI" will automatically make a move in response.
         * If all ships have been sunk by either player, the game will finish.
         */
        override fun onSingleTapUp(e: MotionEvent?): Boolean {

            if (e != null && !grid.shipsSunk.all{ it } && !opponentGrid.shipsSunk.all{ it }) {
                val columnTouched =  (((e.x - squareLength * squareSpacingRatio)-offsetX) / (squareLength + squareSpacing)).toInt()
                val rowTouched = (((e.y - squareLength * squareSpacingRatio)-offsetY) / (squareLength + squareSpacing)).toInt()

                if(opponent.shipAt(columnTouched, rowTouched) != null){ //There is a ship here
                    grid.score += (100)*difficulty
                }

                //Player's turn
                try {
                    grid.shootAt(columnTouched, rowTouched)
                } catch (e: Exception) {
                    return false //Unsuccessful turn
                }

                //Opponent's turn
                opponentGrid.playMove(difficulty)

                if(grid.shipsSunk.all{ it }){ //Player has won
                    gameWon().show()
                }else if(opponentGrid.shipsSunk.all{ it }){ //Computer has won
                    gameWon(1).show()

                }
            }
            return super.onSingleTapUp(e)
        }
    })



    override fun onTouchEvent(event: MotionEvent): Boolean{
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event)
    }



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
     */
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

                when(grid[col, row]) {
                    is GuessCell.UNSET -> canvas.drawRect(top, left, bot, right, noPlayerPaint)
                    is GuessCell.MISS -> canvas.drawRect(top, left, bot, right, missPaint)
                    is GuessCell.HIT -> canvas.drawRect(top, left, bot, right, hitPaint)
                    is GuessCell.SUNK -> canvas.drawRect(top, left, bot, right, sunkPaint)
                    else -> continue
                }

            }
        }

    }

    /**
     * Displays a snackbar to notify the player who has won.
     *
     * @param player 0 - This is the player
     *               1 - This is the opponent
     */
    fun gameWon(player: Int = 0): Snackbar{
        val message: String = if(player == 0){ //Player
            "You have won!"
        } else if(player == 1){ //Opponent
            "You have lost!"
        } else{
            throw IllegalArgumentException("Player must be 0 or 1.")
        }
        return Snackbar.make(this, message, Snackbar.LENGTH_LONG)
    }

}