import javax.swing.*;

/* Instructions:
1. The user is allowed to move left and right using the left and right arrow keys ONLY
2. The user is allowed to shoot the aliens using the space bar ONLY (30 bullets) <- can change
3. The user is allowed to use their power-up by pressing T on their keyboard which eliminates the column of aliens (10 power-ups)
4. The game ends when the user runs out of power-ups and bullets OR when the aliens reach the last row
5. Have fun! :)
*/

// Runs the aliensAttack game for the user
public class Run {
    public static void main(String[] args) throws Exception {
        // Each tile is 32 pixels (length and width)
        int tileSize = 32;
        // There are 20 rows in the game
        int rows = 20;
        // There are 20 columns in the game
        int columns = 20;
        // The board width is 20 * 32 = 640 pixels
        int boardWidth = tileSize * columns;
        // The board height is 20 * 32 = 640 pixels
        int boardHeight = tileSize * rows;

        // The name of the frame when the method is run
        JFrame frame = new JFrame("Aliens Attack");
        // Set the frame to be visible to the user
        frame.setVisible(true);
        // The size of the frame -> 640 x 640 pixels
        frame.setSize(boardWidth, boardHeight);
        // Centers the window in the middle of the screen
        frame.setLocationRelativeTo(null);
        // Does not allow the user to resize the game
        frame.setResizable(false);
        // Makes sure when the user closes the program, the application terminates
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Creates the new instances of MainGame
        MainGame aliensAttack = new MainGame();
        frame.add(aliensAttack);
        frame.pack();
        aliensAttack.requestFocus();
        frame.setVisible(true);
    }
}
