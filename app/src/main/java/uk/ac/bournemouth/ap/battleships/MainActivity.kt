package uk.ac.bournemouth.ap.battleships

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

/**
 * Main activity for the app. Acts as a title screen with buttons for navigation.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val beginButton = findViewById<Button>(R.id.beginButton)
        val highScoresButton = findViewById<Button>(R.id.highScoresButton)
        val rulesButton = findViewById<Button>(R.id.rulesButton)

        beginButton.setOnClickListener{
            val intent = Intent(this, BattleshipGameActivity::class.java)
            startActivity(intent)
        }

        highScoresButton.setOnClickListener{
            val intent = Intent(this, HighScoresActivity::class.java)
            startActivity(intent)
        }

        rulesButton.setOnClickListener{
            val intent = Intent(this, RulesActivity::class.java)
            startActivity(intent)
        }




    }
}