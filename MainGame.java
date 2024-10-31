import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

// MainGame that is able to use the functions of JPanel, which then implements
// ActionListener to handle timer events, and KeyListener for any keyboard events
public class MainGame extends JPanel implements ActionListener, KeyListener {
    class Space {
        // The x coordinate on a 2D plane
        int x;
        // The y coordinate on a 2D plane
        int y;
        // The width of the Space object on a 2D plane
        int width;
        // The height of the Space object on a 2D plane
        int height;
        // The image related to the Space object to be rendered in the game
        Image img;
        // Used for aliens -> determines if an alien is alive
        boolean alive = true;
        // Used for bullets -> if a bullet was used
        boolean used = false;

        // The constructor
        Space(int x, int y, int width, int height, Image img) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.img = img;
        }
    }

    // Constants for the board dimensions
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

    // Images for rocket and alien
    Image rocketImg, alienYellowImg, alienPinkImg, alienBlueImg;
    ArrayList<Image> alienImgArray;

    // Constants for rocket properties
    // The width of a rocket in tile size -> 32 * 2 = 64 pixels
    int rocketWidth = tileSize * 2;
    // The height of a rocket -> 32 pixels
    int rocketHeight = tileSize;
    // The initial x position of the rocket according to the board dimension
    int rocketX = tileSize * columns / 2 - tileSize;
    // The initial y position of the rocket according to the board dimension
    int rocketY = boardHeight - tileSize * 2;
    // The horizontal speed of a rocket when moving left and right -> 32 pixels
    int rocketVelocityX = tileSize;
    // Rocket variable is defined as type Space
    Space rocket;

    // Constants for alien properties
    // Be able to hold multiple alien, type Space in an array
    ArrayList<Space> alienArray;
    // The width of an alien -> 32 * 2 = 64 pixels
    int alienWidth = tileSize * 2;
    // The height of an alien -> 32 pixels
    int alienHeight = tileSize;
    // The starting amount of alien rows when the game is run
    int alienRows = 2;
    // The starting amount of alien columns when the game is run
    int alienColumns = 3;
    // Track the total number of aliens in the game, initialized to 0
    int alienCount = 0;
    // The horizontal velocity of an alien in which the alien moves
    int alienVelocityX = 1;

    // Constants for bullet properties
    // Be able to hold multiple bullet, type Space in an array
    ArrayList<Space> bulletArray;
    // The width of a bullet -> 32 / 8 = 4 pixels
    int bulletWidth = tileSize / 8;
    // The height of a bullet -> 32 / 2 = 16 pixels
    int bulletHeight = tileSize / 2;
    // The vertical velocity of a bullet, since it goes upwards, it is represented by -10
    int bulletVelocityY = -10;

    // Timer to create a gameTimer used to create a game loop
    Timer gameTimer;

    // The player's starting score starts off at 0
    int score = 0;
    // The game state determining if the game is over or not, initialized to false
    boolean gameOver = false;

    // Sets the maximum amount of bullets the user has to begin with
    int maxBullets = 30; // <- Up to you to change if you would like to :)
    // The amount of bullets left by the current player
    int bulletsLeft = maxBullets;

    // Sets the maximum amount of power-ups the user has to begin with
    int maxPowerUps = 10; // <- Up to you to change if you would like to :)
    // The amount of power-ups left by the current player
    int powerUpsLeft = maxPowerUps;

    // Lets the user know what level they are on
    int currentLevel = 1;

    // The source the contains all on the initialized variables in the game
    MainGame() {
        // Sets up the preferred size of the MainGame board -> 640 x 640 pixels
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        // The background color of the game
        setBackground(Color.black);
        // Allows the game to receive keyboard inputs by the user
        setFocusable(true);
        // Be able to respond to key events
        addKeyListener(this);

        // Grab the png images of rocket and alien from the src folder
        rocketImg = new ImageIcon(getClass().getResource("/rocket.png")).getImage();
        alienYellowImg = new ImageIcon(getClass().getResource("/yellow-alien.png")).getImage();
        alienPinkImg = new ImageIcon(getClass().getResource("/pink-alien.png")).getImage();
        alienBlueImg = new ImageIcon(getClass().getResource("/blue-alien.png")).getImage();

        // Initialize the array to store the 3 different alien images
        alienImgArray = new ArrayList<>();
        alienImgArray.add(alienYellowImg);
        alienImgArray.add(alienPinkImg);
        alienImgArray.add(alienBlueImg);

        // Initialize rocket and alien and bullet
        rocket = new Space(rocketX, rocketY, rocketWidth, rocketHeight, rocketImg);
        alienArray = new ArrayList<>();
        bulletArray = new ArrayList<>();

        // Sets up the game timer used in the game (60 fps)
        gameTimer = new Timer(1000 / 60, this);
        // Initializes the aliens in the game
        createAliens(0, 0);
        // Starts the timer and the game loop begins
        gameTimer.start();
    }

    // Paint method used whenever the game needs to be repainted
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    // Drawing method used to draw shapes, images, and text
    public void draw(Graphics g) {
        // Draw the rocket in the set points with the set dimensions
        g.drawImage(rocket.img, rocket.x, rocket.y, rocket.width, rocket.height, null);

        // Draws each aliens recursively in the alienArray
        drawAliens(g,0);

        // Draw each bullet recursively in the bulletArray
        drawBullets(g, 0);

        // Draws the score of the player
        g.setColor(Color.green);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        g.drawString("Score: " + score, 10, 35);

        // Draw the bullets that a player has left to shoot
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.drawString("Bullets Left: " + bulletsLeft, 12, 60);

        // Draw the power-ups that a player has left to use
        g.setColor(Color.red);
        g.drawString("Power-ups Left (press T): " + powerUpsLeft, 12, 85);

        // Draw the power-ups that a player has left to use
        g.setColor(Color.yellow);
        g.drawString("Level:  " + currentLevel, 12, 105);

        // Draw the gameOver message if the gameOver expression becomes true
        if (gameOver) {
            g.setColor(Color.red);
            g.setFont(new Font("Arial", Font.PLAIN, 48));
            g.drawString("Game Over :(", boardWidth / 2 - 150, boardHeight / 2);
        }

        // Determines if the player completed the current level, if there are no more aliens left
        // and gameOver is false
        if (alienCount == 0 && !gameOver) {
            score += alienColumns * alienRows * 100; // Gives the player bonus points for finishing a level
            bulletsLeft += 5; // Add 5 bullets when the level is completed
            currentLevel += 1; // Add 1 level to determine the current level
            alienColumns = Math.min(alienColumns + 1, columns / 2 - 2); // Increases the number of aliens without exceeding the limit
            alienRows = Math.min(alienRows + 1, rows - 6); // Increases the number of aliens without exceeding the limit
            alienArray.clear(); // Clears the existing aliens in the array before the next level
            bulletArray.clear(); // Clears the existing bullets in the array before the next level
            createAliens(0, 0); // Spawns a new set of aliens
        }
    }

    // Method that recursively draws the aliens from the alienArray
    public void drawAliens(Graphics g, int index) {
        // Base case
        if (index >= alienArray.size()) return;

        // Gets the current alien from alienArray at the set index
        Space alien = alienArray.get(index);
        // Checks if the alien is alive, and will only draw alive aliens
        if (alien.alive) {
            g.drawImage(alien.img, alien.x, alien.y, alien.width, alien.height, null);
        }
        // Recursively draw alive aliens in alienArray adding 1 to the index (position)
        drawAliens(g, index + 1);
    }

    // Method that recursively draws the bullet and power-up
    public void drawBullets(Graphics g, int index) {
        // Base case
        if (index >= bulletArray.size()) return;

        // Gets the current bullet from bulletArray at the set index
        Space bullet = bulletArray.get(index);
        // Determines if it is a power-up shot, if it is, draw the long rectangle shot
        if (bullet.height == boardHeight) {
            g.setColor(Color.red);
            g.fillRect(bullet.x, 0, bullet.width, rocket.y);
            // Determines if the bullet is not a power-up shot, then draw the regular bullet shot
        } else if (!bullet.used) {
            g.setColor(Color.white);
            g.fillRect(bullet.x, bullet.y, bullet.width, bullet.height);
        }
        // Recursively draw bullets in bulletArray adding 1 to the index (position)
        drawBullets(g, index + 1);
    }

    // Recursively moves aliens, bullets starting from index 0
    public void move() {
        moveAliens(0);
        moveBullets(0);

        // Recursively removes bullets that are used or not on the screen
        removeBullets(0);

        // Checks if the gameOver condition has been after the move method runs
        checkGameOver();
    }

    // Method that recursively moves aliens across the board
    public void moveAliens(int index) {
        // Base case
        if (index >= alienArray.size()) return;

        Space alien = alienArray.get(index);
        // Moves the alien horizontally
        if (alien.alive) {
            alien.x += alienVelocityX;

            // Changes the direction of the alien if it hits the edge of the board
            // and then moves all aliens down 1
            if (alien.x + alien.width >= boardWidth || alien.x <= 0) {
                alienVelocityX *= -1;
                alien.x += alienVelocityX * 2;
                shiftAliensDown(0);
            }

            // Determines if any alien has hit the bottom of the board aka
            // below the level of the rocket
            if (alien.y + alien.height >= rocket.y) {
                gameOver = true;
            }
        }
        // Moves the next alien in alienArray recursively adding 1 to the index (position)
        moveAliens(index + 1);
    }

    // Method that moves all aliens down by 1 row if it hits the edge of the board
    public void shiftAliensDown(int index) {
        // Base case
        if (index >= alienArray.size()) return;

        // Increases the y position by the height of the alien
        Space alien = alienArray.get(index);
        alien.y += alienHeight;

        // Moves the next alien in alienArray recursively adding 1 to the index (position)
        shiftAliensDown(index + 1);
    }

    // Method that moves all bullets upward and checks if they hit an alien
    public void moveBullets(int index) {
        // Base case
        if (index >= bulletArray.size()) return;

        // Increases the y position by the bulletVelocityY, -10 meaning it will go upwards
        Space bullet = bulletArray.get(index);
        bullet.y += bulletVelocityY;

        // Checks if the bullet collided with an alien
        bulletHitAlien(bullet, 0);

        // Recursively calls a bullet in bulletArray and adds 1 to the index (position)
        moveBullets(index + 1);
    }

    // Method that checks if a bullet collided with an alien
    public void bulletHitAlien(Space bullet, int alienIndex) {
        // Base case
        if (alienIndex >= alienArray.size()) return;

        Space alien = alienArray.get(alienIndex);
        // Checks if the coordinates of a bullet and an alien are the same, meaning they collided
        if (!bullet.used && alien.alive && checkIfHit(bullet, alien)) {
            bullet.used = true; // A bullet has been used
            alien.alive = false; // The alien is no longer alive
            alienCount--; // The alien count is decreased
            score += 100; // Add 100 points to the score :)
        }
        // Recursively checks if a bullet hits an alien in alienArray adding 1 to the index (position)
        bulletHitAlien(bullet, alienIndex + 1);
    }

    // Method for shooting a bullet when a player has shot
    public void shoot() {
        if (bulletsLeft > 0) {
            Space bullet = new Space(rocket.x + rocket.width / 2 - bulletWidth / 2, rocket.y, bulletWidth, bulletHeight, null);
            bulletArray.add(bullet); // The bullet is added to bulletArray
            bulletsLeft --; // The number of bullets a player has left is decremented
        }
    }

    // Method that removes the bullets in bulletArray, either been used or
    // gone off the top of the board
    public void removeBullets(int index) {
        // Base case
        if (index >= bulletArray.size()) return;

        // Checks if the bullet has been used, or off the board
        if (bulletArray.get(index).used || bulletArray.get(index).y < 0) {
            bulletArray.remove(index); // If true, removes the bullet from bulletArray
            removeBullets(index); // Checks the current index again to make sure the bullet was removed
        } else {
            // Moves onto the next bullet in bulletArray by adding 1 to the index (position) and checking
            // that bullet if the previous one is not used or off the board
            removeBullets(index + 1);
        }
    }

    // Method that creates aliens for the new level and placing them in the correct
    // column and row on the board
    public void createAliens(int row, int column) {
        // Base case
        if (row >= alienRows) return;

        // If the current column exceeds alienColumns, then move to the new row
        if (column >= alienColumns) {
            createAliens(row + 1, 0);
            return;
        }

        // Randomly select an alien color png in alienArray when created in the next level
        Random random = new Random();
        int randomImgIndex = random.nextInt(alienImgArray.size());
        // The new alien image is created in the Space object
        Space alien = new Space(
                tileSize + column * alienWidth,
                tileSize + row * alienHeight,
                alienWidth, alienHeight,
                alienImgArray.get(randomImgIndex));
        // Added onto the alienArray
        alienArray.add(alien);
        // The alienCount is incremented
        alienCount++;

        // Recursively do this for the next alien in the row in alienArray by adding 1
        // to the index (position)
        createAliens(row, column + 1);
    }

    // Checks if a bullet has hit an alien
    public boolean checkIfHit(Space bullet, Space alien) {
        // Two rectangles representing their "hit-box" for an alien and bullet
        Rectangle bulletRect = new Rectangle(bullet.x, bullet.y, bullet.width, bullet.height);
        Rectangle alienRect = new Rectangle(alien.x, alien.y, alien.width, alien.height);
        return bulletRect.intersects(alienRect);
    }

    // Checks if the last bullet fired is off the screen
    public boolean lastBulletGone() {
        // Base case
        if (bulletArray.isEmpty()) return true; // No bullets left in bulletArray

        // Gets the last bullet in bulletArray to know what to compare it to
        Space lastBullet = bulletArray.get(bulletArray.size() - 1);
        return lastBullet.used || lastBullet.y < 0;
    }

    // Method that determines when the game is over
    public void checkGameOver() {
        // Check if there are no bullets or power-ups left, and the last bullet is off the board
        if (bulletsLeft == 0 && powerUpsLeft == 0 && lastBulletGone()) {
            gameOver = true; // No more bullets or power-ups left
        }

        // Recursively check if any of the aliens have hit the bottom of the board
        // and reached the same level of the rocket, starting from the first alien in
        // alienArray
        checkAlienPos(0);
    }

    // Method that checks the position of an alien
    public void checkAlienPos(int index) {
        // Base case
        if (index >= alienArray.size()) return; // We have checked all the aliens in alienArray

        // Gets the current alien in alienArray
        Space alien = alienArray.get(index);

        // Checks if the alien is alive and has reached the bottom of the board
        if (alien.alive && alien.y + alien.height >= rocket.y) {
            gameOver = true; // gameOver if any alien has reached the bottom of the board
            return;
        }

        // Recursively call this method to check the next alien in alienArray by adding 1
        // to the index (position)
        checkAlienPos(index + 1);
    }


    // The game loop that is performed every frame
    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();

        // Stops the game making sure the game has a termination
        if (gameOver) {
            gameTimer.stop();
        }
    }

    // Methods enforced by KeyListener
    // Not using it
    @Override
    public void keyTyped(KeyEvent e) {
    }

    // Not using it
    @Override
    public void keyPressed(KeyEvent e) {
    }

    // Method that handles keys that are pressed one at a time, so that
    // the player is unable to spam bullets :)
    @Override
    public void keyReleased(KeyEvent e) {
        // Left arrow key allows the rocket to move 32 pixels to the left
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            rocket.x = Math.max(rocket.x - rocketVelocityX, 0);
            // Right arrow key allows the rocket to move 32 pixels to the right
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            rocket.x = Math.min(rocket.x + rocketVelocityX, boardWidth - rocket.width);
            // Space key allows the rocket to shoot a bullet
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            shoot();
            // T key allows the rocket to shoot a power-up, a huge ray eliminating all aliens in that column
        } else if (e.getKeyCode() == KeyEvent.VK_T) {
            shootRay();
        }
    }

    // Method that allows the player to use the power-up special :)
    public void shootRay() {
        if (powerUpsLeft > 0) { // Checks if the player is able to use a power-up
            powerUpsLeft--; // Decrements the amount of power-ups a player has if used

            // The power-up will be from the top of the rocket to the top of the board
            int powerupX = rocket.x + rocket.width / 2 - bulletWidth / 2;
            int powerupY = rocket.y;

            Space powerup = new Space(powerupX, powerupY, bulletWidth, boardHeight, null);

            // Mark the power-up as a bullet but does not affect the amount of bullets a player has
            bulletArray.add(powerup);

            // Eliminates all aliens in the same column of the power-up
            killAliensInColumn(powerupX, 0);
        }
    }

    // Kill all aliens in the column where the power-up was shot at
    public void killAliensInColumn(int rayX, int index) {
        // Base case
        if (index >= alienArray.size()) return;

        Space alien = alienArray.get(index);
        // Checks if the alien is alive where the ray (power-up) was shot at and is hit
        if (alien.alive && alien.x <= rayX && alien.x + alien.width >= rayX) {
            alien.alive = false; // The alien has been killed
            alienCount--; // Decrements the alienCount
            score += 100; // Adds 100 to the total score, same features as a bullet and score
        }

        // Recursively call the method for the next alien in alienArray by adding 1 to the index (position)
        killAliensInColumn(rayX, index + 1);
    }
}
