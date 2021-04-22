package uk.ac.bournemouth.ap.battleships

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class HighScoresActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_high_scores)

        val highScoresBackButton = findViewById<Button>(R.id.highScoresBackButton)
        val highScoresText = findViewById<TextView>(R.id.highScoreTextView)

        val pref = applicationContext.getSharedPreferences("BattleshipsPref", 0)

        highScoresBackButton.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        highScoresText.text = "1) ".plus(pref.getInt("firstHighScore", 0)).plus(
                "\n2) ").plus(pref.getInt("secondHighScore", 0)).plus(
                "\n3) ").plus(pref.getInt("thirdHighScore", 0))
    }
}