package GUI;

import Heckmeck.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.awt.GridBagConstraints.CENTER;


public class HeckmeckGUI {

    private static JFrame frame;
    private static JPanel currentPanel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                initGUI();
            }
        });
    }

    private static void initGUI() {

        frame = new JFrame("HECKMECK");
        frame.setSize(1200, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        switchToMenuPanel();
    }

    private static void switchToRulesPanel() {
        JPanel rulesPanel = new JPanel(new BorderLayout());

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


        rulesPanel.add(backToMenuButton, BorderLayout.NORTH);
        rulesPanel.add(scrollPane, BorderLayout.CENTER);

        frame.getContentPane().removeAll();
        frame.setContentPane(rulesPanel);
        frame.revalidate();
        //frame.repaint();
    }

    private static void switchToMenuPanel() {

        //TODO: extract this method used to put in background a jpg image
        JPanel menuPanel = new JPanel() {
            private Image backgroundImage = new ImageIcon("src/main/java/GUI/Icons/heckmeckBackground.jpg").getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        menuPanel.setLayout(new GridBagLayout());

        JPanel buttonsPanel = new JPanel(new GridLayout(3, 1, 10, 20));
        buttonsPanel.setPreferredSize(new Dimension(300,200));
        buttonsPanel.setOpaque(false);

        JButton playButton = new JButton("Play");
        playButton.addActionListener(e -> switchToGamePanel());
        playButton.setBackground(Color.orange);
        buttonsPanel.add(playButton);

        JButton rulesButton = new JButton("Rules");
        rulesButton.addActionListener(e -> switchToRulesPanel());
        rulesButton.setBackground(Color.orange);
        buttonsPanel.add(rulesButton);

        JButton settingsButton = new JButton("Settings");
        settingsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Implementa l'azione per il pulsante "Settings"
            }
        });
        settingsButton.setBackground(Color.orange);
        buttonsPanel.add(settingsButton);

        menuPanel.add(buttonsPanel, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, CENTER, GridBagConstraints.BOTH, new Insets(180, 0, 0, 0), 0, 0));

        frame.getContentPane().removeAll();
        frame.setContentPane(menuPanel);
        frame.setVisible(true);
    }

    private static void switchToGamePanel() {

        JPanel imagePanel = new JPanel() {
            private Image backgroundImage = new ImageIcon("src/main/java/GUI/Icons/table.jpg").getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };

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
}
