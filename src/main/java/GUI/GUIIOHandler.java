package GUI;

import GUI.Panels.ImagePanel;
import GUI.Panels.PlayerDataPanel;
import Heckmeck.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static javax.swing.JOptionPane.*;
import static java.awt.GridBagConstraints.*;

public class GUIIOHandler implements IOHandler {

    public static final Dimension TILE_DIMENSIONS = new Dimension(80, 90);
    //TODO: removing method's parameters and adding a Game local variable which contains players dice etc..
    private final JFrame frame;
    private PlayerDataPanel playerPane;
    private JPanel dicePanel;
    private JPanel othersPlayerPane;
    private JPanel tilesPanel;
    private JPanel messagePanel;
    private volatile Die.Face chosenFace;

    private static final Map<Die.Face, String> faceToIconPath =
            Collections.unmodifiableMap(new HashMap<>() {{
                put(Die.Face.ONE, "src/main/java/GUI/Icons/Dice/one.png");
                put(Die.Face.TWO, "src/main/java/GUI/Icons/Dice/two.png");
                put(Die.Face.THREE, "src/main/java/GUI/Icons/Dice/three.png");
                put(Die.Face.FOUR, "src/main/java/GUI/Icons/Dice/four.png");
                put(Die.Face.FIVE, "src/main/java/GUI/Icons/Dice/five.png");
                put(Die.Face.WORM, "src/main/java/GUI/Icons/Dice/worm.png");
            }});

    private static final Map<Integer, String> tileNumberToIconPath =
            Collections.unmodifiableMap(new HashMap<>() {{
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
        this.frame = frame;
        dicePanel = new JPanel();
        othersPlayerPane = new JPanel();
        tilesPanel = new JPanel();
        messagePanel = new JPanel();
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
            tileIcon.setPreferredSize(TILE_DIMENSIONS);
            tilesPanel.add(tileIcon);
        }
        frame.add(tilesPanel,BorderLayout.NORTH);
    }

    @Override
    public void askRollDiceConfirmation(String playerName){
        return;
    }

    @Override
    public void showRolledDice(Dice dice) {
        //TODO: aggiungere roll dadi
        dicePanel(dice);
    }

    // TODO: move method to the new signature
    @Override
    public boolean wantToPick(int actualDiceScore, int availableTileNumber) {
        int result = showConfirmDialog(null, "Actual score: " + actualDiceScore + "\nDo you want to pick tile " + availableTileNumber + "?");
        return result == JOptionPane.OK_OPTION;
    }

    @Override
    public boolean wantToSteal(Player robbedPlayer) {
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
        othersPlayerPane = new ImagePanel("src/main/java/GUI/Icons/table.jpg");
        othersPlayerPane.setLayout(new BoxLayout(othersPlayerPane, BoxLayout.Y_AXIS));
        othersPlayerPane.setBorder(new EmptyBorder(new Insets(10,10,10,10)));

        JLabel scoreboardLabel = new JLabel("Scoreboard");
        scoreboardLabel.setPreferredSize(new Dimension(220, 30));
        scoreboardLabel.setFont(new Font("Serif", Font.BOLD, 30));
        scoreboardLabel.setAlignmentX(CENTER_ALIGNMENT);
        othersPlayerPane.add(scoreboardLabel);
        othersPlayerPane.add(Box.createVerticalStrut(10));
        othersPlayerPane.add(makeHorizontalSeparator());

        for (Player otherPlayer : players) {
            if (!player.equals(otherPlayer)) {
                othersPlayerPane.add(makeOtherPlayerPanel(otherPlayer));
                othersPlayerPane.add(makeHorizontalSeparator());
            }
        }
        //TODO: Mettere scrollPane solo se tanti giocatori presenti
        JScrollPane scrollPane = new JScrollPane(othersPlayerPane);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        frame.add(scrollPane, BorderLayout.EAST);
        frame.revalidate();
        frame.repaint();
    }

    private static JSeparator makeHorizontalSeparator() {
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        separator.setMaximumSize(new Dimension(220,15));
        separator.setAlignmentX(Component.CENTER_ALIGNMENT);
        return separator;
    }

    private JPanel makeOtherPlayerPanel(Player otherPlayer) {
        JPanel playerPanel = new JPanel();
        playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.Y_AXIS));
        playerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        playerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        playerPanel.setOpaque(false);

        JLabel playerName = new JLabel(otherPlayer.getName());
        playerName.setPreferredSize(new Dimension(220, 30));
        playerName.setFont(new Font("Serif", Font.BOLD, 25));
        playerName.setAlignmentX(CENTER_ALIGNMENT);
        playerPanel.add(playerName);
        playerPanel.add(Box.createVerticalStrut(8));

        JLabel lastPlayerTile = new JLabel();
        lastPlayerTile.setAlignmentX(CENTER_ALIGNMENT);
        if(otherPlayer.hasTile()){
            int tileNumber = otherPlayer.getLastPickedTile().getNumber();
            lastPlayerTile.setIcon(getTileIcon(tileNumber, 60, 50));
            playerPanel.add(lastPlayerTile);
            playerPanel.add(Box.createVerticalStrut(10));
        }
        JLabel scoreLabel = new JLabel("Worm score: " + otherPlayer.getWormScore());
        scoreLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        scoreLabel.setForeground(Color.RED);
        scoreLabel.setAlignmentX(CENTER_ALIGNMENT);
        playerPanel.add(scoreLabel);

        return playerPanel;
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
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        printMessage("BUUUUUSTTTT!!!!");
    }

    @Override
    public String getInputString() {
        return null;
    }

    public static ImageIcon getTileIcon(int tileNumber, int height, int width){
        ImageIcon icon = new ImageIcon(tileNumberToIconPath.get(tileNumber));
        Image img = icon.getImage() ;
        Image newimg = img.getScaledInstance(width , height,  java.awt.Image.SCALE_SMOOTH ) ;
        return new ImageIcon( newimg );
    }
//TODO: vedere se spostare
    public static ImageIcon getDieIcon(Die.Face face, int size){
        ImageIcon icon = new ImageIcon(faceToIconPath.get(face));
        Image img = icon.getImage() ;
        Image newimg = img.getScaledInstance(size , size,  java.awt.Image.SCALE_SMOOTH ) ;
        return new ImageIcon( newimg );
    }
}
