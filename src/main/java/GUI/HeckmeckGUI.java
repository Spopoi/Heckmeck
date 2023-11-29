package GUI;

import Heckmeck.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.awt.GridBagConstraints.CENTER;
import static java.awt.GridBagConstraints.NORTHWEST;


public class HeckmeckGUI {

    private static JFrame frame;
    private static JPanel currentPanel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(HeckmeckGUI::initGUI);
    }

    private static void initGUI() {

        frame = new JFrame("HECKMECK");
        frame.setSize(1200, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        switchToMenuPanel();
    }

    private static void switchToRulesPanel() {
        JPanel rulesPanel = new JPanel(new GridBagLayout());

        JPanel imagesPanel = new JPanel(new GridLayout(2, 1));
        JScrollPane scrollPane = new JScrollPane(imagesPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setUnitIncrement(15);

        ImageIcon rulesImage1 = new ImageIcon("src/main/java/GUI/Icons/Rules1.jpg");
        JLabel rulesLabel1 = new JLabel(rulesImage1);
        imagesPanel.add(rulesLabel1);

        ImageIcon rulesImage2 = new ImageIcon("src/main/java/GUI/Icons/Rules2.jpg");
        JLabel rulesLabel2 = new JLabel(rulesImage2);
        imagesPanel.add(rulesLabel2);

        JButton backToMenuButton = new JButton("Menu");
        backToMenuButton.addActionListener(e -> switchToMenuPanel());
        backToMenuButton.setPreferredSize(new Dimension(70,70));
        backToMenuButton.setBackground(Color.ORANGE);

        rulesPanel.add(backToMenuButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        rulesPanel.add(scrollPane, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        
        frame.getContentPane().removeAll();
        frame.setContentPane(rulesPanel);
        frame.revalidate();
        //frame.repaint();
    }

    private static void switchToMenuPanel() {

        ImagePanel menuPanel = new ImagePanel("src/main/java/GUI/Icons/heckmeckBackground.jpg");
        menuPanel.setLayout(new GridBagLayout());

        JPanel buttonsPanel = new JPanel(new GridLayout(3, 1, 10, 20));
        buttonsPanel.setPreferredSize(new Dimension(300,200));
        buttonsPanel.setOpaque(false);

        JButton playButton = new JButton("Play");
        playButton.setFocusPainted(false);
        playButton.addActionListener(e -> switchToGamePanel());
        playButton.setBackground(Color.orange);
        buttonsPanel.add(playButton);

        JButton rulesButton = new JButton("Rules");

        rulesButton.addActionListener(e -> switchToRulesPanel());
        rulesButton.setBackground(Color.orange);
        buttonsPanel.add(rulesButton);

        JButton settingsButton = new JButton("Settings");

        settingsButton.addActionListener(e -> switchToSettings());
        settingsButton.setBackground(Color.orange);
        buttonsPanel.add(settingsButton);

        menuPanel.add(buttonsPanel, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, CENTER, GridBagConstraints.BOTH, new Insets(180, 0, 0, 0), 0, 0));

        frame.getContentPane().removeAll();
        frame.setContentPane(menuPanel);
        frame.setVisible(true);
    }

    private static void switchToSettings() {
        //TODO: cosa metterci? resize del frame...
        JPanel settingsPanel = createSettingsPanel();

        frame.getContentPane().removeAll();
        frame.setContentPane(settingsPanel);
        frame.revalidate();
    }

    private static final Color ORANGE_COLOR = new Color(255, 201, 150);
    private static final Color BUTTON_COLOR = new Color(255, 174, 103);

    private static JPanel createSettingsPanel() {
        JPanel settingsPanel = new JPanel(new BorderLayout());
        settingsPanel.setPreferredSize(frame.getSize());

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(new EmptyBorder(0, 150, 50, 150));

        JButton backButton = new JButton("Torna al Menu");
        backButton.addActionListener(e -> switchToMenuPanel());
        backButton.setBackground(BUTTON_COLOR);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(backButton);

        settingsPanel.add(buttonPanel, BorderLayout.NORTH);

        JLabel titleLabel = new JLabel("Impostazioni");
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        int titleSpacing = 30;
        titleLabel.setBorder(new EmptyBorder(20, 0, titleSpacing, 0));

        centerPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 50));
        inputPanel.setBorder(new EmptyBorder(70, 50, 100, 50));

        JLabel heightLabel = new JLabel("Altezza frame:");
        heightLabel.setFont(new Font("Arial", Font.BOLD, 20));
        JTextField heightTextField = new JTextField();
        JLabel widthLabel = new JLabel("Larghezza frame:");
        widthLabel.setFont(new Font("Arial", Font.BOLD, 20));
        JTextField widthTextField = new JTextField();

        inputPanel.add(heightLabel);
        inputPanel.add(heightTextField);
        inputPanel.add(widthLabel);
        inputPanel.add(widthTextField);

        inputPanel.setBackground(ORANGE_COLOR);
        centerPanel.add(inputPanel, BorderLayout.CENTER);

        JButton applyButton = new JButton("Applica");
        applyButton.addActionListener(e -> applySettings(heightTextField, widthTextField));

        applyButton.setBackground(BUTTON_COLOR);
        centerPanel.add(applyButton, BorderLayout.SOUTH);
        centerPanel.setBackground(ORANGE_COLOR);

        settingsPanel.add(centerPanel, BorderLayout.CENTER);

        settingsPanel.setBackground(ORANGE_COLOR);

        return settingsPanel;
    }

    private static void applySettings(JTextField heightTextField, JTextField widthTextField) {
        try {
            int newHeight = Integer.parseInt(heightTextField.getText());
            int newWidth = Integer.parseInt(widthTextField.getText());
            frame.setSize(newWidth, newHeight);
            frame.revalidate();
            frame.repaint();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Inserisci numeri validi per altezza e larghezza.");
        }
    }


    private static void switchToGamePanel() {

        ImagePanel imagePanel = new ImagePanel("src/main/java/GUI/Icons/table.jpg");

        frame.getContentPane().removeAll();
        frame.setContentPane(imagePanel);
        frame.getContentPane().setLayout(new BorderLayout(50, 30));

        GUIIOHandler io = new GUIIOHandler(frame);
        Game game = new Game(io);

        final var worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                game.init();
                game.play();
                return null;
            }
        };
        worker.execute();

        frame.revalidate();
        frame.repaint();
    }

    public static class ImagePanel extends JPanel {
        //TODO: Move it?
        private Image backgroundImage;

        public ImagePanel(String imagePath) {
            this.backgroundImage = new ImageIcon(imagePath).getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
