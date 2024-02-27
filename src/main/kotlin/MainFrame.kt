import java.awt.Color
import java.awt.Cursor
import java.awt.Font
import java.awt.Image
import java.awt.event.*
import javax.imageio.ImageIO
import javax.swing.*

/**
 * JFrame housing all components of the Connect 4 GUI. This frame contains all visual elements of the game.
 *
 * @author Ryan Johnson
 */
class MainFrame: JFrame() {
    private val WINDOW_WIDTH = 1000
    private val WINDOW_HEIGHT = 700

    val game = Connect4Game()

    val boardButtonsList = ArrayList<JButton>()
    val welcomeLabel = JLabel("Welcome to Connect 4!")
    val instructionLabel = JLabel("Click a column to place a piece.")
    val humanWinnerLabel = JLabel("<html>Congratulations! You've won!<br>Press Enter to play again.</html>")
    val computerWinnerLabel = JLabel("<html>Sorry, but the computer's beat you...<br>Press Enter to play again...</html>")
    val warningLabel = JLabel("That column is full. Choose a different column.")

    /**
     * Initializer for the Main Frame.
     */
    init {
        setTitle("Connect 4 GUI")
        setSize(WINDOW_WIDTH,WINDOW_HEIGHT)
        setDefaultCloseOperation(EXIT_ON_CLOSE)
        setLayout(null)
        createElements()
        setVisible(true)
    }

    /**
     * Renders all components of the frame, including several labels and buttons. Creates several transparent buttons representing the columns of the
     * game board, allowing the user to click on the column they want to put their piece in. These buttons each contain
     * mouse listeners that renders a red game piece into the chosen column before placing a yellow piece in the random
     * column chosen by the computer.
     */
    private fun createElements() {
        welcomeLabel.setFont(Font("Arial", Font.PLAIN, 30))
        welcomeLabel.setBounds(WINDOW_WIDTH / 3, 20, 400, 50)
        add(welcomeLabel)

        instructionLabel.setFont(Font("Arial", Font.PLAIN, 20))
        instructionLabel.setBounds(350, 90, 400, 50)
        add(instructionLabel)

        humanWinnerLabel.setFont(Font("Arial", Font.PLAIN, 20))
        humanWinnerLabel.setBounds(360, 90, 400, 50)

        computerWinnerLabel.setFont(Font("Arial", Font.PLAIN, 20))
        computerWinnerLabel.setBounds(360, 90, 400, 50)

        warningLabel.setFont(Font("Arial", Font.PLAIN, 20))
        warningLabel.setForeground(Color.red)
        warningLabel.setBounds(290, 90, 450, 70)

        val board = ImageIO.read(this::class.java.getResource("/board.png"))
        val resizedBoard = board.getScaledInstance(600, 500, Image.SCALE_DEFAULT)
        val boardLabel = JLabel(ImageIcon(resizedBoard))
        add(boardLabel)
        boardLabel.setBounds(190,150, 600, 500)

        for (i in 0..6) {
            val button = JButton()
            boardButtonsList.add(button)
            button.setBounds(83*i + 200,150, 83, 500)
            add(button)

            button.addMouseListener(object : MouseAdapter() {
                /**
                 * Cursor changes to a hand when hovering over a column.
                 */
                override fun mouseEntered(evt: MouseEvent) {
                    if (game.getCurrentPlayer() == 1) cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
                }

                /**
                 * Cursor changes back to the default when not hovering over a column.
                 */
                override fun mouseExited(evt: MouseEvent) {
                    cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)
                }

                /**
                 * When a column is clicked, a red game piece is rendered to the screen unless the column is already full.
                 * After the human piece is placed, a yellow piece is placed for the computer opponent. If either of
                 * these moves results in a win, the end game process is initiated.
                 */
                override fun mouseClicked(e: MouseEvent?) {
                    if (game.getCurrentPlayer() == 1) {
                        if (!game.placePiece(i)) {
                            // Warn the player that the column is full
                            remove(instructionLabel)
                            add(warningLabel)
                            repaint()
                        } else {
                            remove(warningLabel)  // The warning no longer needs to be shown if the piece was played successfully
                            add(instructionLabel)
                            repaint()

                            val humanCoords = game.getTopRow(i)  // Determine which row the piece landed in
                            renderHumanPiece(humanCoords)

                            // Only let the computer place a piece if the human didn't win on their turn
                            if (!game.getHasWinner()) {
                                game.nextPlayer()
                                computerMove()
                            }
                        }
                    }
                }
            })
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
        }
    }

    /**
     * Renders a red human player piece to the screen. If the human player wins, the end game sequence is initiated.
     *
     * @param humanCoords Pair object containing the index of the row and column the human piece should be shown in.
     */
    fun renderHumanPiece(humanCoords: Pair<Int, Int>) {
        // Render the piece onto the game board
        val redPieceImage = ImageIO.read(this::class.java.getResource("/red_piece.png"))
        val resizedRedPiece = redPieceImage.getScaledInstance(58, 56, Image.SCALE_DEFAULT)
        val redPieceLabel = JLabel(ImageIcon(resizedRedPiece))
        add(redPieceLabel)
        redPieceLabel.setBounds(82*(humanCoords.second) + 201,79*(humanCoords.first) + 160, 86, 86)

        if (game.hasWon(humanCoords.first, humanCoords.second)) {
            endGame()
        }
    }

    /**
     * The computer selects a random column to place its piece into before rendering it to the screen. If the computer
     * player wins, the end game sequence is initiated.
     */
    fun computerMove() {
        // Move the computer after the human has played
        val computerCoords = game.computerMove()
        val yellowPieceImage = ImageIO.read(this::class.java.getResource("/yellow_piece.png"))
        val resizedYellowPiece = yellowPieceImage.getScaledInstance(58, 56, Image.SCALE_DEFAULT)
        val yellowPieceLabel = JLabel(ImageIcon(resizedYellowPiece))
        add(yellowPieceLabel)
        yellowPieceLabel.setBounds(82*(computerCoords.second) + 201,79*(computerCoords.first) + 160, 86, 86)

        if (game.hasWon(computerCoords.first, computerCoords.second)) {
            endGame()
        }
        game.nextPlayer()
    }

    /**
     * Displays the winner of the game and removes the buttons to prevent further gameplay. The game is reset after the
     * player presses the "Enter" key.
     */
    fun endGame() {
        if (game.getCurrentPlayer() == 1) {
            remove(instructionLabel)
            add(humanWinnerLabel)
        } else {
            remove(instructionLabel)
            add(computerWinnerLabel)
        }
        repaint()
        for (button in boardButtonsList) {
            cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)
            remove(button)
        }
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "reset");
        getRootPane().actionMap.put("reset", object : AbstractAction() {
            override fun actionPerformed(e: ActionEvent) {
                resetGame()
            }
        })
    }

    /**
     * Resets the game board and visual aspects of the game.
     */
    fun resetGame() {
        game.resetGame()
        getContentPane().removeAll()
        createElements()
        repaint()
    }
}