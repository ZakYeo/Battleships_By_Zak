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
import androidx.core.view.GestureDetectorCompat
import com.google.android.material.snackbar.Snackbar
import org.example.student.battleshipgame.StudentBattleshipOpponent
import org.example.student.battleshipgame.StudentShip
import uk.ac.bournemouth.ap.battleshiplib.BattleshipOpponent
import uk.ac.bournemouth.ap.battleshiplib.GuessCell
import uk.ac.bournemouth.ap.battleshiplib.GuessResult
import uk.ac.bournemouth.ap.battleshiplib.Ship
import java.lang.IllegalArgumentException

class BattleshipGameView : BaseGameView {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    var opponent = StudentBattleshipOpponent(grid.opponent.ships)


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

    private val gestureDetector = GestureDetectorCompat(context, object:
    GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onSingleTapUp(e: MotionEvent?): Boolean {

            if (e != null && !grid.shipsSunk.all{ it } && !opponentGrid.shipsSunk.all{ it }) {
                val columnTouched = ((e.x - squareLength * squareSpacingRatio) / (squareLength + squareSpacing)).toInt()
                val rowTouched = ((e.y - squareLength * squareSpacingRatio) / (squareLength + squareSpacing)).toInt()

                try {
                    grid.shootAt(columnTouched, rowTouched)
                } catch (e: Exception) {
                    return false //Unsuccessful turn
                }

                opponentGrid.playRandomMove() ?: return false

                //val intent = Intent(context, MainActivity::class.java)
                if(grid.shipsSunk.all{ it }){ //Player has won
                    gameWon().show()
                    //context.startActivity(intent)
                }else if(opponentGrid.shipsSunk.all{ it }){ //Computer has won
                    gameWon(1).show()
                    //context.startActivity(intent)
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
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val size = measuredWidth.coerceAtMost(measuredHeight)
        setMeasuredDimension(size, size)
    }


    override fun onDraw(canvas: Canvas) {

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
                if(grid[col, row] == GuessCell.UNSET){
                    canvas.drawRect(top, left, bot, right, noPlayerPaint)
                } else if(grid[col, row] == GuessCell.MISS){
                    canvas.drawRect(top, left, bot, right, missPaint)
                } else if (shipInfo != null) {
                    if(grid[col, row] == GuessCell.HIT(shipInfo.index)){
                        canvas.drawRect(top, left, bot, right, hitPaint)
                    }else if(grid[col, row] == GuessCell.SUNK(shipInfo.index)){
                        canvas.drawRect(top, left, bot, right, sunkPaint)
                    }
                }
            }
        }

    }

    fun gameWon(player: Int = 0): Snackbar{
        val message: String = if(player == 0){ //You
            "You have won!"
        } else if(player == 1){ //Opponent
            "You have lost!"
        } else{
            throw IllegalArgumentException("Player must be 0 or 1.")
        }
        return Snackbar.make(this, message, Snackbar.LENGTH_LONG)
    }

}