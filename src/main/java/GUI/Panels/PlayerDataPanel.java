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
    private static final int PLAYERTILE_GAP = 80;
    private static final int TILEPANEL_HEIGHT = 90;
    private RoundedPanel playerTile;

    public PlayerDataPanel(String imagePath) {
        super(imagePath);
        init();
    }

    private void init() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));

        playerName = getTitleLabel("", JLabel.LEFT);
        add(playerName);
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

    //TODO: Prop
    private void initPlayerDicePanel() {
        JLabel chosenDiceLabel = getLabel("Chosen dice:", JLabel.LEFT);
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
        playerTilePanel.setPreferredSize(getDimensions(TILEPANEL_HEIGHT));
        playerTilePanel.setAlignmentX(LEFT_ALIGNMENT);
        playerTilePanel.setOpaque(false);

        JLabel lastTileLabel = getLabel("Top tile: ", JLabel.LEFT);
        playerTilePanel.add(lastTileLabel);
        playerTilePanel.add(Box.createRigidArea(new Dimension(PLAYERTILE_GAP, 0)));

        add(playerTilePanel);
        add(Box.createRigidArea(getDimensions(5)));
    }

    public void update(Player player, Dice dice) {
        playerName.setText("Player " + player.getName());
        playerWormScore.setText("Worm score: " + player.getWormScore());
        updateDicePanel(dice);
        score.setText("Current score: " + dice.getScore());
        updateTilePanel(player);

        revalidate();
        repaint();
    }

    private void updateTilePanel(Player player){
        if(playerTile != null) playerTilePanel.remove(playerTile);
        if(player.hasTile()){
            playerTile = getPlayerTileIcon(player.getLastPickedTile().number());
            playerTilePanel.add(playerTile);
        }
    }

    private void updateDicePanel(Dice dice) {
        playerDicePanel.removeAll();
        for(Die die : dice.getChosenDice()){
            JLabel dieIconLabel = getChosenDieLabel(die.getDieFace());
            playerDicePanel.add(dieIconLabel);
        }
    }

    //TODO: extract this and horizontalSeparator
    private Dimension getDimensions(int height){
        return new Dimension(0,height);
    }
}