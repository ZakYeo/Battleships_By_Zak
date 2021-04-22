package uk.ac.bournemouth.ap.battleships


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import uk.ac.bournemouth.ap.battleshiplib.BattleshipGrid

/**
 * Acts as the activity where the battleships game takes place.
 */
class BattleshipGameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battleship_game)

        val battleshipGameBackButton = findViewById<Button>(R.id.battleshipGameBackButton)
        battleshipGameBackButton.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val mainView = findViewById<BattleshipGameView>(R.id.battleshipGameView)
        val opponentView = findViewById<BattleshipGameOpponentView>(R.id.battleshipGameOpponentView)

        val opponentGridString = getString(R.string.enemy_grid)
        val scoreTextView = findViewById<TextView>(R.id.opponentGridText)
        scoreTextView.text = opponentGridString.plus(" | SCORE: ").plus(mainView.grid.score)

        //This listener is used to allow the UI to keep an up-to-date score on screen.
        val listener: BattleshipGrid.BattleshipGridListener =
                BattleshipGrid.BattleshipGridListener { _, _, _ ->
                    scoreTextView.text = opponentGridString.plus(" | SCORE: ").plus(mainView.grid.score)
                }
        mainView.grid.apply{addOnGridChangeListener(listener)}

        mainView.opponentGrid = opponentView.opponentGrid

    }
}