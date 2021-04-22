package uk.ac.bournemouth.ap.battleships

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import org.example.student.battleshipgame.StudentBattleshipGrid
import org.example.student.battleshipgame.StudentBattleshipOpponent
import uk.ac.bournemouth.ap.battleshiplib.BattleshipGrid


/**
 * Acts as a base view class that the two grid custom views inherit
 *
 * @property grid The user's battleship grid.
 * @property opponentGrid The opponent's battleship grid.
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

    val gridPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply{
        style = Paint.Style.FILL
        color = Color.DKGRAY
    }

    val noPlayerPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply{
        style = Paint.Style.FILL
        color = Color.GRAY
    }

    val missPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply{
        style = Paint.Style.FILL
        color = Color.RED
    }

    val hitPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply{
        style = Paint.Style.FILL
        color = Color.GREEN
    }

    val sunkPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply{
        style = Paint.Style.FILL
        color = Color.TRANSPARENT
    }

    private val listener: BattleshipGrid.BattleshipGridListener =
            BattleshipGrid.BattleshipGridListener { grid, column, row -> invalidate() }


    var grid: StudentBattleshipGrid =
        StudentBattleshipGrid(StudentBattleshipOpponent(rowSize, columnSize)).apply {
        addOnGridChangeListener(listener)
    }
        set(value) {
            field.removeOnGridChangeListener(listener)
            value.addOnGridChangeListener(listener)
            field = value
            onSizeChanged(width, height, width, height)
            invalidate()
        }

    var opponentGrid: StudentBattleshipGrid =
        StudentBattleshipGrid(StudentBattleshipOpponent(rowSize, columnSize)).apply {
                addOnGridChangeListener(listener)
            }

    var squareLength: Float = 0f
    var squareSpacingRatio = ((grid.columns + grid.rows) / 100f) * 1.2f
    var squareSpacing: Float = 0f
}