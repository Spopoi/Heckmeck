package GUI;

import GUI.Panels.*;
import Heckmeck.*;
import TCP.Client;
import Utils.TCP.ConnectionHandler;
import com.formdev.flatlaf.FlatIntelliJLaf;
import javax.swing.*;
import java.awt.*;

import static Utils.TCP.ConnectionHandler.startGameServer;

public class HeckmeckGUI {
    private static JFrame frame;
    public static String YELLOW_BACKGROUND_PATH = "src/main/resources/GUI/yellowBackground.png";
    public static String BACKGROUND_IMAGE_PATH = YELLOW_BACKGROUND_PATH;
    public static final String GREEN_BACKGROUND_PATH = "src/main/resources/GUI/green_background.jpg";
    public static final String BLUE_BACKGROUND_PATH = "src/main/resources/GUI/blue_background.png";
    private static final Dimension minimumFrameDimension = new Dimension(1100, 600);

    public static void main(String[] args) throws UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(new FlatIntelliJLaf());
        SwingUtilities.invokeLater(HeckmeckGUI::initGUI);
    }

    private static void initGUI() {
        frame = new JFrame("HECKMECK");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(minimumFrameDimension);
        frame.setLocationRelativeTo(null);
        switchToMenuPanel();
        frame.setVisible(true);
    }

    public static void setBackgroundImagePath(String path) {
        BACKGROUND_IMAGE_PATH = path;
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
        SettingsPanel settingsPanel = new SettingsPanel();
        updateFrame(settingsPanel);
    }

    public static void switchToMultiplayerPanel() {
        ImagePanel imagePanel = new ImagePanel(BACKGROUND_IMAGE_PATH);

        frame.getContentPane().removeAll();
        frame.setContentPane(imagePanel);
        frame.getContentPane().setLayout(new BorderLayout());

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

    public static void startLocalClient(String IP, IOHandler io){
        Client cli = ConnectionHandler.startClient(IP, io);
        io.printMessage("Connected, waiting for other players to begin");
        final var worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                cli.commandInterpreter();
                return null;
            }
        };
        worker.execute();
        frame.revalidate();
        frame.repaint();

    }
    public static void startLocalClient(IOHandler io) {
        startLocalClient("127.0.0.1", io);
    }

    public static void switchToGamePanel() {
        ImagePanel imagePanel = new ImagePanel(BACKGROUND_IMAGE_PATH);

        frame.getContentPane().removeAll();
        frame.setContentPane(imagePanel);
        frame.getContentPane().setLayout(new BorderLayout());

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
