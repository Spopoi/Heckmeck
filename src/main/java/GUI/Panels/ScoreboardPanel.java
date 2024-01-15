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
    private static final int SCOREBOARD_LABEL_HEIGHT = 30;
    private static final int PLAYERPANE_VERTICAL_SPACE = 10;
    public ScoreboardPanel(Player player, Player[] players){

        super(BACKGROUND_IMAGE_PATH);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel scoreboardLabel = getTitleLabel("Scoreboard", SCOREBOARD_WIDTH_PANEL, SCOREBOARD_LABEL_HEIGHT );
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
        playerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, PLAYERPANE_VERTICAL_SPACE, 0));
        playerPanel.setOpaque(false);

        JLabel playerName = getLabel(otherPlayer.getName(), SCOREBOARD_WIDTH_PANEL, 100);
        playerName.setAlignmentX(CENTER_ALIGNMENT);
        playerPanel.add(playerName);
        playerPanel.add(verticalSpace);

        if(otherPlayer.hasTile()){
            int tileNumber = otherPlayer.getLastPickedTile().number();
            playerPanel.add(getPlayerTileIcon(tileNumber));
        }
        JLabel scoreLabel = getScoreLabel(otherPlayer);
        playerPanel.add(scoreLabel);

        return playerPanel;
    }

    private JLabel getScoreLabel(Player otherPlayer) {
        JLabel scoreLabel = getLabel("Worm score: " + otherPlayer.getWormScore(), SCOREBOARD_WIDTH_PANEL, 30);
        scoreLabel.setForeground(Color.RED);
        scoreLabel.setAlignmentX(CENTER_ALIGNMENT);
        return scoreLabel;
    }

    public JScrollPane getScrollableScoreboard(){
        JScrollPane scrollableScoreboard = new JScrollPane(this);
        scrollableScoreboard.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        return scrollableScoreboard;
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
