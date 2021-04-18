package uk.ac.bournemouth.ap.battleships

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class BattleshipGameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battleship_game)

        val mainView = findViewById<BattleshipGameView>(R.id.battleshipGameView)
        val opponentView = findViewById<BattleshipGameOpponentView>(R.id.battleshipGameOpponentView)

        opponentView.grid = mainView.grid
        opponentView.opponentGrid = mainView.opponentGrid
    }
}