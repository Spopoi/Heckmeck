package GUI.Panels;

import Heckmeck.Dice;
import Heckmeck.Die;
import Heckmeck.Player;

import javax.swing.*;
import java.awt.*;

import static GUI.GUIIOHandler.textFont;
import static GUI.GUIIOHandler.titleFont;
import static Heckmeck.FileReader.getDieIcon;
import static Heckmeck.FileReader.getTileIcon;


public class PlayerDataPanel extends ImagePanel{

    private JLabel playerName;
    private JPanel playerDicePanel;
    private JLabel score;
    private JPanel playerTilePanel;

    public PlayerDataPanel(String imagePath) {
        super(imagePath);
        init();
    }

    private void init() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));

        initPlayerNameLabel();
        add(Box.createVerticalStrut(6));
        addHorizontalSeparator();

        initPlayerTilePanel();
        add(Box.createRigidArea(getDimensions(20)));

        initPlayerDicePanel();
        addHorizontalSeparator();
        initScoreLabel();
    }

    private void addHorizontalSeparator() {
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setPreferredSize(getDimensions(5));
        separator.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        separator.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(separator);
    }

    private void initScoreLabel() {
        score = new JLabel();
        score.setPreferredSize(getDimensions(45));
        score.setFont(titleFont);
        score.setForeground(Color.red);
        score.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(score);
    }

    private void initPlayerDicePanel() {
        JLabel chosenDiceLabel = new JLabel("Chosen dice:");
        chosenDiceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        chosenDiceLabel.setFont(textFont);
        add(chosenDiceLabel);

        add(Box.createRigidArea(getDimensions(10)));

        playerDicePanel = new JPanel();
        playerDicePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        playerDicePanel.setPreferredSize(getDimensions(120));
        playerDicePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        playerDicePanel.setOpaque(false);
        add(playerDicePanel);
    }

    private void initPlayerTilePanel() {
        playerTilePanel = new JPanel();
        playerTilePanel.setLayout(new BoxLayout(playerTilePanel, BoxLayout.X_AXIS));
        playerTilePanel.setPreferredSize(getDimensions(90));
        playerTilePanel.setAlignmentX(LEFT_ALIGNMENT);
        playerTilePanel.setOpaque(false);
        initTilePanel(-1);
        add(playerTilePanel);
    }

    private void initPlayerNameLabel() {
        playerName = new JLabel();
        playerName.setPreferredSize(getDimensions(50));
        playerName.setFont(titleFont);
        playerName.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(playerName);
    }

    //TODO: refactoring tileNumber = -1
    private void initTilePanel(int tileNumber) {
        playerTilePanel.removeAll();
        JLabel lastTileLabel = new JLabel("Top tile: ");
        lastTileLabel.setFont(textFont);
        playerTilePanel.add(lastTileLabel);

        playerTilePanel.add(Box.createRigidArea(new Dimension(30,0)));

        JLabel lastPlayerTile = new JLabel();
        if(tileNumber != -1) lastPlayerTile.setIcon(getTileIcon(tileNumber, 60, 50));
        playerTilePanel.add(lastPlayerTile);
    }

    public void update(Player player, Dice dice){
        playerName.setText("Player " + player.getName());
        updateDicePanel(dice);
        score.setText("Current score: " + dice.getScore());
        if(player.hasTile()){
            initTilePanel(player.getLastPickedTile().getNumber());
        }else{
            initTilePanel(-1);
        }
    }

    private void updateDicePanel(Dice dice) {
        playerDicePanel.removeAll();
        for(Die die : dice.getChosenDice()){
            JLabel dieIconLabel = new JLabel(getDieIcon(die.getDieFace(),45));
            dieIconLabel.setPreferredSize(new Dimension(50,45));
            playerDicePanel.add(dieIconLabel);
        }
    }

    private Dimension getDimensions(int height){
        return new Dimension(0,height);
    }
}


