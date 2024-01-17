package GUI;

import GUI.Panels.*;
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
import static Utils.GUI.SoundHandler.*;
import static javax.swing.JOptionPane.*;

public class GUIIOHandler implements IOHandler {
    private final JFrame frame;
    private PlayerDataPanel playerPane;
    private DicePanel dicePanel;
    private JScrollPane otherPlayerPane;
    private BoardtilesPanel tilesPanel;
    private int lateralPanelWidth;
    private static final double PANEL_WIDTH_TO_FRAME_RATIO = 0.25;
    private static final double LOG_HEIGHT_TO_FRAME_RATIO = 0.15;
    private static final Icon WORM_ICON = getDieIcon(Die.Face.WORM);
    private static final int BUST_DELAY = 2000;

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
        tilesPanel = new BoardtilesPanel();
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

    public String askIPToConnect() {
        return MessagePanel.showInputDialog(messages.getProperty("askIP"));
    }

    @Override
    public void backToMenu() {
        switchToMenuPanel();
    }

    @Override
    public void showTurnBeginConfirm(Player player) {
        String message = player.getName() + messages.getProperty("turnBeginConfirm");
        showMessageDialog(null, message, messages.getProperty("heckmeckMessage"), INFORMATION_MESSAGE , WORM_ICON);
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
            else return Integer.parseInt(selectedOption);
        }
    }

    private void wantToQuitHeckmeck() {
        if(messagePanel.showYesNoPanel(messages.getProperty("wantToQuit"))) Launcher.exit();
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
        tilesPanel.update(boardTiles);
        frame.add(tilesPanel, BorderLayout.NORTH);
        updateFrame();
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
        frame.remove(playerPane);
        frame.add(playerPane, BorderLayout.WEST);
        updateFrame();
    }

    private void updateFrame(){
        frame.revalidate();
        frame.repaint();
    }
    private void showOthersPlayerPanel(Player player, Player[] players) {
        if(otherPlayerPane != null) frame.remove(otherPlayerPane);
        ScoreboardPanel scoreboardPanel = new ScoreboardPanel(player, players);
        otherPlayerPane = scoreboardPanel.getScrollableScoreboard();
        otherPlayerPane.setPreferredSize(new Dimension(lateralPanelWidth,0));
        frame.add(otherPlayerPane, BorderLayout.EAST);
        updateFrame();
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

    //TODO: update dicePanel instead of remove it
    @Override
    public void showRolledDice(Dice dice) {
        frame.getContentPane().remove(dicePanel);
        dicePanel.updateDice(dice);
        frame.getContentPane().add(dicePanel, BorderLayout.CENTER);
        updateFrame();
        dicePanel.rollDiceAnimation();
        playSound(ROLLING_SOUND_PATH, ACTIONS_MUSIC_VOLUME);
    }

    @Override
    public void showBustMessage() {  // todo: sta roba senza il delay non funzia più??
        try {
            Thread.sleep(BUST_DELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        printMessage(messages.getProperty("bust"));
    }
}