package GUI;

import Heckmeck.*;

import javax.swing.*;
import java.awt.*;

public class HeckmeckGUI {
    private static JFrame frame;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(HeckmeckGUI::initGUI);
    }

    private static void initGUI() {

        frame = new JFrame("HECKMECK");
        frame.setSize(1200, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        switchToMenuPanel();
        frame.setVisible(true);
    }

    public static void switchToRulesPanel() {
        RulesPanel rulesPanel = new RulesPanel();
        updateFrame(rulesPanel);
    }

    public static void switchToMenuPanel(){
        MenuPanel menuPanel = new MenuPanel();
        updateFrame(menuPanel);
    }

    public static void switchToSettings() {
        SettingsPanel settingsPanel = new SettingsPanel(frame);
        updateFrame(settingsPanel);
    }


    public static void switchToGamePanel() {

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

    private static void updateFrame(JPanel newPanel){
        frame.getContentPane().removeAll();
        frame.setContentPane(newPanel);
        frame.revalidate();
        frame.repaint();
    }
}
