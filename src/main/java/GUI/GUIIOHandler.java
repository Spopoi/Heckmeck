package GUI;

import GUI.Panels.DicePanel;
import GUI.Panels.PlayerDataPanel;
import GUI.Panels.ScoreboardPanel;
import Heckmeck.*;
import Heckmeck.Components.*;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.util.Properties;

import static GUI.HeckmeckGUI.BACKGROUND_IMAGE_PATH;
import static Utils.GUI.IconHandler.getDieIcon;
import static Utils.GUI.IconHandler.getTileIcon;
import static javax.swing.JOptionPane.*;

public class GUIIOHandler implements IOHandler {
    private final JFrame frame;
    private PlayerDataPanel playerPane;
    private DicePanel dicePanel;
    private JScrollPane otherPlayerPane;
    private JPanel tilesPanel;
    private int lateralPanelWidth;
    private static final double PANEL_TO_FRAME_RATIO = 0.25;
    private static final int BUST_DELAY = 2000;
    private static final int TILES_GAP = 10;
    private static final int TOP_BORDER = 20;
    private static final int BOARDTILES_BOTTOM_BORDER = 40;
    private static final String HECKMECK_MESSAGES_FILENAME = "messages";
    private Properties messages;

    public GUIIOHandler(JFrame frame){
        this.frame = frame;
        initPanels();
        initHeckmeckMessages();
    }

    private void initPanels(){
        lateralPanelWidth = (int)(frame.getWidth() * PANEL_TO_FRAME_RATIO);
        playerPane = new PlayerDataPanel(BACKGROUND_IMAGE_PATH);
        playerPane.setPreferredSize(new Dimension(lateralPanelWidth,0));

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
        showInternalMessageDialog(null, message, messages.getProperty("heckmeckMessage"), INFORMATION_MESSAGE , getDieIcon(Die.Face.WORM));
    }

    @Override
    public void printError(String text) {
        JOptionPane.showMessageDialog(null, text, messages.getProperty("error"), JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void showWelcomeMessage() {
        printMessage(messages.getProperty("welcomeMessage"));
    }

    @Override
    public String askIPToConnect() {
        return JOptionPane.showInputDialog(null, messages.getProperty("askIP"), messages.getProperty("heckmeckMultiplayer"), JOptionPane.QUESTION_MESSAGE);
    }

    @Override
    public boolean wantToPlayAgain() {
        int result = JOptionPane.showOptionDialog(
                null,
                messages.getProperty("wantToPlayAgain"),
                messages.getProperty("heckmeckTitle"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                getDieIcon(Die.Face.WORM),
                null,
                null
        );
        return result == JOptionPane.YES_OPTION;
    }

    @Override
    public void showTurnBeginConfirm(Player player) {
        frame.getContentPane().removeAll();
        frame.repaint();
        printMessage(player.getName() + messages.getProperty("turnBeginConfirm"));
    }

    public boolean wantToHost(){
        int result = JOptionPane.showOptionDialog(
                null,
                messages.getProperty("wantToHost"),
                messages.getProperty("heckmeckMultiplayer"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                getDieIcon(Die.Face.WORM),
                null,
                null
        );
        return result == JOptionPane.YES_OPTION;
    }

    @Override
    public int chooseNumberOfPlayers() {
        while (true) {
            String[] options = {"2", "3", "4", "5", "6", "7"};
            String selectedOption = (String) JOptionPane.showInputDialog(null, messages.getProperty("chooseNumberPlayer"), messages.getProperty("heckmeckTitle"), JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (selectedOption == null) wantToQuitHeckmeck();
            else if(Rules.validNumberOfPlayer(Integer.parseInt(selectedOption)))
                return Integer.parseInt(selectedOption);
        }
    }

    private void wantToQuitHeckmeck() {
        int wantToQuit = JOptionPane.showConfirmDialog(null, messages.getProperty("wantToQuit"), messages.getProperty("heckmeckTitle"), JOptionPane.YES_NO_OPTION);
        if(wantToQuit == OK_OPTION) System.exit(0);
    }

    @Override
    public String choosePlayerName(Player player) {
        while(true) {
            String playerName = showInputDialog(null, messages.getProperty("choosePlayerName"));
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
            tilesPanel.add(getTileIcon(tile.getNumber()));
        }
        frame.add(tilesPanel, BorderLayout.NORTH);
    }

    @Override
    public boolean wantToPick(Player player, int actualDiceScore, int availableTileNumber) {
        String message = messages.getProperty("actualScore") + " " + actualDiceScore + '\n' + messages.getProperty("wantToPick") + " " + availableTileNumber + "?";
        int result = JOptionPane.showOptionDialog(
                null,
                message,
                messages.getProperty("heckmeckTitle"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                getDieIcon(Die.Face.WORM),
                null,
                null
        );
        return result == JOptionPane.YES_OPTION;
    }

    @Override
    public boolean wantToSteal(Player player, Player robbedPlayer) {
        int result = showConfirmDialog(null, messages.getProperty("wantToSteal") + robbedPlayer.getLastPickedTile().getNumber() + " from "+ robbedPlayer.getName()+"?");
        return result == JOptionPane.OK_OPTION;
    }

    @Override
    public void showPlayerData(Player player, Dice dice, Player[] players) {
        showOthersPlayerPanel(player, players);
        playerPane.update(player, dice);
        frame.add(playerPane, BorderLayout.WEST);
        frame.revalidate();
        frame.repaint();
    }

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
        return dicePanel.getChosenFace();
    }

    @Override
    public void showRolledDice(Dice dice) {
        updateDicePanel(dice);
        dicePanel.rollDiceAnimation();
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

    @Override
    public String getInputString() {
        return null;
    }

}