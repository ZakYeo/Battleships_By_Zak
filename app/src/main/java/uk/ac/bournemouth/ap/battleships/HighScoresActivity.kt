package uk.ac.bournemouth.ap.battleships

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

/**
 * Activity to represent the high scores menu. Here you can view the highscores and reset them.
 */
class HighScoresActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_high_scores)

        val highScoresBackButton = findViewById<Button>(R.id.highScoresBackButton)
        val highScoresText = findViewById<TextView>(R.id.highScoreTextView)
        val resetButton = findViewById<Button>(R.id.resetButton)

        val pref = applicationContext.getSharedPreferences("BattleshipsPref", 0)

        highScoresBackButton.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        //Reset the high scores:
        resetButton.setOnClickListener{
            val editor = pref.edit()
            editor.putInt("firstHighScore", 0)
            editor.putInt("secondHighScore", 0)
            editor.putInt("thirdHighScore", 0)
            highScoresText.text = getString(R.string.high_scores_empty)
            editor.apply()
        }

        //Display the high scores:
        highScoresText.text = "1) ".plus(pref.getInt("firstHighScore", 0)).plus(
                "\n2) ").plus(pref.getInt("secondHighScore", 0)).plus(
                "\n3) ").plus(pref.getInt("thirdHighScore", 0))
    }
}