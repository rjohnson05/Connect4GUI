import kotlin.random.Random

/**
 * Business logic for a Connect 4 Game. This provides all logic functionality for a GUI version of a Connect 4 game.
 *
 * @author Ryan Johnson
 * @property HUMAN_PLAYER ID number for the human player on the game board, used for identifying pieces on the game board
 * and keeping track of which player is currently playing
 * @property COMPUTER_PLAYER ID number for the computer player on the game board, used for identifying pieces on the game board
 *  * and keeping track of which player is currently playing
 * @property currentPlayer identifies which player is currently playing (1 for the human player, 2 for the computer player)
 * @property board 5x7 array storing the state of the game board
 */
class Connect4Game(private val HUMAN_PLAYER: Int = 1,
                   private val COMPUTER_PLAYER: Int = 2,
                   private var currentPlayer: Int = 1,
                   private var hasWinner: Boolean = false,
                   private var board: Array<Array<Int>> = Array(6) { Array<Int>(7) { 0 } }) {

    /**
     * Returns the index of the topmost row in the specified column which contains a played piece.
     *
     * @return coordinates of placed piece
     */
    fun getTopRow(col: Int): Pair<Int, Int> {
        var colList = ArrayList<Int>()
        for (row in board) colList.add(row[col])
        return Pair(colList.lastIndexOf(0) + 1, col)
    }

    /**
     * Randomly chooses a column to play in for the computer and places the piece on the game board.
     *
     * @return coordinates of newly placed piece
     */
    fun computerMove(): Pair<Int, Int> {
        // Sleeps to simulate computer thinking
        var validCol = false
        var chosenCol: Int = -1
        while (!validCol) {
            chosenCol = Random.nextInt(0,6)
            if (placePiece(chosenCol)) {
                validCol = true
            }
        }

        var col = ArrayList<Int>()
        for (row in board) col.add(row[chosenCol])
        return Pair(col.lastIndexOf(0) + 1, chosenCol)
    }

    /**
     * Places a piece into the designated column for the current player. Moves the piece down the column until it reaches
     * a previously played piece.
     *
     * @param col integer representing the index of the column to place the piece into
     * @return true if the piece is correctly placed; false if the column input is invalid or the column is already full
     */
    fun placePiece(col: Int): Boolean {
        if (col !in 0..6) {
            println("Invalid column number input to playPiece")
            return false
        }
        if  (board[0][col] != 0) {
            println("That column is already full. Try again.")
            return false
        }
        for (i in (0..5).reversed()) {
            if (board[i][col] == 0) {
                board[i][col] = currentPlayer
                return true
            }
        }
        return false
    }

    /**
     * Displays the game board to the console.
     */
    fun displayBoard() {
        println("----------------------------")
        for (col in board) println(col.contentDeepToString())
    }

    /**
     * Determines if either player has won the game.
     *
     * @param chosenRow integer representing the row the last piece was placed in
     * @param chosenCol integer representing the column the last piece was placed in
     * @return true if either player has won; false otherwise
     */
    fun hasWon(chosenRow: Int, chosenCol: Int): Boolean {
        hasWinner = checkRowWin(chosenRow) ||
                checkColWin(chosenRow, chosenCol) ||
                checkDownLeftDiagWin(chosenRow, chosenCol) ||
                checkDownRightDiagWin(chosenRow, chosenCol) ||
                checkUpLeftDiagWin(chosenRow, chosenCol) ||
                checkUpRightDiagWin(chosenRow, chosenCol)
        return hasWinner
    }

    /**
     * Determines if there is a win (4 horizontal pieces in a row) in the row that the last piece was placed in
     *
     * @param chosenRow integer representing the row the last piece was placed in
     * @return true if there is a win in the row; false otherwise
     */
    fun checkRowWin(chosenRow: Int): Boolean {
        for (i in 0..3) {
            if (currentPlayer == board[chosenRow][i] &&
                currentPlayer == board[chosenRow][i + 1] &&
                currentPlayer == board[chosenRow][i + 2] &&
                currentPlayer == board[chosenRow][i + 3]
            ) {
                println("row win")
                return true
            }
        }
        return false
    }

    /**
     * Determines if there is a win (4 vertical in a row) in the column that the last piece was placed in
     *
     * @param chosenRow integer representing the row the last piece was placed in
     * @param chosenCol integer representing the column the last piece was placed in
     * @return true if there is a win in the column; false otherwise
     */
    fun checkColWin(chosenRow: Int, chosenCol: Int): Boolean {
        if (chosenRow <= 2) {
            if (currentPlayer == board[chosenRow][chosenCol] &&
                currentPlayer == board[chosenRow + 1][chosenCol] &&
                currentPlayer == board[chosenRow + 2][chosenCol] &&
                currentPlayer == board[chosenRow + 3][chosenCol]
            ) {
                println("col win")
                return true
            }
        }
        return false
    }

    /**
     * Determines if there is a win (4 diagonal in a row) in the diagonal below and to the left from the last piece
     *
     * @param chosenRow integer representing the row the last piece was placed in
     * @param chosenCol integer representing the column the last piece was placed in
     * @return true if there is a win in the diagonal; false otherwise
     */
    fun checkDownLeftDiagWin(chosenRow: Int, chosenCol: Int): Boolean {
        if (chosenRow <= 2 && chosenCol >= 3) {
            for (i in 0..1) {
                for (j in 3..6) {
                    if (currentPlayer == board[i][j] &&
                        currentPlayer == board[i + 1][j - 1] &&
                        currentPlayer == board[i + 2][j - 2] &&
                        currentPlayer == board[i + 3][j - 3]
                    ) {
                        println("downleft win")
                        return true
                    }
                }
            }
        }
        return false
    }

    /**
     * Determines if there is a win (4 diagonal in a row) in the diagonal above and to the left from the last piece
     *
     * @param chosenRow integer representing the row the last piece was placed in
     * @param chosenCol integer representing the column the last piece was placed in
     * @return true if there is a win in the diagonal; false otherwise
     */
    fun checkUpLeftDiagWin(chosenRow: Int, chosenCol: Int): Boolean {
        if (chosenRow >= 3 && chosenCol >= 3) {
            for (i in 3..4) {
                for (j in 3..6) {
                    if (currentPlayer == board[i][j] &&
                        currentPlayer == board[i - 1][j - 1] &&
                        currentPlayer == board[i - 2][j - 2] &&
                        currentPlayer == board[i - 3][j - 3]
                    ) {
                        println("upleft win")
                        return true
                    }
                }
            }
        }
        return false
    }

    /**
     * Determines if there is a win (4 diagonal in a row) in the diagonal below and to the right from the last piece
     *
     * @param chosenRow integer representing the row the last piece was placed in
     * @param chosenCol integer representing the column the last piece was placed in
     * @return true if there is a win in the diagonal; false otherwise
     */
    fun checkDownRightDiagWin(chosenRow: Int, chosenCol: Int): Boolean {
        if (chosenRow <= 2 && chosenCol <= 3) {
            for (i in 0..1) {
                for (j in 0..3) {
                    if (currentPlayer == board[i][j] &&
                        currentPlayer == board[i + 1][j + 1] &&
                        currentPlayer == board[i + 2][j + 2] &&
                        currentPlayer == board[i + 3][j + 3]
                        ) {
                        println("downright win")
                        return true
                    }
                }
            }
        }
        return false
    }

    /**
     * Determines if there is a win (4 diagonal in a row) in the diagonal above and to the right from the last piece
     *
     * @param chosenRow integer representing the row the last piece was placed in
     * @param chosenCol integer representing the column the last piece was placed in
     * @return true if there is a win in the diagonal; false otherwise
     */
    fun checkUpRightDiagWin(chosenRow: Int, chosenCol: Int): Boolean {
        if (chosenRow >= 3 && chosenCol <= 3) {
            for (i in 3..4) {
                for (j in 0..3) {
                    if (currentPlayer == board[i][j] &&
                        currentPlayer == board[i - 1][j + 1] &&
                        currentPlayer == board[i - 2][j + 2] &&
                        currentPlayer == board[i - 3][j + 3]
                    ) {
                        println("upright win")
                        return true
                    }
                }
            }
        }
        return false
    }

    /**
     * Resets the game board and current player to the human player.
     */
    fun resetGame() {
        board = Array(6) { Array<Int>(7) { 0 } }
        currentPlayer = 1
    }

    /**
     * Changes play to the opposing player.
     */
    fun nextPlayer() {
        currentPlayer = if (currentPlayer == 1) 2
        else 1
    }

    /**
     * Returns the current player.
     *
     * @return Int representing the number of the current player (1 for the human player; for the computer player)
     */
    fun getCurrentPlayer(): Int {
        return currentPlayer
    }

    /**
     * Returns whether either player has won the game. To win, a player must have placed four pieces in a row.
     *
     * @return Boolean true if a player has won the game; false otherwise
     */
    fun getHasWinner(): Boolean {
        return hasWinner
    }
}