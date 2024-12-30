package com.example.mobileappsassigment1

import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.GridLayout
import android.view.View
import android.widget.Button
import android.widget.TextView


class MainActivity : AppCompatActivity() {

    private val board = Array(3) { arrayOfNulls<String>(3) }
    private var currentPlayer = "X"
    private var gameActive = true
    private lateinit var gridLayout: GridLayout
    private lateinit var resultTextView: TextView
    private lateinit var playAgainButton: Button
    private lateinit var currentPlayerTextView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        gridLayout = findViewById<GridLayout>(R.id.gridLayout)
        resultTextView = findViewById<TextView>(R.id.tvResult)
        playAgainButton = findViewById<Button>(R.id.btnPlayAgain)
        currentPlayerTextView = findViewById(R.id.tvCurrentPlayer)

        updateCurrentPlayer()


        for (i in 0 until gridLayout.childCount) {
            val button = gridLayout.getChildAt(i) as Button
            val row = i / 3
            val col = i % 3

            button.setOnClickListener {
                if (gameActive && button.text.isEmpty()) {
                    button.text = currentPlayer
                    button.setTextColor(
                        if (currentPlayer == "X") Color.BLUE else Color.RED
                    )

                    board[row][col] = currentPlayer
                    if (checkWinner(row, col)) {
                        gameActive = false
                        resultTextView.text = "$currentPlayer Wins!"
                        resultTextView.visibility = View.VISIBLE
                        playAgainButton.visibility = View.VISIBLE
                        currentPlayerTextView.visibility = View.GONE
                    }else if (isBoardFull()) {
                        gameActive = false
                        resultTextView.text = "Draw!"
                        resultTextView.visibility = View.VISIBLE
                        playAgainButton.visibility = View.VISIBLE
                        currentPlayerTextView.visibility = View.GONE
                    }
                    currentPlayer = if (currentPlayer == "X") "O" else "X"
                    updateCurrentPlayer()
                }
            }
        }

        playAgainButton.setOnClickListener { resetGame() }
    }


    //Checking if there is a winner
    private fun checkWinner(row: Int, col: Int): Boolean {
        // Check row
        if (board[row].all { it == currentPlayer }) return true
        // Check column
        if (board.all { it[col] == currentPlayer }) return true
        // Check diagonals
        if (row == col && board.allIndexed { i, _ -> board[i][i] == currentPlayer }) return true
        if (row + col == 2 && board.allIndexed { i, _ -> board[i][2 - i] == currentPlayer }) return true
        return false
    }

    private fun <T> Array<T>.allIndexed(predicate: (Int, T?) -> Boolean): Boolean {
        for (index in indices) {
            if (!predicate(index, this[index])) {
                return false
            }
        }
        return true
    }

    //Checking if the board is full
    private fun isBoardFull(): Boolean = board.all { row -> row.all { it != null } }

    //Reset game
    private fun resetGame() {
        for (i in 0 until gridLayout.childCount) {
            (gridLayout.getChildAt(i) as Button).text = ""
        }
        for (row in board.indices) {
            for (col in board[row].indices) {
                board[row][col] = null
            }
        }
        currentPlayer = "X"
        gameActive = true
        resultTextView.visibility = View.GONE
        playAgainButton.visibility = View.GONE
        updateCurrentPlayer()
        currentPlayerTextView.visibility = View.VISIBLE
    }

    //Updating who is the current player to play
    private fun updateCurrentPlayer() {
        currentPlayerTextView.text = "Current Player: $currentPlayer"

        if (currentPlayer == "X") {
            currentPlayerTextView.setTextColor(Color.BLUE)
        } else {
            currentPlayerTextView.setTextColor(Color.RED)
        }

    }


}
