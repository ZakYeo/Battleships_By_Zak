package uk.ac.bournemouth.ap.battleships

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity


class RulesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rules)

        val columnsEditText = findViewById<EditText>(R.id.columnsEditText)
        val rowsEditText = findViewById<EditText>(R.id.rowsEditText)
        val rulesBackButton = findViewById<Button>(R.id.rulesBackButton)

        val pref = applicationContext.getSharedPreferences("BattleshipsPref", 0)

        val editor = pref.edit()


        rulesBackButton.setOnClickListener{
            editor.putInt("column_size", columnsEditText.text.toString().toInt())
            editor.putInt("row_size", rowsEditText.text.toString().toInt())
            editor.apply()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}