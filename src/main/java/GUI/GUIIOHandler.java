package GUI;

import GUI.Panels.DicePanel;
import GUI.Panels.MessagePanel;
import GUI.Panels.PlayerDataPanel;
import GUI.Panels.ScoreboardPanel;
import Heckmeck.*;
import Heckmeck.Components.*;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.util.Properties;
import java.util.stream.IntStream;

import static GUI.HeckmeckGUI.*;
import static Heckmeck.Rules.MAX_NUM_OF_PLAYERS;
import static Heckmeck.Rules.MIN_NUM_OF_PLAYERS;
import static Utils.GUI.IconHandler.getDieIcon;
import static Utils.GUI.IconHandler.getTileIcon;
import static Utils.GUI.SoundHandler.*;
import static javax.swing.JOptionPane.*;

public class GUIIOHandler implements IOHandler {
    private final JFrame frame;
    private PlayerDataPanel playerPane;
    private DicePanel dicePanel;
    private JScrollPane otherPlayerPane;
    private JPanel tilesPanel;
    private int lateralPanelWidth;
    private static final double PANEL_WIDTH_TO_FRAME_RATIO = 0.25;
    private static final double LOG_HEIGHT_TO_FRAME_RATIO = 0.15;
    private static final Icon WORM_ICON = getDieIcon(Die.Face.WORM);
    private static final int BUST_DELAY = 2000;
    private static final int TILES_GAP = 10;
    private static final int TOP_BORDER = 20;
    private static final int BOARDTILES_BOTTOM_BORDER = 40;
    private MessagePanel messagePanel;
    private static final String HECKMECK_MESSAGES_FILENAME = "messages";
    private Properties messages;

    public GUIIOHandler(JFrame frame){
        this.frame = frame;
        initPanels();
        initHeckmeckMessages();
    }

    private void initPanels(){
        lateralPanelWidth = (int)(frame.getWidth() * PANEL_WIDTH_TO_FRAME_RATIO);
        playerPane = new PlayerDataPanel(BACKGROUND_IMAGE_PATH);
        playerPane.setPreferredSize(new Dimension(lateralPanelWidth,0));

        messagePanel = new MessagePanel();
        messagePanel.setPreferredSize(new Dimension(0, (int) (frame.getHeight()*LOG_HEIGHT_TO_FRAME_RATIO)));
        frame.add(messagePanel, BorderLayout.SOUTH);

        dicePanel = new DicePanel();
        tilesPanel = new JPanel();
    }

