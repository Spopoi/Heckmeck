package GUI;

import Heckmeck.*;
import exception.IllegalInput;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static javax.swing.JOptionPane.*;
import static java.awt.GridBagConstraints.*;

public class GUIIOHandler implements IOHandler {

    //TODO: removing method's parameters and adding a Game local variable which contains players dice etc..
    private final JFrame frame;
    private JPanel playerPane;
    private JPanel dicePanel;
    private JPanel othersPlayerPane;
    private JPanel tilesPanel;
    private JPanel messagePanel;
    private volatile Die.Face chosenFace;

    private static final Map<Die.Face, String> faceToIconPath =
            Collections.unmodifiableMap(new HashMap<Die.Face, String>() {{
                put(Die.Face.ONE, "src/main/java/GUI/Icons/Dice/one.png");
                put(Die.Face.TWO, "src/main/java/GUI/Icons/Dice/two.png");
                put(Die.Face.THREE, "src/main/java/GUI/Icons/Dice/three.png");
                put(Die.Face.FOUR, "src/main/java/GUI/Icons/Dice/four.png");
                put(Die.Face.FIVE, "src/main/java/GUI/Icons/Dice/five.png");
                put(Die.Face.WORM, "src/main/java/GUI/Icons/Dice/worm.png");
            }});

    private static final Map<Integer, String> tileNumberToIconPath =
            Collections.unmodifiableMap(new HashMap<Integer, String>() {{
                put(21, "src/main/java/GUI/Icons/Tiles/Tile_21.png");
                put(22, "src/main/java/GUI/Icons/Tiles/Tile_22.png");
                put(23, "src/main/java/GUI/Icons/Tiles/Tile_23.png");
                put(24, "src/main/java/GUI/Icons/Tiles/Tile_24.png");
                put(25, "src/main/java/GUI/Icons/Tiles/Tile_25.png");
                put(26, "src/main/java/GUI/Icons/Tiles/Tile_26.png");
                put(27, "src/main/java/GUI/Icons/Tiles/Tile_27.png");
                put(28, "src/main/java/GUI/Icons/Tiles/Tile_28.png");
                put(29, "src/main/java/GUI/Icons/Tiles/Tile_29.png");
                put(30, "src/main/java/GUI/Icons/Tiles/Tile_30.png");
                put(31, "src/main/java/GUI/Icons/Tiles/Tile_31.png");
                put(32, "src/main/java/GUI/Icons/Tiles/Tile_32.png");
                put(33, "src/main/java/GUI/Icons/Tiles/Tile_33.png");
                put(34, "src/main/java/GUI/Icons/Tiles/Tile_34.png");
                put(35, "src/main/java/GUI/Icons/Tiles/Tile_35.png");
                put(36, "src/main/java/GUI/Icons/Tiles/Tile_36.png");

            }});

    public GUIIOHandler(JFrame frame){
        this.frame =frame;
        this.playerPane = new JPanel();
        this.dicePanel = new JPanel();
        this.othersPlayerPane = new JPanel();
        this.tilesPanel = new JPanel();
        this.messagePanel = new JPanel();
        frame.setVisible(true);
    }

    @Override
    public void printMessage(String message) {
        showMessageDialog(null, message);
    }

    @Override
    public void showTurnBeginConfirm(String playerName) {
        frame.getContentPane().removeAll();
        frame.repaint();
        printMessage(playerName + " turn, press ok for starting: ");
    }

    @Override
    public void showWelcomeMessage() {
        printMessage("WELCOME to Heckmeck!");
    }

    // TODO: REMOVE BEFORE COMMIT!!
    @Override
    public boolean wantToPlayRemote() {
        return false;
    }

    @Override
    public int chooseNumberOfPlayers() {
        while(true){
            try{
                int numberOfPlayer = Integer.parseInt(showInputDialog(null, "Choose number of players between 2 and 7:"));
                if(Rules.validNumberOfPlayer(numberOfPlayer)) return numberOfPlayer;
                else throw new IllegalInput("Invalid number of player, please select a number between 2 and 7");
            } catch (IllegalInput | NumberFormatException e) {
                printMessage(e.getMessage());
            }
        }
    }

    @Override
    public String choosePlayerName(int playerNumber) {

        while(true) {
            try {
                String playerName = showInputDialog(null, "Insert player name");
                if (playerName.isBlank()) throw new IllegalInput("Blank name, choose a valid a one");
                else return playerName;
            } catch (IllegalInput e) {
                printMessage(e.getMessage());
            }
        }
    }

