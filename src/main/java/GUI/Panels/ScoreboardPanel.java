package GUI.Panels;

import Heckmeck.Components.Player;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static GUI.HeckmeckGUI.BACKGROUND_IMAGE_PATH;
import static GUI.Panels.PlayerDataPanel.verticalSpace;
import static Utils.GUI.IconHandler.getPlayerTileIcon;
import static Utils.GUI.LabelHandler.*;

public class ScoreboardPanel extends ImagePanel {
    private static final int SCOREBOARD_WIDTH_PANEL = 220;
    public ScoreboardPanel(Player player, Player[] players){

        super(BACKGROUND_IMAGE_PATH);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(new Insets(10,10,10,10)));

        JLabel scoreboardLabel = getTitleLabel("Scoreboard", SCOREBOARD_WIDTH_PANEL, 30);
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

        JLabel playerName = getLabel(otherPlayer.getName(), SCOREBOARD_WIDTH_PANEL, 100);
        playerName.setAlignmentX(CENTER_ALIGNMENT);
        playerPanel.add(playerName);
        playerPanel.add(verticalSpace);

        JLabel lastPlayerTile = new JLabel();
        lastPlayerTile.setAlignmentX(CENTER_ALIGNMENT);
        if(otherPlayer.hasTile()){
            int tileNumber = otherPlayer.getLastPickedTile().getNumber();
            lastPlayerTile.setIcon(getPlayerTileIcon(tileNumber));
            playerPanel.add(lastPlayerTile);
        }
        JLabel scoreLabel = getLabel("Worm score: " + otherPlayer.getWormScore(), SCOREBOARD_WIDTH_PANEL, 30);
        scoreLabel.setForeground(Color.RED);
        scoreLabel.setAlignmentX(CENTER_ALIGNMENT);
        playerPanel.add(scoreLabel);

        return playerPanel;
    }

    //TODO: refactoring horizontal separator
    private static JSeparator makeHorizontalSeparator() {
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        separator.setMaximumSize(new Dimension(220,15));
        separator.setAlignmentX(Component.CENTER_ALIGNMENT);
        return separator;
    }
}
