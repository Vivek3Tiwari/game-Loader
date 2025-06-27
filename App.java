import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class App {
    private static boolean darkMode = true;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame launcherFrame = new JFrame("Game Board");
            launcherFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            launcherFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize
            launcherFrame.setResizable(true);

            // Main panel setup
            JPanel panel = new JPanel(new BorderLayout(10, 10));
            panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
            applyTheme(panel);

            // Title
            JLabel title = new JLabel("Fun to Play Games", JLabel.CENTER);
            title.setFont(new Font("Arial", Font.BOLD, 32));
            title.setForeground(darkMode ? Color.WHITE : Color.BLACK);
            panel.add(title, BorderLayout.NORTH);

            // Grid buttons
            JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 25, 25));
            buttonPanel.setOpaque(false);

            JButton pacmanButton = createButton("Pac-Man", e -> launchPacMan());
            JButton ticTacToeButton = createButton("Tic-Tac-Toe", e -> launchTicTacToe());
            JButton brickBreakerButton = createButton("Brick Breaker", e -> launchBrickBreaker());
            JButton matchCardsButton = createButton("Match Cards", e -> launchMatchCards());

            buttonPanel.add(pacmanButton);
            buttonPanel.add(ticTacToeButton);
            buttonPanel.add(brickBreakerButton);
            buttonPanel.add(matchCardsButton);

            panel.add(buttonPanel, BorderLayout.CENTER);

            // Bottom panel with theme toggle
            JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            bottomPanel.setOpaque(false);
            JButton toggleTheme = new JButton("Toggle Theme");
            toggleTheme.addActionListener(e -> {
                darkMode = !darkMode;
                launcherFrame.dispose();
                main(null); // Relaunch
            });
            bottomPanel.add(toggleTheme);

            panel.add(bottomPanel, BorderLayout.SOUTH);
            launcherFrame.setContentPane(panel);
            launcherFrame.setVisible(true);
        });
    }

    private static JButton createButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 22));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        styleButton(button);

        button.addActionListener(action);
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(75, 161, 255));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                styleButton(button);
            }
        });

        return button;
    }

    private static void styleButton(JButton button) {
        if (darkMode) {
            button.setBackground(new Color(56, 121, 217));
            button.setForeground(Color.WHITE);
        } else {
            button.setBackground(new Color(200, 200, 255));
            button.setForeground(Color.BLACK);
        }
    }

    private static void applyTheme(JPanel panel) {
        if (darkMode) {
            panel.setBackground(new Color(40, 44, 52));
        } else {
            panel.setBackground(new Color(240, 240, 255));
        }
    }

    private static void launchPacMan() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Pac-Man");
            frame.setSize(600, 600);
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            PacMan pacmanGame = new PacMan();
            frame.add(pacmanGame);
            frame.pack();
            pacmanGame.requestFocus();
            frame.setVisible(true);
        });
    }

    private static void launchTicTacToe() {
        SwingUtilities.invokeLater(() -> new TicTacToe());
    }

    private static void launchBrickBreaker() {
        SwingUtilities.invokeLater(BrickBreaker::new);
    }

    private static void launchMatchCards() {
        SwingUtilities.invokeLater(() -> new MatchCards());
    }
}