    @Override
    public void showBoardTiles(BoardTiles boardTiles) {

        tilesPanel = new JPanel();
        tilesPanel.setLayout(new GridLayout(1,0,10, 10));
        tilesPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        tilesPanel.setPreferredSize(new Dimension(0,125));
        tilesPanel.setBackground(Color.LIGHT_GRAY);
        //tilesPanel.setBackground(Color.PINK);

        for(Tile tile : boardTiles.getTiles()){
            JLabel tileIcon = new JLabel(getTileIcon(tile.getNumber()));
            tileIcon.setPreferredSize(new Dimension(80,90));
            tilesPanel.add(tileIcon);

        }
        frame.getContentPane().add(tilesPanel,BorderLayout.NORTH);
        frame.setVisible(true);
    }

    @Override
    public void askRollDiceConfirmation(String playerName) {
        return;
    }

    @Override
    public void showRolledDice(Dice dice) {
        return;
    }

    // TODO: move method to the new signature
    @Override
    public boolean wantToPick(int actualDiceScore, int availableTileNumber) {
        int result = showConfirmDialog(null, "Do you want to pick? Your actual score is " +actualDiceScore);
        return result == JOptionPane.OK_OPTION;
    }

    @Override
    public boolean wantToSteal(Player robbedPlayer) {
        int result = showConfirmDialog(null, "Do you want to steal tile #" + robbedPlayer.getLastPickedTile().getNumber() + " from "+ robbedPlayer.getName()+"?");
        return result == JOptionPane.OK_OPTION;
    }