    private void initHeckmeckMessages(){
        messages = new Properties();
        try (InputStream input = GUIIOHandler.class.getClassLoader().getResourceAsStream(HECKMECK_MESSAGES_FILENAME)) {
            if (input == null) {
                printError("Sorry, unable to find " + HECKMECK_MESSAGES_FILENAME);
                return;
            }
            messages.load(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void printMessage(String message) {
        messagePanel.showLogMessage(message);
    }

    //TODO: error sound
    @Override
    public void printError(String text) {
        messagePanel.showLogMessage(text);
    }

    @Override
    public void showWelcomeMessage() {
        String message = messages.getProperty("welcomeMessage");
        showInternalMessageDialog(null, message, messages.getProperty("heckmeckMessage"), INFORMATION_MESSAGE , WORM_ICON);
    }

    @Override
    public String askIPToConnect() {
        return MessagePanel.showInputDialog(messages.getProperty("askIP"));
    }

    @Override
    public void backToMenu() {  // TODO farlo diventare solo un "Press ok to continue" (non più y/n)
        messagePanel.showYesNoPanel(messages.getProperty("wantToPlayAgain"));
    }

    @Override
    public void showTurnBeginConfirm(Player player) {
        frame.getContentPane().removeAll();
        frame.add(messagePanel, BorderLayout.SOUTH);
        frame.repaint();
        String message = player.getName() + messages.getProperty("turnBeginConfirm");
        showInternalMessageDialog(null, message, messages.getProperty("heckmeckMessage"), INFORMATION_MESSAGE , WORM_ICON);
    }

    public boolean wantToHost(){
        int result = JOptionPane.showOptionDialog(
                null,
                messages.getProperty("wantToHost"),
                messages.getProperty("heckmeckMultiplayer"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                WORM_ICON,
                null,
                null
        );
        return result == JOptionPane.YES_OPTION;
    }

    @Override
    public int chooseNumberOfPlayers() {
        String[] options = IntStream.rangeClosed(MIN_NUM_OF_PLAYERS, MAX_NUM_OF_PLAYERS)
                .mapToObj(Integer::toString)
                .toArray(String[]::new);
        while (true) {
            String selectedOption = (String) JOptionPane.showInputDialog(null, messages.getProperty("chooseNumberPlayer"), messages.getProperty("heckmeckTitle"), JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (selectedOption == null) wantToQuitHeckmeck();
            else if(Rules.validNumberOfPlayer(Integer.parseInt(selectedOption)))
                return Integer.parseInt(selectedOption);
        }
    }

    private void wantToQuitHeckmeck() {
        if(messagePanel.showYesNoPanel(messages.getProperty("wantToQuit"))) System.exit(0);
    }

    @Override
    public String choosePlayerName(Player player) {
        while(true) {
            String playerName = MessagePanel.showInputDialog(messages.getProperty("choosePlayerName"));
            if (playerName == null) wantToQuitHeckmeck();
            else if(playerName.isBlank()) printError(messages.getProperty("blankName"));
            else return playerName;
        }
    }

    @Override
    public void showBoardTiles(BoardTiles boardTiles) {
        frame.remove(tilesPanel);
        tilesPanel = new JPanel();
        tilesPanel.setLayout(new FlowLayout(FlowLayout.CENTER, TILES_GAP, 0));
        tilesPanel.setBorder(BorderFactory.createEmptyBorder(TOP_BORDER, 0, BOARDTILES_BOTTOM_BORDER, 0));
        tilesPanel.setOpaque(false);
        for (Tile tile : boardTiles.getTiles()) {
            tilesPanel.add(getTileIcon(tile.number()));
        }
        frame.add(tilesPanel, BorderLayout.NORTH);
    }

    @Override
    public boolean wantToPick(Player player, int actualDiceScore, int availableTileNumber) {
        String message = messages.getProperty("actualScore") + " " + actualDiceScore + '\n' + messages.getProperty("wantToPick") + " " + availableTileNumber + "?";
        if(messagePanel.showYesNoPanel(message)){
            playSound(PICK_SOUND_PATH, ACTIONS_MUSIC_VOLUME);
            return true;
        }else return false;
    }

    @Override
    public boolean wantToSteal(Player player, Player robbedPlayer) {
        String message = messages.getProperty("wantToSteal") + robbedPlayer.getLastPickedTile().number() + " from "+ robbedPlayer.getName()+"?";
        return messagePanel.showYesNoPanel(message);
    }

    @Override
    public void showPlayerData(Player player, Dice dice, Player[] players) {
        playerPane.update(player, dice);
        showOthersPlayerPanel(player, players);
        frame.add(playerPane, BorderLayout.WEST);
        frame.revalidate();
        frame.repaint();
    }

    //TODO: extract frame.revalidate and repaint

    private void showOthersPlayerPanel(Player player, Player[] players) {
        if(otherPlayerPane != null) frame.remove(otherPlayerPane);
        ScoreboardPanel scoreboardPanel = new ScoreboardPanel(player, players);
        otherPlayerPane = new JScrollPane(scoreboardPanel);
        otherPlayerPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        otherPlayerPane.setPreferredSize(new Dimension(lateralPanelWidth,0));
        frame.add(otherPlayerPane, BorderLayout.EAST);
        frame.revalidate();
        frame.repaint();
    }

    @Override
    public Die.Face chooseDie(Player player) {
        dicePanel.reset();
        while (dicePanel.getChosenFace() == null) {
            Thread.onSpinWait();
        }
        playSound(PICK_SOUND_PATH, ACTIONS_MUSIC_VOLUME);
        return dicePanel.getChosenFace();
    }

    @Override
    public void showRolledDice(Dice dice) {
        updateDicePanel(dice);
        dicePanel.rollDiceAnimation();
        playSound(ROLLING_SOUND_PATH, ACTIONS_MUSIC_VOLUME);
    }

    private void updateDicePanel(Dice dice){
        frame.getContentPane().remove(dicePanel);
        dicePanel.updateDice(dice);
        frame.getContentPane().add(dicePanel, BorderLayout.CENTER);
        frame.revalidate();
    }
    @Override
    public void askRollDiceConfirmation(Player playerName){
    }

    @Override
    public void showBustMessage() {
        try {
            Thread.sleep(BUST_DELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        printMessage(messages.getProperty("bust"));
    }
}