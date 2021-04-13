package uk.ac.bournemouth.ap.battleships

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.GestureDetectorCompat
import org.example.student.battleshipgame.StudentBattleshipGrid
import org.example.student.battleshipgame.StudentBattleshipOpponent
import org.example.student.battleshipgame.StudentShip
import uk.ac.bournemouth.ap.battleshiplib.BattleshipGrid
import uk.ac.bournemouth.ap.battleshiplib.BattleshipGrid.Companion.DEFAULT_SHIP_SIZES
import uk.ac.bournemouth.ap.battleshiplib.BattleshipOpponent
import uk.ac.bournemouth.ap.battleshiplib.GuessCell
import uk.ac.bournemouth.ap.battleshiplib.Ship
import kotlin.random.Random

class BattleshipGameView : Game {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val colCount = 10
    private val rowCount = 10

    private var squareLength: Float = 0f
    private var squareSpacingRatio = ((colCount + rowCount) / 100f) * 1.2f
    private var squareSpacing: Float = 0f


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

            if (e != null) {
                val columnTouched = ((e.x - squareLength * squareSpacingRatio) / (squareLength + squareSpacing)).toInt()
                val rowTouched = ((e.y - squareLength * squareSpacingRatio) / (squareLength + squareSpacing)).toInt()


                shoot(columnTouched, rowTouched)
            }
            return super.onSingleTapUp(e)
        }
    })



    override fun onTouchEvent(event: MotionEvent): Boolean{
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event)
    }



    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int){
        val cellW = (w / (colCount + (colCount + 1) * squareSpacingRatio))
        val cellH = (h / (rowCount + (rowCount + 1) * squareSpacingRatio))

        squareLength = minOf(cellW, cellH)
        squareSpacing = squareLength*squareSpacingRatio
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var size = measuredWidth.coerceAtMost(measuredHeight)
        setMeasuredDimension(size, size)
    }



    var opponent = StudentBattleshipOpponent(rowCount, colCount, grid.opponent.ships as List<StudentShip>)

    override fun onDraw(canvas: Canvas) {
        //super.onDraw(canvas)


        val gameWidth: Float = colCount * (squareLength+squareSpacing) + squareSpacing
        val gameHeight: Float = rowCount * (squareLength+squareSpacing) + squareSpacing
        canvas.drawRect(0f, 0f, gameWidth, gameHeight, gridPaint)


        for (col in 0 until colCount) {
            for (row in 0 until rowCount) {
                val top = (0 + (squareSpacing * (col+1) + (squareLength * col)))
                val left = (0 + (squareSpacing * (row+1) + (squareLength * row)))
                val bot = top + squareLength
                val right = left + squareLength

                var shipInfo: BattleshipOpponent.ShipInfo<Ship>? = opponent.shipAt(col, row)


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


}