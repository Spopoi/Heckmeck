package GUI;

import GUI.Panels.*;
import Heckmeck.*;
import com.formdev.flatlaf.FlatIntelliJLaf;
import javax.swing.*;
import java.awt.*;

import static CLI.HeckmeckCLI.startGameServer;
import static CLI.HeckmeckCLI.startLocalClient;

public class HeckmeckGUI {
    private static JFrame frame;
    public static final String BACKGROUND_IMAGE_PATH = "src/main/resources/Icons/table.jpg";

    public static void main(String[] args) throws UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(new FlatIntelliJLaf());
        SwingUtilities.invokeLater(HeckmeckGUI::initGUI);
    }

    private static void initGUI() {
        frame = new JFrame("HECKMECK");
        frame.setSize(1100, 600);
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

    public static void switchToSettingsPanel() {
        SettingsPanel settingsPanel = new SettingsPanel(frame);
        updateFrame(settingsPanel);
    }

    public static void switchToMultiplayerPanel() {
        ImagePanel imagePanel = new ImagePanel(BACKGROUND_IMAGE_PATH);

        frame.getContentPane().removeAll();
        frame.setContentPane(imagePanel);
        frame.getContentPane().setLayout(new BorderLayout(50, 30));

        GUIIOHandler io = new GUIIOHandler(frame);
        if(io.wantToHost()){
            int numOfPlayers = io.chooseNumberOfPlayers();
            startGameServer(numOfPlayers);
            startLocalClient(io);
        }
        else{
            startLocalClient(io.askIPToConnect(), io);
        }
    }

    public static void switchToGamePanel() {
        ImagePanel imagePanel = new ImagePanel(BACKGROUND_IMAGE_PATH);

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
