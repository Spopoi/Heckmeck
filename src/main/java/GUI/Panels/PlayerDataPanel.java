package GUI.Panels;

import Heckmeck.Components.Dice;
import Heckmeck.Components.Die;
import Heckmeck.Components.Player;

import javax.swing.*;
import java.awt.*;

import static Utils.GUI.IconHandler.getPlayerTileIcon;
import static Utils.GUI.LabelHandler.*;


public class PlayerDataPanel extends RoundedPanel{
    private JLabel playerName;
    private JPanel playerDicePanel;
    private JLabel score;
    private JPanel playerTilePanel;
    private JLabel playerWormScore;
    public static final Component verticalSpace = Box.createVerticalStrut(10);
    private static final int LABEL_HEIGHT = 45;

    public PlayerDataPanel(String imagePath) {
        super(imagePath);
        init();
    }

    private void init() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));

        initPlayerNameLabel();
        add(verticalSpace);
        addHorizontalSeparator();

        initPlayerWormScore();
        add(verticalSpace);

        initPlayerTilePanel();
        add(verticalSpace);

        initPlayerDicePanel();
        add(verticalSpace);

        addHorizontalSeparator();
        initScoreLabel();
    }

    private void initPlayerWormScore() {
        playerWormScore = getLabel(0 , LABEL_HEIGHT);
        playerWormScore.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(playerWormScore);
    }

    private void addHorizontalSeparator() {
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setPreferredSize(getDimensions(5));
        separator.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        separator.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(separator);
    }

    private void initScoreLabel() {
        score = getTitleLabel(0, LABEL_HEIGHT);
        score.setForeground(Color.red);
        score.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(score);
    }

    private void initPlayerDicePanel() {
        JLabel chosenDiceLabel = new JLabel("Chosen dice:");
        chosenDiceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        chosenDiceLabel.setFont(textFont);
        add(chosenDiceLabel);

        add(Box.createRigidArea(getDimensions(5)));

        playerDicePanel = new JPanel();
        playerDicePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        playerDicePanel.setPreferredSize(getDimensions(120));
        playerDicePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        playerDicePanel.setOpaque(false);
        add(playerDicePanel);
        add(Box.createRigidArea(getDimensions(5)));
    }

    private void initPlayerTilePanel() {
        playerTilePanel = new JPanel();
        playerTilePanel.setLayout(new BoxLayout(playerTilePanel, BoxLayout.X_AXIS));
        playerTilePanel.setPreferredSize(getDimensions(90));
        playerTilePanel.setAlignmentX(LEFT_ALIGNMENT);
        playerTilePanel.setOpaque(false);
        initTilePanel(-1);
        add(playerTilePanel);
        add(Box.createRigidArea(getDimensions(5)));
    }

    private void initPlayerNameLabel() {
        playerName = getTitleLabel(0,LABEL_HEIGHT);
        playerName.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(playerName);
    }

    private void initTilePanel(int tileNumber) {
        playerTilePanel.removeAll();
        JLabel lastTileLabel = new JLabel("Top tile: ");
        lastTileLabel.setFont(textFont);
        playerTilePanel.add(lastTileLabel);
        playerTilePanel.add(Box.createRigidArea(new Dimension(80,0)));
        //TODO: refactoring
        if(tileNumber != -1) playerTilePanel.add(getPlayerTileIcon(tileNumber));
    }

    public void update(Player player, Dice dice){
        playerName.setText("Player " + player.getName());
        playerWormScore.setText("Worm score: " + player.getWormScore());
        updateDicePanel(dice);
        score.setText("Current score: " + dice.getScore());
        if(player.hasTile()){
            initTilePanel(player.getLastPickedTile().number());
        }else{
            initTilePanel(-1);
        }
    }

    private void updateDicePanel(Dice dice) {
        playerDicePanel.removeAll();
        for(Die die : dice.getChosenDice()){
            JLabel dieIconLabel = getChosenDieLabel(die.getDieFace());
            playerDicePanel.add(dieIconLabel);
        }
    }

    private Dimension getDimensions(int height){
        return new Dimension(0,height);
    }
}


