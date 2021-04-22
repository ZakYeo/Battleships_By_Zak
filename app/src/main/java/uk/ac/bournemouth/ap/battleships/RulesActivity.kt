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
        val difficultyEditText = findViewById<EditText>(R.id.difficultyEditText)
        val rulesBackButton = findViewById<Button>(R.id.rulesBackButton)

        val pref = applicationContext.getSharedPreferences("BattleshipsPref", 0)

        val editor = pref.edit()


        rulesBackButton.setOnClickListener{
            var columnsEditTextValue: Int? = null
            var rowsEditTextValue: Int? = null
            var difficultyEditTextValue: Int? = null

            try{
                columnsEditTextValue = columnsEditText.text.toString().toInt()
            } catch(e: NumberFormatException){} //Edit text empty or invalid, so ignore

            try{
                rowsEditTextValue = rowsEditText.text.toString().toInt()
            } catch(e: NumberFormatException){} //Edit text empty or invalid, so ignore

            try{
                difficultyEditTextValue = difficultyEditText.text.toString().toInt()
                if(difficultyEditTextValue > 10){
                    difficultyEditTextValue = 10
                } else if(difficultyEditTextValue < 1){
                    difficultyEditTextValue = 1
                }
            } catch(e: NumberFormatException){} //Edit text empty or invalid, so ignore


            editor.putInt("column_size", columnsEditTextValue?: pref.getInt("column_size", 10))
            editor.putInt("row_size", rowsEditTextValue?: pref.getInt("row_size", 10))
            editor.putInt("difficulty", difficultyEditTextValue?: pref.getInt("difficulty", 1))

            editor.apply()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}