package GUI;

import Heckmeck.*;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;

import static java.awt.GridBagConstraints.*;
import static javax.swing.JOptionPane.*;
import static javax.swing.JOptionPane.showMessageDialog;

public class GUIOutputHandler implements OutputHandler{

    private JFrame frame;
    private JPanel playerPane;
    private JPanel dicePanel;
    private JPanel othersPlayerPane;
    private JPanel tilesPanel;
    private JPanel messagePanel;

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
    public GUIOutputHandler(JFrame frame){
        this.frame =frame;
        this.playerPane = new JPanel();
        this.dicePanel = new JPanel();
        this.othersPlayerPane = new JPanel();
        this.tilesPanel = new JPanel();
        this.messagePanel = new JPanel();
        //frame.getContentPane().add(playerPane,BorderLayout.WEST);
        frame.setVisible(true);
    }
    @Override
    public void showWelcomeMessage(){
        showMessageDialog(null, "WELCOME to Heckmeck!");
        //messagePanel.setLayout(new GridLayout(1,0,50, 0));
//        messagePanel.setPreferredSize(new Dimension(0,100));
//        JLabel outputLabel = new JLabel("Welcome to Heckmeck!");
//        messagePanel.add(outputLabel);
//        frame.add(messagePanel, BorderLayout.SOUTH);
//        frame.setVisible(true);
    }

    @Override
    public void showWantToPick() {

    }

    @Override
    public void printMessage(String message){
        showMessageDialog(null, message);
    }

    @Override
    public void showTurnBeginConfirm(String playerName){
        frame.getContentPane().removeAll();
        frame.repaint();
        showMessageDialog(null, playerName + " turn, press ok for starting: ");
    }

    @Override
    public void showTiles(BoardTiles boardTiles){

        tilesPanel = new JPanel();
        tilesPanel.setLayout(new GridLayout(1,0,10, 10));
        tilesPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        tilesPanel.setPreferredSize(new Dimension(0,125));

        for(Tile tile : boardTiles.getTiles()){
            JLabel tileIcon = new JLabel(getTileIcon(tile.getNumber()));
            tileIcon.setPreferredSize(new Dimension(80,90));
            tilesPanel.add(tileIcon);

//            String tileText = tile.getNumber() + System.lineSeparator() + tile.getWormString();
//            JButton tileButton = new JButton("<html>" + tileText.replaceAll(System.lineSeparator(), "<br>") + "</html>");
//            tileButton.setPreferredSize(new Dimension(80,90));
//            //tileButton.setMaximumSize(new Dimension(20,50));
//            tilesPanel.add(tileButton);
        }
        frame.getContentPane().add(tilesPanel,BorderLayout.NORTH);
        frame.setVisible(true);
        //frame.getContentPane().add(boardTilesLayout(boardTiles), BorderLayout.CENTER);
    }


    //Todo: using gridBagLayout instead of GridLayout for formatting better
    @Override
    public void showDice(Dice dice){

        frame.getContentPane().remove(dicePanel);
        //frame.getContentPane().repaint();

        dicePanel = new JPanel();
        dicePanel.setLayout(new GridLayout(2,4,20, 20));
        dicePanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 50, 50));


        for(Die die : dice.getDiceList()){
            JButton dieButton = new JButton(getDieIcon(die.getDieFace()));
            dieButton.setPreferredSize(new Dimension(60,60));
            dicePanel.add(dieButton, CENTER_ALIGNMENT);
        }

        frame.getContentPane().add(dicePanel,BorderLayout.CENTER);
        frame.setVisible(true);
    }

    @Override
    public void showPlayerData(Player player, Dice dice, Player[] players){

        frame.getContentPane().remove(playerPane);

        showOthersPlayerPanel(player, players);

        playerPane = new JPanel();
        playerPane.setLayout(new BoxLayout(playerPane, BoxLayout.Y_AXIS));
        playerPane.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 5));
        //playerPane.add(Box.createHorizontalGlue());

        JLabel playerName = new JLabel(player.getName());
        playerName.setPreferredSize(new Dimension(300,50));
        playerName.setFont(new Font("Serif", Font.BOLD, 30));
        playerName.setBackground(Color.LIGHT_GRAY);
        playerPane.add(playerName);
        //playerPane.add(Box.createVerticalGlue());
        //playerPane.add(Box.createRigidArea(new Dimension(10, 20)));


        JLabel score = new JLabel("Your score: " + dice.getScore());
        score.setPreferredSize(new Dimension(200,50));
        score.setFont(new Font("Serif", Font.PLAIN, 20));
        playerPane.add(score);
        //playerPane.add(Box.createVerticalGlue());
        // playerPane.add(Box.createRigidArea(new Dimension(10, 20)));

        JPanel dicePanel = new JPanel();
        dicePanel.setLayout(new GridBagLayout());
        dicePanel.setMaximumSize(new Dimension(300,150));

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

        playerPane.add(dicePanel);

        frame.add(playerPane, BorderLayout.WEST);
        frame.setVisible(true);
    }

    private void showOthersPlayerPanel(Player player, Player[] players) {
        frame.getContentPane().remove(othersPlayerPane);
        othersPlayerPane = new JPanel();
        othersPlayerPane.setLayout(new BoxLayout(othersPlayerPane, BoxLayout.Y_AXIS));
        othersPlayerPane.setPreferredSize(new Dimension(100,0));

        for(Player otherPlayer : players){
            if(!player.equals(otherPlayer)){
                if(otherPlayer.hasTile()){
                    JLabel otherPlayerLabel = new JLabel(otherPlayer.getName() + "Tile: ");
                    othersPlayerPane.add(otherPlayerLabel);
                    JLabel otherPlayerLastTile = new JLabel(getTileIcon(otherPlayer.getLastPickedTile().getNumber()));
                    //otherPlayerLastTile.setPreferredSize(new Dimension(20,30));
                    othersPlayerPane.add(otherPlayerLastTile);
                    othersPlayerPane.add(Box.createVerticalGlue());
                }
                else{
                    JLabel otherPlayerLabel = new JLabel("<html>"+otherPlayer.getName() + "<br> No Tiles </html>");
                    othersPlayerPane.add(otherPlayerLabel);
                    othersPlayerPane.add(Box.createVerticalGlue());
                }
            }
        }
        othersPlayerPane.setAlignmentX(CENTER);

        frame.add(othersPlayerPane, BorderLayout.EAST);
        frame.setVisible(true);
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

    @Override
    public void showBustMessage() {
        showMessageDialog(null, "BUUUUSSSSTTTTTT!!!!");
    }
    @Override
    public void showWantToSteal(Player robbedPlayer) {

    }

    @Override
    public void showWantToPlay() {

    }

}
