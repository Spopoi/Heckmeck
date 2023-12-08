package GUI.Panels;

import GUI.GUIIOHandler;
import Heckmeck.Dice;
import Heckmeck.Die;
import Heckmeck.Player;

import javax.swing.*;
import java.awt.*;

import static GUI.GUIIOHandler.getTileIcon;
import static java.awt.GridBagConstraints.*;

public class PlayerDataPanel extends ImagePanel{

    private JLabel playerName;
    private JPanel playerDicePanel;
    private JLabel score;
    private JPanel playerTilePanel;
    private JLabel lastPlayerTile;

    public PlayerDataPanel(String imagePath) {
        super(imagePath);
        init();
    }

    private void init() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));

        playerName = new JLabel();
        playerName.setPreferredSize(new Dimension(300, 50));
        playerName.setFont(new Font("Serif", Font.BOLD, 30));
        playerName.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(playerName);

        JSeparator separator_first = new JSeparator(SwingConstants.HORIZONTAL);
        separator_first.setPreferredSize(new Dimension(300, 5)); // Imposta la dimensione preferita
        separator_first.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0)); // Imposta il margine
        separator_first.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(separator_first);

        playerTilePanel = new JPanel();
        playerTilePanel.setLayout(new BoxLayout(playerTilePanel, BoxLayout.X_AXIS));
        playerTilePanel.setPreferredSize(new Dimension(300,100));
        playerTilePanel.setAlignmentX(LEFT_ALIGNMENT);
        playerTilePanel.setOpaque(false);
        initTilePanel(-1);
        add(playerTilePanel);

        add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel chosenDiceLabel = new JLabel("Chosen dice:");
        chosenDiceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        chosenDiceLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        add(chosenDiceLabel);

        add(Box.createRigidArea(new Dimension(0, 10)));

        playerDicePanel = new JPanel();
        playerDicePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        playerDicePanel.setPreferredSize(new Dimension(300,120));
        playerDicePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        playerDicePanel.setOpaque(false);
        add(playerDicePanel);

        JSeparator separator_up = new JSeparator(SwingConstants.HORIZONTAL);
        separator_up.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.black));
        separator_up.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(separator_up, BOTTOM_ALIGNMENT);

        score = new JLabel();
        score.setPreferredSize(new Dimension(300, 40));
        score.setFont(new Font("Serif", Font.PLAIN, 25));
        score.setForeground(Color.red);
        score.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(score);
    }


    //TODO: Valutare un ulteriore refactoring
    private void initTilePanel(int tileNumber) {

        playerTilePanel.removeAll();
        JLabel lastTileLabel = new JLabel("Top tile: ");
        lastTileLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        playerTilePanel.add(lastTileLabel);

        playerTilePanel.add(Box.createRigidArea(new Dimension(100, 0)));

        lastPlayerTile = new JLabel();
        if(tileNumber != -1) lastPlayerTile.setIcon(getTileIcon(tileNumber, 50, 40));
        else lastPlayerTile.setText("No tiles");
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

    //TODO: ridimensionare
    private void updateDicePanel(Dice dice) {
        playerDicePanel.removeAll();
        for(Die die : dice.getChosenDice()){
            JLabel dieIconLabel = new JLabel(GUIIOHandler.getDieIcon(die.getDieFace(),50));
            dieIconLabel.setPreferredSize(new Dimension(50,55));
            playerDicePanel.add(dieIconLabel);
            playerDicePanel.add(Box.createRigidArea(new Dimension(13,0)));
        }
    }
}


