package uk.ac.bournemouth.ap.battleships

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

/**
 *
 */
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

            //Attempt to assign the values of each edit text
            try{
                columnsEditTextValue = columnsEditText.text.toString().toInt()
                columnsEditTextValue = checkBoundaries(columnsEditTextValue, 5, 30)
            } catch(e: NumberFormatException){} //Edit text empty or invalid, so ignore

            try{
                rowsEditTextValue = rowsEditText.text.toString().toInt()
                rowsEditTextValue = checkBoundaries(rowsEditTextValue, 5, 30)
            } catch(e: NumberFormatException){} //Edit text empty or invalid, so ignore

            try{
                difficultyEditTextValue = difficultyEditText.text.toString().toInt()
                difficultyEditTextValue = checkBoundaries(difficultyEditTextValue, 1, 10)
            } catch(e: NumberFormatException){} //Edit text empty or invalid, so ignore


            //Now save the new settings in shared preferences if possible.
            editor.putInt("column_size", columnsEditTextValue?: pref.getInt("column_size", 10))
            editor.putInt("row_size", rowsEditTextValue?: pref.getInt("row_size", 10))
            editor.putInt("difficulty", difficultyEditTextValue?: pref.getInt("difficulty", 1))

            editor.apply() //Save changes

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * Simple function to make sure an integer is between a specified threshold. If it isn't, set it
     * to the minimum or maximum allowed value depending on if it is below or above the threshold.
     *
     * @param variable The Integer to be tested
     * @param lowerBound The lowest possible value the variable may be.
     * @param upperBound The highest possible value the variable may be.
     *
     * @return The new value, if changed at all.
     */
    private fun checkBoundaries(variable: Int, lowerBound: Int, upperBound: Int): Int {
        if(variable < lowerBound){
            return lowerBound
        } else if(variable > upperBound){
            return upperBound
        }
        return variable
    }
}