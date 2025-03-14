
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.sound.sampled.*;
import javax.swing.*;

public class Canvas extends JFrame {
    private final static int size = 400; // Size of the chessboard
    private final ChessBoard chessBoard;
    private final JTextField fenField;
    private final JTextField depthField;
    private final JTextField timeField;
    private final JButton colorButton;
    private boolean randomNoise1 = false;
    private boolean playerColor = true;

    public Canvas() {
        setTitle("Chess-bot");
        setSize(size + 300, size);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Window decoration height (title bar)
        int windowDecorationHeight = 28;
        setMinimumSize(new Dimension(size + 302, size + windowDecorationHeight));

        // Create the chessboard
        chessBoard = new ChessBoard(size);
        chessBoard.setupGame("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", 3, 1000, false);

        // Create the input panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setPreferredSize(new Dimension(300, size));
        inputPanel.setMinimumSize(new Dimension(250, size));
        Color mediumCyanGray = new Color(210, 180, 140);
        inputPanel.setBackground(mediumCyanGray);

        JPanel separator = new JPanel();
        separator.setBackground(Color.BLACK);
        separator.setPreferredSize(new Dimension(2, size));

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(separator, BorderLayout.WEST); // Separator on the left
        rightPanel.add(inputPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;



        // FEN input field with horizontal scroll
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.gridwidth = 2; // Span across two columns
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel fenLabel = new JLabel("FEN:");
        inputPanel.add(fenLabel, gbc);

        gbc.gridy = 1;
        fenField = new JTextField("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", 35);
        fenField.setCaretPosition(0);
        fenField.setPreferredSize(new Dimension(250, 40)); // Taller text field
        JScrollPane fenScroll = new JScrollPane(fenField);
        fenScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        fenScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        fenScroll.setPreferredSize(new Dimension(250, 47)); // Extra space for scrollbar
        inputPanel.add(fenScroll, gbc);

        // Depth input field
        gbc.gridy = 2;
        JLabel depthLabel = new JLabel("Depth:");
        inputPanel.add(depthLabel, gbc);

        gbc.gridy = 3;
        depthField = new JTextField("3", 5);
        inputPanel.add(depthField, gbc);

        // Time input field
        gbc.gridy = 4;
        JLabel timeLabel = new JLabel("Time (ms):");
        inputPanel.add(timeLabel, gbc);

        gbc.gridy = 5;
        timeField = new JTextField("1000", 8);
        inputPanel.add(timeField, gbc);

        // Color button
        gbc.gridy = 6;
        gbc.gridwidth = 1; // Reset to 1 column width
        gbc.anchor = GridBagConstraints.CENTER; // Align center
        gbc.fill = GridBagConstraints.NONE;
        colorButton = new JButton("Selected: White"); // Initial text
        colorButton.setPreferredSize(new Dimension(120, 40));
        colorButton.addActionListener(e -> {
            try {
                SoundPlayer.sound("src/sounds/click.wav");
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                throw new RuntimeException(ex);
            }
            // Toggle the button text and board orientation
            if (colorButton.getText().equals("Selected: Black")) {
                colorButton.setText("Selected: White");
                playerColor = true;
            } else {
                colorButton.setText("Selected: Black");
                playerColor = false;
            }
        });
        inputPanel.add(colorButton, gbc);

        gbc.gridx = 1; // Second column
        JCheckBox checkbox = new JCheckBox("Random noise");
        checkbox.setPreferredSize(new Dimension(150, 40)); // Optional: Set size
        checkbox.addItemListener(e -> {
            try {
                SoundPlayer.sound("src/sounds/click.wav");
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                throw new RuntimeException(ex);
            }
            randomNoise1 = e.getStateChange() == ItemEvent.SELECTED;
        });
        inputPanel.add(checkbox, gbc);

        // Reset gridx for future components
        gbc.gridx = 0;

        // Start button
        gbc.gridy = 7;
        gbc.gridwidth = 0; // Reset to 1 column width
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.fill = GridBagConstraints.NONE;
        JButton startButton = new JButton("Start Game");
        startButton.setPreferredSize(new Dimension(150, 40)); // Same size as startButton

        startButton.addActionListener(e -> {
            try {
                SoundPlayer.sound("src/sounds/click.wav");
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                throw new RuntimeException(ex);
            }
            String fen = fenField.getText();
            int depth = Integer.parseInt(depthField.getText());
            int time = Integer.parseInt(timeField.getText());
            if (!chessBoard.isBoardFlipped && !playerColor) {
                chessBoard.toggleBoardOrientation();
            }
            if (chessBoard.isBoardFlipped && playerColor) {
                chessBoard.toggleBoardOrientation();
            }
            chessBoard.newSearch();
            chessBoard.setupGame(fen, depth, time, randomNoise1);
            chessBoard.repaint();
        });
        inputPanel.add(startButton, gbc);





          gbc.gridx = 0;

        // Start button
        gbc.gridy = 8;
        JButton helpButton = getJButton(mediumCyanGray);
        inputPanel.add(helpButton, gbc);
        // Add components
        add(chessBoard, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);

        pack();
        startButton.requestFocusInWindow();
        setVisible(true);
    }

    private JButton getJButton(Color mediumCyanGray) {
        JButton helpButton = new JButton("?");
        helpButton.setFont(new Font("Arial", Font.BOLD, 14));
        helpButton.setPreferredSize(new Dimension(30, 30));
        helpButton.setToolTipText("Click for help");
        helpButton.addActionListener(e -> {
            // Load the image from the specified path

            try {
                SoundPlayer.sound("src/sounds/click.wav");
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                throw new RuntimeException(ex);
            }
            ImageIcon imageIcon = new ImageIcon("src/resources_chess_pieces//FEN_example.png");

            // Check if the image was loaded successfully
            if (imageIcon.getImage().getWidth(null) == -1) {
                JOptionPane.showMessageDialog(
                        Canvas.this,
                        "Error: Image not found at 'src/resources_chess_pieces//FEN_example.png'",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // Create a panel to hold both text and image
            JPanel helpPanel = new JPanel();
            helpPanel.setBackground(mediumCyanGray);
            //UIManager.put("Panel.background", mediumCyanGray);
            UIManager.put("OptionPane.background", mediumCyanGray);
            UIManager.put("Panel.background", mediumCyanGray);
            helpPanel.setLayout(new BorderLayout(10, 10));


            JLabel helpText = new JLabel(
                    "<html>" +
                            "<b>1. FEN:</b> Enter the Forsyth-Edwards Notation.<br>" +
                            "&nbsp;&nbsp;&nbsp;<b>1.1 Black figures:</b> Pawn: p, Rook: r, Queen: q, King: k,<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Bishop: b, Knight: n<br>" +
                            "&nbsp;&nbsp;&nbsp;<b>1.2 White figures: </b> Pawn: P, Rook: R, Queen: Q, King: K,<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Bishop: B, Knight: N<br>" +
                            "&nbsp;&nbsp;&nbsp;<b>1.3 Blank space:</b>is represented by a number,indicating how many<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; consecutive blank squares are there in a row.<br>" +
                            "&nbsp;&nbsp;&nbsp;<b>1.4 picture FEN:</b> r1bk3r/p2pBpNp/n4n2/1p1NP2P/6P1/3P4/P1P1K3/q5b1<br>" +
                            "<b>2. Depth:</b> Search depth for the bot.<br>" +
                            "<b>3. Time (ms):</b> Max think time.<br>" +
                            "<b>4. Selected:white/black:</b> Choose your color.<br>" +
                            "<b>5. Random Noise:</b> Enable/disable evaluation noise." +
                            "</html>"
            );

// Set font and padding
            helpText.setFont(new Font("Arial", Font.PLAIN, 14));
            helpText.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            //helpText.setEditable(false);
            helpText.setBackground(mediumCyanGray);


            // Resize the image (optional)
            Image image = imageIcon.getImage();
            Image scaledImage = image.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
            JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
            imageLabel.setOpaque(true);
            imageLabel.setBackground(mediumCyanGray);
            // Add components to the panel
            helpPanel.add(helpText, BorderLayout.NORTH);
            helpPanel.add(imageLabel, BorderLayout.CENTER);

            // Show the dialog
            JOptionPane.showMessageDialog(
                    Canvas.this,
                    helpPanel,
                    "Help",
                    JOptionPane.PLAIN_MESSAGE
            );
        });
        return helpButton;
    }

}


