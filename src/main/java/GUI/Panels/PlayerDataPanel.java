package GUI.Panels;

import Heckmeck.Components.Dice;
import Heckmeck.Components.Die;
import Heckmeck.Components.Player;
import Utils.PropertiesManager;

import javax.swing.*;
import java.awt.*;

import static Heckmeck.Launcher.getPropertiesManager;
import static Utils.GUI.IconHandler.getPlayerTileIcon;
import static Utils.GUI.LabelHandler.*;

public class PlayerDataPanel extends RoundedPanel{
    private JLabel playerName;
    private JPanel playerDicePanel;
    private JLabel score;
    private JPanel playerTilePanel;
    private JLabel playerWormScore;
    private static final int PLAYERTILE_GAP = 80;
    private static final int TILEPANEL_HEIGHT = 70;
    private static final int SIDE_BORDER = 20;
    private static final int BOTTOM_BORDER = 10;
    private static final int DICE_PANEL_HEIGHT = 120;
    private RoundedPanel playerTile;

    private final PropertiesManager propertiesManager;

    public PlayerDataPanel(String imagePath) {
        super(imagePath);
        propertiesManager = getPropertiesManager();
        init();
    }

    private void init() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(0, SIDE_BORDER, BOTTOM_BORDER, SIDE_BORDER));

        playerName = getTitleLabel("", JLabel.LEFT);
        add(playerName);
        addHorizontalSeparator();

        playerWormScore = getLabel("",JLabel.LEFT);
        add(playerWormScore);

        initPlayerTilePanel();
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
        score = getTitleLabel("", JLabel.LEFT);
        score.setForeground(Color.red);
        add(score);
    }

    private void initPlayerDicePanel() {
        String chosenDice = propertiesManager.getMessage("chosenDice");
        JLabel chosenDiceLabel = getLabel(chosenDice, JLabel.LEFT);
        add(chosenDiceLabel);

        playerDicePanel = new JPanel();
        playerDicePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        playerDicePanel.setPreferredSize(getDimensions(DICE_PANEL_HEIGHT));
        playerDicePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        playerDicePanel.setOpaque(false);
        add(playerDicePanel);
    }

    private void initPlayerTilePanel() {
        playerTilePanel = new JPanel();
        playerTilePanel.setLayout(new BoxLayout(playerTilePanel, BoxLayout.X_AXIS));
        playerTilePanel.setPreferredSize(getDimensions(TILEPANEL_HEIGHT));
        playerTilePanel.setAlignmentX(LEFT_ALIGNMENT);
        playerTilePanel.setOpaque(false);

        String topTile = propertiesManager.getMessage("topTile");
        JLabel lastTileLabel = getLabel(topTile, JLabel.LEFT);
        playerTilePanel.add(lastTileLabel);
        playerTilePanel.add(Box.createRigidArea(new Dimension(PLAYERTILE_GAP, 0)));
        add(playerTilePanel);
    }

    public void update(Player player, Dice dice) {
        String playerText = propertiesManager.getMessage("player");
        playerName.setText(playerText + player.getName());
        String wormScore = propertiesManager.getMessage("wormScore");
        playerWormScore.setText(wormScore + player.getWormScore());
        String currentScore = propertiesManager.getMessage("currentScore");
        score.setText(currentScore + dice.getScore());
        updateDicePanel(dice);
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

    private Dimension getDimensions(int height){
        return new Dimension(0,height);
    }
}