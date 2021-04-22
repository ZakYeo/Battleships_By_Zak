package uk.ac.bournemouth.ap.battleships

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.core.view.GestureDetectorCompat
import com.google.android.material.snackbar.Snackbar
import org.example.student.battleshipgame.StudentBattleshipOpponent
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
                } else{
                    try{
                        grid.score -= (100)*(difficulty/3)
                    }catch(e: Exception){ //Score cannot be negative, so set it to 0.
                        grid.score = 0
                    }

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


    //Overriding performClick is not needed here, so suppress the warning.
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean{
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event)
    }


    override fun onDraw(canvas: Canvas) {
        drawGrid(canvas, grid)

    }

    /**
     * Displays a snackbar to notify the player who has won.
     *
     * @param player 0 - This is the player
     *               1 - This is the opponent
     */
    fun gameWon(player: Int = 0): Snackbar{
        val message: String = when (player) {
            0 -> { //Player
                val firstHighScore = pref.getInt("firstHighScore", 0)
                val secondHighScore = pref.getInt("secondHighScore", 0)
                val thirdHighScore = pref.getInt("thirdHighScore", 0)
                val editor = pref.edit()

                when{
                    grid.score > firstHighScore -> { //Change highscores, and make sure they cascade
                        editor.putInt("firstHighScore", grid.score)
                        editor.putInt("secondHighScore", firstHighScore)
                        editor.putInt("thirdHighScore", secondHighScore)
                    }
                    grid.score > secondHighScore -> {
                        editor.putInt("secondHighScore", grid.score)
                        editor.putInt("thirdHighScore", secondHighScore)
                    }
                    grid.score > thirdHighScore -> {editor.putInt("thirdHighScore", grid.score)}
                }

                editor.apply()


                "You have won!"

            }
            1 -> { //Opponent
                "You have lost!"
            }
            else -> {
                throw IllegalArgumentException("Player must be 0 (you) or 1 (opponent).")
            }
        }
        return Snackbar.make(this, message, Snackbar.LENGTH_LONG)
    }

}