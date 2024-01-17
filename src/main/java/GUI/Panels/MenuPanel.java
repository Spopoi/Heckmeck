package GUI.Panels;
import javax.swing.*;
import java.awt.*;

import static GUI.HeckmeckGUI.*;
import static Utils.GUI.ButtonHandler.createButton;

public class MenuPanel extends ImagePanel {
    private static final int BUTTON_PANEL_TOP_INSET = 180;
    private static final Dimension BUTTON_PANEL_DIMENSIONS = new Dimension(350, 250);
    private static final int NUMBER_OF_BUTTONS = 4;
    private static final int VERTICAL_BUTTONS_GAP = 10;

    public MenuPanel(String backgroundPath) {
        super(backgroundPath);
        setLayout(new GridBagLayout());
        JPanel buttonsPanel = createButtonsPanel();
        add(buttonsPanel, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(BUTTON_PANEL_TOP_INSET, 0, 0, 0), 0, 0));
    }

    private JPanel createButtonsPanel() {
        JPanel buttonsPanel = new JPanel(new GridLayout(NUMBER_OF_BUTTONS, 1, 0, VERTICAL_BUTTONS_GAP));
        buttonsPanel.setPreferredSize(BUTTON_PANEL_DIMENSIONS);
        buttonsPanel.setOpaque(false);

        JButton playButton = createButton("PLAY", e -> switchToGamePanel());
        buttonsPanel.add(playButton);

        JButton multiplayerButton = createButton("MULTIPLAYER", e -> switchToMultiplayerPanel());
        buttonsPanel.add(multiplayerButton);

        JButton rulesButton = createButton("RULES", e -> switchToRulesPanel());
        buttonsPanel.add(rulesButton);

        JButton settingsButton = createButton("SETTINGS", e -> switchToSettingsPanel());
        buttonsPanel.add(settingsButton);

        return buttonsPanel;
    }

}
