import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;


public class BrickBreaker extends JFrame {
    public static void main(String[] args) {
        new BrickBreaker();
    }

    public BrickBreaker() {
        setTitle("Brick Breaker Game - Java Swing");
        setBounds(100, 100, 700, 600);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        GamePanel panel = new GamePanel();
        add(panel);
        setVisible(true);
    }
}

class GamePanel extends JPanel implements KeyListener, ActionListener {
    private boolean play = false;
    private int score = 0;

    private int totalBricks = 21;

    private Timer timer;
    private int delay = 8;
    private int playerX = 310;
    private int ballPosX = 120;
    private int ballPosY = 350;
    private int ballDirX = -1;
    private int ballDirY = -2;

    private MapGenerator map;

    public GamePanel() {
        map = new MapGenerator(3, 7);
        setFocusable(true);
        addKeyListener(this);
        setBackground(Color.BLACK);
        timer = new Timer(delay, this);
        timer.start();
    }

    public void paint(Graphics g) {
        // background
        g.setColor(Color.BLACK);
        g.fillRect(1, 1, 700, 600);

        // drawing map
        map.draw((Graphics2D) g);

        // borders
        g.setColor(Color.YELLOW);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(691, 0, 3, 592);

        // scores
        g.setColor(Color.WHITE);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString("" + score, 590, 30);

        // the paddle
        g.setColor(Color.GREEN);
        g.fillRect(playerX, 550, 100, 8);

        // the ball
        g.setColor(Color.YELLOW);
        g.fillOval(ballPosX, ballPosY, 20, 20);

        // win
        if (totalBricks <= 0) {
            play = false;
            ballDirX = 0;
            ballDirY = 0;
            g.setColor(Color.GREEN);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("You Won!", 260, 300);

            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter to Restart", 230, 350);
        }

        // lose
        if (ballPosY > 570) {
            play = false;
            ballDirX = 0;
            ballDirY = 0;
            g.setColor(Color.RED);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Game Over, Score: " + score, 190, 300);

            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter to Restart", 230, 350);
        }

        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();

        if (play) {
            // check for paddle collision
            if (new Rectangle(ballPosX, ballPosY, 20, 20)
                    .intersects(new Rectangle(playerX, 550, 100, 8))) {
                ballDirY = -ballDirY;
            }

            // check for brick collision
            A: for (int i = 0; i < map.map.length; i++) {
                for (int j = 0; j < map.map[0].length; j++) {
                    if (map.map[i][j] > 0) {
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        Rectangle brickRect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballPosX, ballPosY, 20, 20);

                        if (ballRect.intersects(brickRect)) {
                            map.setBrickValue(0, i, j);
                            totalBricks--;
                            score += 5;

                            // bounce off
                            if (ballPosX + 19 <= brickRect.x || ballPosX + 1 >= brickRect.x + brickRect.width)
                                ballDirX = -ballDirX;
                            else
                                ballDirY = -ballDirY;

                            break A;
                        }
                    }
                }
            }

            ballPosX += ballDirX;
            ballPosY += ballDirY;

            // bounce off walls
            if (ballPosX < 0) ballDirX = -ballDirX;
            if (ballPosY < 0) ballDirY = -ballDirY;
            if (ballPosX > 670) ballDirX = -ballDirX;
        }

        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (playerX >= 600) playerX = 600;
            else moveRight();
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (playerX <= 10) playerX = 10;
            else moveLeft();
        }

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!play) {
                play = true;
                ballPosX = 120;
                ballPosY = 350;
                ballDirX = -1;
                ballDirY = -2;
                playerX = 310;
                score = 0;
                totalBricks = 21;
                map = new MapGenerator(3, 7);
                repaint();
            }
        }
    }

    public void moveRight() {
        play = true;
        playerX += 20;
    }

    public void moveLeft() {
        play = true;
        playerX -= 20;
    }
}

class MapGenerator {
    public int[][] map;
    public int brickWidth;
    public int brickHeight;

    public MapGenerator(int row, int col) {
        map = new int[row][col];
        for (int[] rowData : map)
            Arrays.fill(rowData, 1);

        brickWidth = 540 / col;
        brickHeight = 150 / row;
    }

    public void draw(Graphics2D g) {
        for (int i = 0; i < map.length; i++)
            for (int j = 0; j < map[0].length; j++)
                if (map[i][j] > 0) {
                    g.setColor(Color.WHITE);
                    g.fillRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);

                    g.setColor(Color.BLACK);
                    g.setStroke(new BasicStroke(3));
                    g.drawRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);
                }
    }

    public void setBrickValue(int value, int row, int col) {
        map[row][col] = value;
    }
}