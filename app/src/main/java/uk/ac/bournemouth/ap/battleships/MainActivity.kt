package uk.ac.bournemouth.ap.battleships

import android.content.Intent
import android.content.SharedPreferences.Editor
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
        beginButton.setOnClickListener{
            val intent = Intent(this, BattleshipGameActivity::class.java)
            startActivity(intent)
        }

        val rulesButton = findViewById<Button>(R.id.rulesButton)
        rulesButton.setOnClickListener{
            val intent = Intent(this, RulesActivity::class.java)
            startActivity(intent)
        }


    }
}