    @Override
    public void showPlayerData(Player player, Dice dice, Player[] players) {

        frame.getContentPane().remove(playerPane);

        showOthersPlayerPanel(player, players);

        int colIndex = 0;

        playerPane = new JPanel();
        playerPane.setLayout(new GridBagLayout());
        playerPane.setBorder(BorderFactory.createEmptyBorder(0, 30, 10, 5));

        JLabel playerName = new JLabel("Player #" + player.getName());
        playerName.setPreferredSize(new Dimension(250, 50));
        playerName.setFont(new Font("Serif", Font.BOLD, 30));
        playerPane.add(playerName, new GridBagConstraints(0, colIndex, 1, 1, 1.0, 0.0, GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        colIndex++;

        JLabel score = new JLabel("Your score: " + dice.getScore());
        score.setPreferredSize(new Dimension(200, 50));
        score.setFont(new Font("Serif", Font.PLAIN, 25));
        score.setForeground(Color.red);
        playerPane.add(score, new GridBagConstraints(0, colIndex, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        colIndex++;

        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setPreferredSize(new Dimension(1, 8));
        separator.setBorder(BorderFactory.createMatteBorder(2, 0, 1, 0, Color.black));
        playerPane.add(separator, new GridBagConstraints(0, colIndex, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 0, 10, 0), 0, 0));
        colIndex++;

        if(player.hasTile()) {

            JLabel lastTileLabel = new JLabel("Last picked tile: ");
            lastTileLabel.setPreferredSize(new Dimension(130, 50));
            lastTileLabel.setFont(new Font("Serif", Font.PLAIN, 20));
            playerPane.add(lastTileLabel, new GridBagConstraints(0, colIndex, 1, 1, 0.0, 1.0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
            colIndex++;

            JLabel lastPlayerTile = new JLabel(getTileIcon(player.getLastPickedTile().getNumber()));
            lastPlayerTile.setPreferredSize(new Dimension(80, 90));
            playerPane.add(lastPlayerTile, new GridBagConstraints(0, colIndex, 1, 1, 0.0, 1.0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
            colIndex++;

        }

        if(!dice.getChosenDice().isEmpty()) {
            JLabel chosenDiceLabel = new JLabel("Chosen dice:");
            chosenDiceLabel.setPreferredSize(new Dimension(200, 50));
            chosenDiceLabel.setFont(new Font("Serif", Font.PLAIN, 20));
            playerPane.add(chosenDiceLabel, new GridBagConstraints(0, colIndex, 1, 1, 1.0, 1.0, WEST, NONE, new Insets(0, 0, 0, 0), 0, 0));
            colIndex++;
        }

        JPanel dicePanel = new JPanel();
        dicePanel.setLayout(new GridBagLayout());
        dicePanel.setMaximumSize(new Dimension(250,150));

        int diceRowIndex = 0;
        for(Die die : dice.getChosenDice()){
            JLabel dieIconLabel = new JLabel(getDieIcon(die.getDieFace()));
            dieIconLabel.setPreferredSize(new Dimension(50,50));

            if(diceRowIndex < 4){
                dicePanel.add(dieIconLabel, new GridBagConstraints(diceRowIndex, 0, 1, 1, 1.0, 0.0, WEST, NONE, new Insets(0, 0, 0, 5), 0, 0 ));
            }else{
                dicePanel.add(dieIconLabel, new GridBagConstraints(diceRowIndex - 4, 1, 1, 1, 1.0, 0.0, WEST, NONE, new Insets(10, 0, 0, 5), 0, 0 ));
            }
            diceRowIndex++;
        }
        dicePanel.setAlignmentX(LEFT_ALIGNMENT);
        playerPane.add(dicePanel, new GridBagConstraints(0, colIndex, 1, 1, 1.0, 1.0, WEST, NONE, new Insets(0, 0, 60, 0), 0, 0 ));

        frame.add(playerPane, BorderLayout.WEST);
        frame.setVisible(true);
    }

    private void showOthersPlayerPanel(Player player, Player[] players) {
        frame.getContentPane().remove(othersPlayerPane);
        othersPlayerPane = new JPanel();
        othersPlayerPane.setLayout(new GridBagLayout());
        othersPlayerPane.setPreferredSize(new Dimension(200,0));
        othersPlayerPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 20));
        int index= 0;
        for(Player otherPlayer : players){
            if(!player.equals(otherPlayer)){
                if(otherPlayer.hasTile()){
                    JLabel otherPlayerLabel = new JLabel(otherPlayer.getName() + "'s last tile: ");
                    otherPlayerLabel.setFont(new Font("Serif", Font.BOLD, 15));
                    othersPlayerPane.add(otherPlayerLabel, new GridBagConstraints(0, index, 1, 1, 1.0, 0.0, PAGE_START, BOTH, new Insets(0, 0, 20, 0), 0, 0 ));

                    JLabel otherPlayerLastTile = new JLabel(getTileIcon(otherPlayer.getLastPickedTile().getNumber()));
                    otherPlayerLastTile.setPreferredSize(new Dimension(10,80));
                    othersPlayerPane.add(otherPlayerLastTile, new GridBagConstraints(1, index, 1, 1, 1.0, 0.0, PAGE_START, BOTH, new Insets(0, 0, 20, 0), 0, 0 ));

                    //othersPlayerPane.add(Box.createVerticalGlue());
                }
                else{
                    JLabel otherPlayerLabel = new JLabel("<html>"+otherPlayer.getName() + "<br> No Tiles </html>");
                    otherPlayerLabel.setFont(new Font("Serif", Font.BOLD, 15));
                    othersPlayerPane.add(otherPlayerLabel, new GridBagConstraints(0, index, 1, 1, 1.0, 0.0, PAGE_START, BOTH, new Insets(0, 0, 20, 0), 0, 0 ));

                    //othersPlayerPane.add(Box.createVerticalGlue());
                }
                index++;
            }
        }
        othersPlayerPane.setAlignmentX(CENTER);

        frame.add(othersPlayerPane, BorderLayout.EAST);
        frame.setVisible(true);
    }

    @Override
    public Die.Face chooseDie(Dice dice) {
        //TODO: se chiudi la finestra deve aspettare un'altra selezione di dadi
        frame.getContentPane().remove(dicePanel);
        chosenFace = null;

        dicePanel = new JPanel();
        dicePanel.setLayout(new GridLayout(2,4,20, 20));
        dicePanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 50, 50));

        for(Die die : dice.getDiceList()){
            JButton dieButton = new JButton(getDieIcon(die.getDieFace()));
            dieButton.addActionListener(e -> chosenFace = die.getDieFace());
            dieButton.setPreferredSize(new Dimension(60,60));
            dicePanel.add(dieButton, CENTER_ALIGNMENT);
        }

        frame.getContentPane().add(dicePanel,BorderLayout.CENTER);
        frame.setVisible(true);

        while (chosenFace == null) {
            Thread.onSpinWait();
        }
        return chosenFace;
    }

    @Override
    public void showBustMessage() {
        printMessage("BUUUUUSTTTT!!!!");
    }

    @Override
    public String getInputString() {
        return null;
    }

    @Override
    public String printError(String text) {
        return null;
    }


    //TODO: give the size of Tile Icon as parameter
    private ImageIcon getTileIcon(int tileNumber){
        ImageIcon icon = new ImageIcon(tileNumberToIconPath.get(tileNumber));
        Image img = icon.getImage() ;
        Image newimg = img.getScaledInstance(80 , 90,  java.awt.Image.SCALE_SMOOTH ) ;
        return new ImageIcon( newimg );
    }

    private ImageIcon getDieIcon(Die.Face face){
        ImageIcon icon = new ImageIcon(faceToIconPath.get(face));
        Image img = icon.getImage() ;
        Image newimg = img.getScaledInstance(50 , 50,  java.awt.Image.SCALE_SMOOTH ) ;
        return new ImageIcon( newimg );
    }
}
