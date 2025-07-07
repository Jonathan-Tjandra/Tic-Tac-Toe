package com.example.tic_tac_toe;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Represents the players and the state of each tile
    private static final int EMPTY = 0;
    private static final int PLAYER_ONE = 1; // Represents 'O'
    private static final int PLAYER_TWO = 2; // Represents 'X'

    // Represents the possible outcomes of the game
    private static final int GAME_STATE_ONGOING = 0;
    private static final int GAME_STATE_PLAYER_ONE_WINS = 1;
    private static final int GAME_STATE_PLAYER_TWO_WINS = 2;
    private static final int GAME_STATE_DRAW = 3;

    // Game state variables
    private int currentPlayer;
    private int[] boardState;
    private boolean isGameOver;

    // Winning combinations
    private static final int[][] WINNING_COMBINATIONS = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, // Rows
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, // Columns
            {0, 4, 8}, {2, 4, 6}             // Diagonals
    };

    // UI elements
    private Button[] boardButtons;
    private TextView playerTurnTextView;
    private TextView gameStatusTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerTurnTextView = findViewById(R.id.player);
        gameStatusTextView = findViewById(R.id.status);
        Button resetButton = findViewById(R.id.reset_button);

        boardButtons = new Button[9];
        int[] buttonIds = {R.id.b1, R.id.b2, R.id.b3, R.id.b4, R.id.b5, R.id.b6, R.id.b7, R.id.b8, R.id.b9};
        for (int i = 0; i < boardButtons.length; i++) {
            boardButtons[i] = findViewById(buttonIds[i]);
            // You can also set a tag to easily identify the button's index
            boardButtons[i].setTag(i);
        }

        resetButton.setOnClickListener(v -> resetGame());

        resetGame();
    }

    /**
     * This single method handles clicks for all 9 tic-tac-toe tiles.
     * To make this work, add `android:onClick="onTileClick"` to each of the 9 Buttons
     * in your activity_main.xml layout file.
     */
    public void onTileClick(View view) {
        if (isGameOver) {
            return; // Don't allow moves if the game is over
        }

        int tileIndex = (int) view.getTag();

        if (boardState[tileIndex] == EMPTY) {
            boardState[tileIndex] = currentPlayer;
            updateBoardUI();

            int gameState = checkGameState();
            if (gameState != GAME_STATE_ONGOING) {
                handleGameEnd(gameState);
            } else {
                switchPlayer();
            }
        }
    }

    private void switchPlayer() {
        currentPlayer = (currentPlayer == PLAYER_ONE) ? PLAYER_TWO : PLAYER_ONE;
        updateGameStatus();
    }

    private int checkGameState() {
        // Check for a winner
        for (int[] combination : WINNING_COMBINATIONS) {
            if (boardState[combination[0]] != EMPTY &&
                    boardState[combination[0]] == boardState[combination[1]] &&
                    boardState[combination[0]] == boardState[combination[2]]) {
                return boardState[combination[0]]; // Returns PLAYER_ONE or PLAYER_TWO
            }
        }

        // Check for a draw (if no winner and board is full)
        boolean isDraw = true;
        for (int state : boardState) {
            if (state == EMPTY) {
                isDraw = false;
                break;
            }
        }
        if (isDraw) {
            return GAME_STATE_DRAW;
        }

        // If no winner and not a draw, the game is ongoing
        return GAME_STATE_ONGOING;
    }

    private void handleGameEnd(int gameState) {
        isGameOver = true;
        String message;
        switch (gameState) {
            case GAME_STATE_PLAYER_ONE_WINS:
                message = "PLAYER 1 WINS!";
                break;
            case GAME_STATE_PLAYER_TWO_WINS:
                message = "PLAYER 2 WINS!";
                break;
            case GAME_STATE_DRAW:
            default:
                message = "IT'S A DRAW!";
                break;
        }
        gameStatusTextView.setText(message);
        playerTurnTextView.setText(""); // Hide the player turn text
    }

    private void resetGame() {
        isGameOver = false;
        currentPlayer = PLAYER_ONE;
        boardState = new int[9]; // Automatically filled with 0 (EMPTY)
        updateBoardUI();
        updateGameStatus();
        gameStatusTextView.setText("");
    }

    private void updateBoardUI() {
        for (int i = 0; i < boardButtons.length; i++) {
            Button button = boardButtons[i];
            button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35f);
            switch (boardState[i]) {
                case PLAYER_ONE:
                    button.setText("O");
                    break;
                case PLAYER_TWO:
                    button.setText("X");
                    break;
                default:
                    button.setText("");
                    break;
            }
        }
    }

    private void updateGameStatus() {
        if (currentPlayer == PLAYER_ONE) {
            playerTurnTextView.setText("Player 1's Turn (O)");
        } else {
            playerTurnTextView.setText("Player 2's Turn (X)");
        }
    }
}