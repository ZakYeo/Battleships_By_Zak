# Battleships By Zak |  22/04/2021


This project is battleships coded in Android Studio using Kotlin.

This project was an assignment for university and was my first time using Kotlin and Android Studio in such a manner.<br>

<h1>Additional Logic Features:</h1>
  <h2>A better computer player</h2>
      • The "AI" has difficulty levels 1 through to 10<br>
      • Difficulty 1 represents always random<br>
      • Difficulty 10 represents always correct<br>
      • Anywhere in between will have a probability of guessing correctly<br>
  <h2>The ability to make ships travel a set distance</h2>
      • Using the moveShipsRandomly() function, it will move specified ships in a random direction a specified distance<br>
      • It will not move ships into an invalid position (out of bounds or overlapping)<br>
      • The idea of this function was to grant the ability to make ships "travel" while playing to make the game harder / more enjoyable<br>
  <h2>The ability to track "score"</h2>
      • StudentBattleshipGrid now has a property to represent score<br>
      • It cannot be set below 0, and just allows for the UI to keep track of score for an individual grid<br><br>
      
 
<h1>Additional UI Features:</h1>
  <h2>Main menu</h2>
      • Upon first opening the app, you are greeted with a main menu where you may navigate to other areas including the game, highscores screen and the rules/settings screen
  <h2>Custom launcher icon</h2>
      • The app has a custom battleships-inspired custom launcher icon<br>
  <h2>Score tracker</h2>
      • While playing the game, score is tracked<br>
      • Score moves up if the player hits a ship<br>
      • Score moves down if the player misses a ship<br>
      • The difficulty acts as a multiplier and affects score gain/loss<br>
  <h2>High Score Screen</h2>
      • The high score activity allows the user to view their top 3 highest scores<br>
      • The high scores can be reset by pressing the "RESET" button<br>
  <h2>Rules / Settings Screen</h2>
      • This screen displays the rules for new users unfamiliar with battleships<br>
      • It also allows the user to edit the grid size by modifying the rows and colum values<br>
      • Users can also modify the difficulty here from 1-10.<br>
