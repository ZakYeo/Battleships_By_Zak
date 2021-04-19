package uk.ac.bournemouth.ap.battleships

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.beginButton)
        button.setOnClickListener{
            val intent = Intent(this, BattleshipGameActivity::class.java)
            startActivity(intent)
        }

        //val shipOne = findViewById<ImageView>(R.id.battleship_main_image1)
        //val shipTwo = findViewById<ImageView>(R.id.battleship_main_image2)

        //val displayMetrics = DisplayMetrics()

        //shipOne.layout(10, 10, 10, 10)
        //shipOne.requestLayout();
        //shipOne.layoutParams.height = displayMetrics.heightPixels
        //shipOne.layoutParams.width = displayMetrics.widthPixels

    }
}