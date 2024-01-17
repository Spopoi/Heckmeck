package GUI;

import GUI.Panels.*;
import Heckmeck.*;
import Heckmeck.Components.*;
import Utils.PropertiesManager;

import javax.swing.*;
import java.awt.*;
import java.util.stream.IntStream;

import static GUI.HeckmeckGUI.*;
import static Heckmeck.Rules.MAX_NUM_OF_PLAYERS;
import static Heckmeck.Rules.MIN_NUM_OF_PLAYERS;
import static Utils.GUI.IconHandler.getDieIcon;
import static Utils.GUI.SoundHandler.*;
import static javax.swing.JOptionPane.*;

public class GUIIOHandler implements IOHandler {
    private final JFrame frame;
    private final PropertiesManager propertiesManager;
    private PlayerDataPanel playerPane;
    private DicePanel dicePanel;
    private JScrollPane otherPlayerPane;
    private BoardtilesPanel tilesPanel;
    private int lateralPanelWidth;
    private static final double PANEL_WIDTH_TO_FRAME_RATIO = 0.25;
    private static final double LOG_HEIGHT_TO_FRAME_RATIO = 0.15;
    private static final Icon WORM_ICON = getDieIcon(Die.Face.WORM);
    private static final int BUST_DELAY = 3000;
    private MessagePanel messagePanel;

    public GUIIOHandler(JFrame frame){
        this.frame = frame;
        propertiesManager = getPropertiesManager();
        initPanels();
    }

    private void initPanels(){
        lateralPanelWidth = (int)(frame.getWidth() * PANEL_WIDTH_TO_FRAME_RATIO);
        playerPane = new PlayerDataPanel(GAME_BACKGROUND_PATH);
        playerPane.setPreferredSize(new Dimension(lateralPanelWidth,0));

        messagePanel = new MessagePanel();
        messagePanel.setPreferredSize(new Dimension(0, (int) (frame.getHeight()*LOG_HEIGHT_TO_FRAME_RATIO)));
        frame.add(messagePanel, BorderLayout.SOUTH);

        dicePanel = new DicePanel();
        tilesPanel = new BoardtilesPanel();
    }

    @Override
    public void printMessage(String message) {
        messagePanel.showLogMessage(message);
    }

    @Override
    public void printError(String text) {
        messagePanel.showLogMessage(text);
    }

    public String askIPToConnect() {
        return MessagePanel.showInputDialog(propertiesManager.getMessage("askIP"));
    }

    @Override
    public void backToMenu() {
        switchToMenuPanel();
    }

    @Override
    public void showTurnBeginConfirm(Player player) {
        String message = propertiesManager.getMessage("turnBeginConfirm").replace("$PLAYER_NAME", player.getName());
        showMessageDialog(null, message, propertiesManager.getMessage("heckmeckMessage"), INFORMATION_MESSAGE , WORM_ICON);
    }

    public boolean wantToHost(){
        int result = JOptionPane.showOptionDialog(
                null,
                propertiesManager.getMessage("wantToHost"),
                propertiesManager.getMessage("heckmeckMultiplayer"),
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
            String selectedOption = (String) JOptionPane.showInputDialog(null, propertiesManager.getMessage("chooseNumberPlayer"), propertiesManager.getMessage("heckmeckTitle"), JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (selectedOption == null) wantToQuitHeckmeck();
            else return Integer.parseInt(selectedOption);
        }
    }

    private void wantToQuitHeckmeck() {
        if(messagePanel.showYesNoPanel(propertiesManager.getMessage("wantToQuit"))) Launcher.exit();
    }

    @Override
    public String choosePlayerName(Player player) {
        while(true) {
            String playerName = MessagePanel.showInputDialog(propertiesManager.getMessage("choosePlayerName").replace("$PLAYER_ID", Integer.toString(player.getPlayerID())));
            if (playerName == null) wantToQuitHeckmeck();
            else if(playerName.isBlank()) printError(propertiesManager.getMessage("blankName"));
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
        String message = propertiesManager.getMessage("actualScore").replace("$DICE_SCORE", Integer.toString(actualDiceScore)) +
                '\n' + propertiesManager.getMessage("wantToPick").replace("$TILE_NUMBER", Integer.toString(availableTileNumber));
        if(messagePanel.showYesNoPanel(message)){
            playSound(PICK_SOUND_PATH, ACTIONS_MUSIC_VOLUME);
            return true;
        }else return false;
    }

    @Override
    public boolean wantToSteal(Player player, Player robbedPlayer) {
        String message = propertiesManager.getMessage("wantToSteal").
                replace("$TILE_NUMBER", Integer.toString(robbedPlayer.getLastPickedTile().number())).
                replace("$ROBBED", robbedPlayer.getName());
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

    @Override
    public void showRolledDice(Dice dice) {
        frame.remove(dicePanel);
        dicePanel.updateDice(dice);
        frame.add(dicePanel, BorderLayout.CENTER);
        updateFrame();
        dicePanel.rollDiceAnimation();
        playSound(ROLLING_SOUND_PATH, ACTIONS_MUSIC_VOLUME);
    }

    @Override
    public void showBustMessage() {
        try {
            Thread.sleep(BUST_DELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        showMessageDialog(null, propertiesManager.getMessage("bust") ,  propertiesManager.getMessage("heckmeckTitle"), JOptionPane.ERROR_MESSAGE);
    }
}