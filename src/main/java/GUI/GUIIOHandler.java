package GUI;

import GUI.Panels.DicePanel;
import GUI.Panels.PlayerDataPanel;
import GUI.Panels.ScoreboardPanel;
import Heckmeck.*;

import javax.swing.*;
import java.awt.*;

import static GUI.Icons.IconHandler.getDieIcon;
import static GUI.Icons.IconHandler.getTileIcon;
import static javax.swing.JOptionPane.*;

public class GUIIOHandler implements IOHandler {

    public static final Dimension BOARD_TILE_DIMENSIONS = new Dimension(80, 90);
    private final JFrame frame;
    private PlayerDataPanel playerPane;
    private DicePanel dicePanel;
    private ScoreboardPanel scoreboardPanel;
    private JPanel tilesPanel;

    public GUIIOHandler(JFrame frame){
        this.frame = frame;
        dicePanel = new DicePanel();
        tilesPanel = new JPanel();
        playerPane = new PlayerDataPanel("src/main/java/GUI/Icons/table.jpg");
        frame.setVisible(true);
    }

    @Override
    public void printMessage(String message) {
        showInternalMessageDialog(null, message, "Heckmeck message", INFORMATION_MESSAGE , getDieIcon(Die.Face.WORM,50));
    }

    @Override
    public void printError(String text) {
        JOptionPane.showMessageDialog(null, text, "Errore", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void showWelcomeMessage() {
        printMessage("WELCOME to Heckmeck, GOOD LUCK!");
    }

    @Override
    public String askIPToConnect() {
        return null;
    }

    //TODO: togliere dall'interfaccia?
    @Override
    public boolean wantToPlayAgain() {
        return false;
    }

    @Override
    public void showTurnBeginConfirm(Player player) {
        frame.getContentPane().removeAll();
        frame.repaint();
        printMessage(player.getName() + " turn, press ok for starting: ");
    }

    // TODO: REMOVE BEFORE COMMIT!!
    @Override
    public boolean wantToPlayRemote() {
        return false;
    }

    public boolean wantToHost(){
        int result = JOptionPane.showOptionDialog(
                null,
                "Do you want to host?",
                "Heckmeck multiplayer",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                getDieIcon(Die.Face.WORM,50),
                null,
                null
        );
        return result == JOptionPane.YES_OPTION;
    }

    @Override
    public int chooseNumberOfPlayers() {
        while (true) {
            String[] options = {"2", "3", "4", "5", "6", "7"};
            String selectedOption = (String) JOptionPane.showInputDialog(null, "Choose number of players:", "Heckmeck", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

            if (selectedOption == null) wantToQuitHeckmeck();
            else if(Rules.validNumberOfPlayer(Integer.parseInt(selectedOption)))
                return Integer.parseInt(selectedOption);
        }
    }

    private static void wantToQuitHeckmeck() {
        int wantToQuit = JOptionPane.showConfirmDialog(null, "Do you want to quit from Heckmeck?", "Heckmeck", JOptionPane.YES_NO_OPTION);
        if(wantToQuit == OK_OPTION) System.exit(0);
    }

    @Override
    public String choosePlayerName(int playerNumber) {
        while(true) {
            String playerName = showInputDialog(null, "Insert player name");
            if (playerName == null) wantToQuitHeckmeck();
            else if(playerName.isBlank()) printError("Blank name, choose a valid one");
            else return playerName;
        }
    }

    @Override
    public void showBoardTiles(BoardTiles boardTiles) {
        tilesPanel = new JPanel();
        tilesPanel.setLayout(new GridLayout(1,0,10, 10));
        tilesPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 0, 20));
        tilesPanel.setPreferredSize(new Dimension(0,125));
        tilesPanel.setOpaque(false);
        for(Tile tile : boardTiles.getTiles()){
            JLabel tileIcon = new JLabel(getTileIcon(tile.getNumber(), 90, 80));
            tileIcon.setPreferredSize(BOARD_TILE_DIMENSIONS);
            tilesPanel.add(tileIcon);
        }
        frame.add(tilesPanel,BorderLayout.NORTH);
    }

    @Override
    public boolean wantToPick(Player player, int actualDiceScore, int availableTileNumber) {
        int result = JOptionPane.showOptionDialog(
                null,
                "Actual score: " + actualDiceScore + "\nDo you want to pick tile " + availableTileNumber + "?",
                "Heckmeck",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                getDieIcon(Die.Face.WORM,50),
                null,
                null
        );
        return result == JOptionPane.YES_OPTION;
    }

    @Override
    public boolean wantToSteal(Player player, Player robbedPlayer) {
        int result = showConfirmDialog(null, "Do you want to steal tile #" + robbedPlayer.getLastPickedTile().getNumber() + " from "+ robbedPlayer.getName()+"?");
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
        scoreboardPanel = new ScoreboardPanel(player,players);

        JScrollPane scrollPane = new JScrollPane(scoreboardPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        frame.add(scrollPane, BorderLayout.EAST);
        frame.revalidate();
        frame.repaint();
    }

    @Override
    public Die.Face chooseDie(Player player, Dice dice) {
        updateDicePanel(dice);
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
    public void askRollDiceConfirmation(String playerName){
        return;
    }

    @Override
    public void showBustMessage() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        printMessage("BUUUUUSTTTT!!!!");
    }

    @Override
    public String getInputString() {
        return null;
    }

}