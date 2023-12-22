package GUI.Panels;

import Heckmeck.Player;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static GUI.GUIIOHandler.textFont;
import static GUI.GUIIOHandler.titleFont;
import static GUI.HeckmeckGUI.BACKGROUND_IMAGE_PATH;
import static Heckmeck.FileReader.getTileIcon;

public class ScoreboardPanel extends ImagePanel {
    public ScoreboardPanel(Player player, Player[] players){

        super(BACKGROUND_IMAGE_PATH);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(new Insets(10,10,10,10)));

        JLabel scoreboardLabel = new JLabel("Scoreboard");
        scoreboardLabel.setPreferredSize(new Dimension(220, 30));
        scoreboardLabel.setFont(titleFont);
        scoreboardLabel.setAlignmentX(CENTER_ALIGNMENT);
        add(scoreboardLabel);
        add(Box.createVerticalStrut(10));
        add(makeHorizontalSeparator());

        for (Player otherPlayer : players) {
            if (!player.equals(otherPlayer)) {
                add(makeOtherPlayerPanel(otherPlayer));
                add(makeHorizontalSeparator());
            }
        }
    }


    //TODO: refactoring Dimensions... estrarre e usare PlayerDataPanel.getDimensions()
    private JPanel makeOtherPlayerPanel(Player otherPlayer) {
        JPanel playerPanel = new JPanel();
        playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.Y_AXIS));
        playerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        playerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        playerPanel.setOpaque(false);

        JLabel playerName = new JLabel(otherPlayer.getName());
        playerName.setPreferredSize(new Dimension(220, 30));
        playerName.setFont(textFont);
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
        scoreLabel.setFont(textFont);
        scoreLabel.setForeground(Color.RED);
        scoreLabel.setAlignmentX(CENTER_ALIGNMENT);
        playerPanel.add(scoreLabel);

        return playerPanel;
    }

    private static JSeparator makeHorizontalSeparator() {
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        separator.setMaximumSize(new Dimension(220,15));
        separator.setAlignmentX(Component.CENTER_ALIGNMENT);
        return separator;
    }
}
