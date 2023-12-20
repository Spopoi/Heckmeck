package GUI.Panels;

import Heckmeck.Player;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static GUI.HeckmeckGUI.BACKGROUND_IMAGE_PATH;
import static Heckmeck.FileReader.getTileIcon;

public class ScoreboardPanel extends ImagePanel {
    public ScoreboardPanel(Player player, Player[] players){

        super(BACKGROUND_IMAGE_PATH);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(new Insets(10,10,10,10)));

        JLabel scoreboardLabel = new JLabel("Scoreboard");
        scoreboardLabel.setPreferredSize(new Dimension(220, 30));
        scoreboardLabel.setFont(new Font("Serif", Font.BOLD, 30));
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

    private static JSeparator makeHorizontalSeparator() {
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        separator.setMaximumSize(new Dimension(220,15));
        separator.setAlignmentX(Component.CENTER_ALIGNMENT);
        return separator;
    }
}
