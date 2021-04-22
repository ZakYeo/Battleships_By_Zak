package uk.ac.bournemouth.ap.battleships

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet

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

    override fun onDraw(canvas: Canvas) {
        drawGrid(canvas, opponentGrid)
    }
}