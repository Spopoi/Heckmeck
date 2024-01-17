package GUI;

import GUI.Panels.*;
import Heckmeck.*;
import Utils.PropertiesManager;
import Utils.TCP.ConnectionHandler;
import com.formdev.flatlaf.FlatIntelliJLaf;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import static Utils.GUI.SoundHandler.*;
import static Utils.TCP.ConnectionHandler.startGameServer;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;

public class HeckmeckGUI extends Launcher{
    private static JFrame frame;
    //public static String YELLOW_BACKGROUND_PATH = "src/main/resources/GUI/yellowBackground.png";
    //public static String BACKGROUND_IMAGE_PATH = YELLOW_BACKGROUND_PATH;
    public static String BACKGROUND_IMAGE_PATH;
    //public static final String GREEN_BACKGROUND_PATH = "src/main/resources/GUI/green_background.jpg";
    //public static final String BLUE_BACKGROUND_PATH = "src/main/resources/GUI/blue_background.png";
    private static final Dimension minimumFrameDimension = new Dimension(1100, 600);
    private static PropertiesManager pathManager;

    public static void main(String[] args){
        try {
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(HeckmeckGUI::initGUI);
    }

    //TODO: PROP
    private static void initGUI() {
        frame = new JFrame("HECKMECK");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(minimumFrameDimension);
        frame.setLocationRelativeTo(null);
        initPathManager();
        setBackgroundImagePath(pathManager.getMessage("YELLOW_BACKGROUND_PATH"));
        switchToMenuPanel();
        playLoopingSound(BACKGROUND_SOUND_PATH, BACKGROUND_MUSIC_VOLUME);
        frame.setVisible(true);
    }

    private static void initPathManager(){
        try {
            pathManager = new PropertiesManager(PropertiesManager.getPathPropertiesPath());
        } catch (IOException e) {
            showMessageDialog(null, "Error loading the file containing the messages of the game", "ERROR", ERROR_MESSAGE);
            throw new RuntimeException(e);
        }
    }

    public static PropertiesManager getPathManager(){return pathManager;}


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
        final var worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                ConnectionHandler.startLocalClient(IP, io);
                switchToMenuPanel();
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
        final var worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                try {
                    startGame(io);
                } catch (IOException e) {
                    //TODO: PROP
                    showMessageDialog(null, "Error loading the file containing the messages of the game" , "Heckmeck ERROR", JOptionPane.ERROR_MESSAGE);
                    exit();
                }
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
