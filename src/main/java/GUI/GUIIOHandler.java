package GUI;

import GUI.Panels.ImagePanel;
import Heckmeck.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        showInternalMessageDialog(null, message, "Heckmeck message", INFORMATION_MESSAGE , getDieIcon(Die.Face.WORM,50));
    }

    @Override
    public void printError(String text) {
        JOptionPane.showMessageDialog(null, text, "Errore", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void showTurnBeginConfirm(String playerName) {
        frame.getContentPane().removeAll();
        frame.repaint();
        printMessage(playerName + " turn, press ok for starting: ");
    }

    @Override
    public void showWelcomeMessage() {
        printMessage("WELCOME to Heckmeck, GOOD LUCK!");
    }

    // TODO: REMOVE BEFORE COMMIT!!
    @Override
    public boolean wantToPlayRemote() {
        return false;
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
            tileIcon.setPreferredSize(new Dimension(80,90));
            tilesPanel.add(tileIcon);
        }
        frame.getContentPane().add(tilesPanel,BorderLayout.NORTH);
        //frame.setVisible(true);
    }

    @Override
    public void askRollDiceConfirmation(String playerName){
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

        ImagePanel playerPane = new ImagePanel("src/main/java/GUI/Icons/wood_background.jpg");
        showOthersPlayerPanel(player, players);

        int colIndex = 0;

        playerPane.setLayout(new GridBagLayout());
        playerPane.setBorder(BorderFactory.createEmptyBorder(0, 30, 10, 5));

        JLabel playerName = new JLabel("Player " + player.getName());
        playerName.setPreferredSize(new Dimension(310, 50));
        playerName.setFont(new Font("Serif", Font.BOLD, 30));
        playerPane.add(playerName, new GridBagConstraints(0, colIndex, 1, 1, 1.0, 0.0, GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 10, 0), 0, 0));
        colIndex++;

        JSeparator separator_first = new JSeparator(SwingConstants.HORIZONTAL);
        separator_first.setPreferredSize(new Dimension(1, 8));
        separator_first.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.black));
        playerPane.add(separator_first, new GridBagConstraints(0, colIndex, 1, 1, 1.0, 0.0, NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 0), 0, 0));
        colIndex++;

        if(player.hasTile()) {

            JPanel playerTilePanel = new JPanel();
            playerTilePanel.setLayout(new GridBagLayout());
            playerTilePanel.setMaximumSize(new Dimension(30,150));

            JLabel lastTileLabel = new JLabel("Top tile: ");
            lastTileLabel.setPreferredSize(new Dimension(130, 50));
            lastTileLabel.setFont(new Font("Serif", Font.PLAIN, 20));
            playerTilePanel.add(lastTileLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, WEST, GridBagConstraints.NONE, new Insets(0, 0, 10, 0), 0, 0));

            JLabel lastPlayerTile = new JLabel(getTileIcon(player.getLastPickedTile().getNumber(), 50, 40));
            lastPlayerTile.setPreferredSize(new Dimension(80, 90));
            playerTilePanel.add(lastPlayerTile, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 10, 0), 0, 0));
            playerPane.add(playerTilePanel, new GridBagConstraints(0, colIndex, 1, 1, 0.0, 0.0, NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 10, 0), 0, 0));
            colIndex++;
        }

        if(!dice.getChosenDice().isEmpty()) {
            JLabel chosenDiceLabel = new JLabel("Chosen dice:");
            chosenDiceLabel.setPreferredSize(new Dimension(200, 50));
            chosenDiceLabel.setFont(new Font("Serif", Font.PLAIN, 20));
            playerPane.add(chosenDiceLabel, new GridBagConstraints(0, colIndex, 1, 1, 1.0, 1.0, WEST, NONE, new Insets(0, 0, 0, 0), 0, 0));
            colIndex++;
        }

        JPanel playerDicePanel = new JPanel(new GridBagLayout());
        //playerDicePanel.setLayout(new GridBagLayout());
        playerDicePanel.setMaximumSize(new Dimension(250,150));

        int diceRowIndex = 0;
        for(Die die : dice.getChosenDice()){
            JLabel dieIconLabel = new JLabel(getDieIcon(die.getDieFace(),40));
            dieIconLabel.setPreferredSize(new Dimension(50,50));

            if(diceRowIndex < 4){
                playerDicePanel.add(dieIconLabel, new GridBagConstraints(diceRowIndex, 0, 1, 1, 1.0, 0.0, WEST, NONE, new Insets(0, 0, 0, 0), 0, 0 ));
            }else{
                playerDicePanel.add(dieIconLabel, new GridBagConstraints(diceRowIndex - 4, 1, 1, 1, 1.0, 0.0, WEST, NONE, new Insets(0, 0, 0, 0), 0, 0 ));
            }
            diceRowIndex++;
        }

        playerPane.add(playerDicePanel, new GridBagConstraints(0, colIndex, 1, 1, 1.0, 1.0, WEST, NONE, new Insets(0, 0, 0, 0), 0, 0 ));

        colIndex++;
        JSeparator separator_up = new JSeparator(SwingConstants.HORIZONTAL);
        separator_up.setPreferredSize(new Dimension(1, 8));
        separator_up.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.black));
        playerPane.add(separator_up, new GridBagConstraints(0, colIndex, 1, 1, 1.0, 0.0, NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 0), 0, 0));
        colIndex++;

        JLabel score = new JLabel("Current score: " + dice.getScore());
        score.setPreferredSize(new Dimension(200, 50));
        score.setFont(new Font("Serif", Font.PLAIN, 25));
        score.setForeground(Color.red);
        playerPane.add(score, new GridBagConstraints(0, colIndex, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 40, 0), 0, 0));

        frame.add(playerPane, BorderLayout.WEST);
        //frame.revalidate();
        //frame.repaint();
        //frame.setVisible(true);
    }

    private void showOthersPlayerPanel(Player player, Player[] players) {
        frame.getContentPane().remove(othersPlayerPane);
        othersPlayerPane = new JPanel();
        othersPlayerPane.setLayout(new GridBagLayout());
        othersPlayerPane.setPreferredSize(new Dimension(230, 200));
        othersPlayerPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 20));
        int index = 0;
        for (Player otherPlayer : players) {
            if (!player.equals(otherPlayer)) {
                if (otherPlayer.hasTile()) {
                    JLabel otherPlayerLabel = new JLabel("<html><b>" + otherPlayer.getName() + "</b>'s last tile: <br> Total worms: "+ otherPlayer.getWormScore() + " </html>");
                    otherPlayerLabel.setFont(new Font("Serif", Font.PLAIN, 15));
                    othersPlayerPane.add(otherPlayerLabel, new GridBagConstraints(0, index, 1, 1, 0.0, 1.0, GridBagConstraints.PAGE_START, GridBagConstraints.BOTH, new Insets(0, 20, 20, 5), 0, 0));

                    JLabel otherPlayerLastTile = new JLabel(getTileIcon(otherPlayer.getLastPickedTile().getNumber(), 60,50));
                    otherPlayerLastTile.setPreferredSize(new Dimension(10, 80));
                    othersPlayerPane.add(otherPlayerLastTile, new GridBagConstraints(1, index, 1, 1, 1.0, 0.0, GridBagConstraints.PAGE_START, GridBagConstraints.BOTH, new Insets(0, 0, 20, 0), 0, 0));

                } else {
                    JLabel otherPlayerLabel = new JLabel("<html> <b>" + otherPlayer.getName() + "</b> : no tiles </html>");
                    otherPlayerLabel.setFont(new Font("Serif", Font.PLAIN, 15));
                    othersPlayerPane.add(otherPlayerLabel, new GridBagConstraints(0, index, 1, 1, 0.0, 1.0, GridBagConstraints.PAGE_START, GridBagConstraints.BOTH, new Insets(0, 0, 20, 0), 0, 0));

                }
                index++;
            }
        }

        frame.add(othersPlayerPane, BorderLayout.EAST);
        //frame.setVisible(true);

    }

    @Override
    public Die.Face chooseDie(Dice dice) {
        frame.getContentPane().remove(dicePanel);
        chosenFace = null;

        dicePanel(dice);

        Timer timer = new Timer(100, new ActionListener() {
            private int rollCount = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (rollCount < 10) {
                    for (Component component : dicePanel.getComponents()) {
                        if (component instanceof JToggleButton dieButton) {
                            Die.Face randomFace = Die.generateDie().getDieFace();
                            dieButton.setIcon(getDieIcon(randomFace, 65));
                            dieButton.setSelectedIcon(getDieIcon(randomFace, 65));
                        }
                    }
                    dicePanel.repaint();
                    rollCount++;
                } else {
                    ((Timer) e.getSource()).stop();
                    for (Component component : dicePanel.getComponents()) {
                        if (component instanceof JToggleButton dieButton) {
                            Die.Face originalFace = (Die.Face) dieButton.getClientProperty("originalFace");
                            dieButton.setIcon(getDieIcon(originalFace, 65));
                            dieButton.setSelectedIcon(getDieIcon(originalFace, 65));
                        }
                    }
                    dicePanel.repaint();
                }
            }
        });

        timer.start();

        while (chosenFace == null) {
            Thread.onSpinWait();
        }

        return chosenFace;
    }

    private void dicePanel(Dice dice) {

        frame.getContentPane().remove(dicePanel);
        dicePanel = new JPanel(new GridBagLayout());

        dicePanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 50, 5));
        dicePanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 20, 30, 20); // Add spacing between components
        gbc.anchor = NORTH; // Center the panel
        gbc.fill = GridBagConstraints.NONE; // Don't fill the available space

        for (Die die : dice.getDiceList()) {
            JToggleButton dieButton = new JToggleButton();
            Die.Face dieFace = die.getDieFace();
            dieButton.setIcon(getDieIcon(dieFace, 65));
            dieButton.setSelectedIcon(getDieIcon(dieFace, 65));
            dieButton.addActionListener(e -> chosenFace = dieFace);
            dieButton.setPreferredSize(new Dimension(60, 60));

            dieButton.setBorder(null);
            dieButton.putClientProperty("originalFace", dieFace); // Memorizza l'immagine originale del dado
            dicePanel.add(dieButton, gbc);

            gbc.gridx++;
            if (gbc.gridx > 3) {
                gbc.gridx = 0;
                gbc.gridy++;
            }
        }

        frame.getContentPane().add(dicePanel, BorderLayout.CENTER);
        frame.revalidate();
        //frame.repaint();
    }
    //frame.setVisible(true);



    @Override
    public void showBustMessage() {
        printMessage("BUUUUUSTTTT!!!!");
    }

    @Override
    public String getInputString() {
        return null;
    }

    private ImageIcon getTileIcon(int tileNumber, int height, int width){
        ImageIcon icon = new ImageIcon(tileNumberToIconPath.get(tileNumber));
        Image img = icon.getImage() ;
        Image newimg = img.getScaledInstance(width , height,  java.awt.Image.SCALE_SMOOTH ) ;
        return new ImageIcon( newimg );
    }

    private ImageIcon getDieIcon(Die.Face face, int size){
        ImageIcon icon = new ImageIcon(faceToIconPath.get(face));
        Image img = icon.getImage() ;
        Image newimg = img.getScaledInstance(size , size,  java.awt.Image.SCALE_SMOOTH ) ;
        return new ImageIcon( newimg );
    }
}
