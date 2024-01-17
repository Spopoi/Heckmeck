package GUI.Panels;
import Utils.PropertiesManager;

import javax.swing.*;
import java.awt.*;

import static GUI.HeckmeckGUI.*;
import static Utils.GUI.ButtonHandler.createButton;

public class MenuPanel extends ImagePanel {
    private static final int BUTTON_PANEL_TOP_INSET = 180;
    private static final Dimension BUTTON_PANEL_DIMENSIONS = new Dimension(350, 250);
    private static final int NUMBER_OF_BUTTONS = 4;
    private static final int VERTICAL_BUTTONS_GAP = 10;
    private static PropertiesManager propertiesManager;

    public MenuPanel(String backgroundPath) {
        super(backgroundPath);
        setLayout(new GridBagLayout());
        propertiesManager = getPropertiesManager();
        JPanel buttonsPanel = createButtonsPanel();
        add(buttonsPanel, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(BUTTON_PANEL_TOP_INSET, 0, 0, 0), 0, 0));
    }

    private JPanel createButtonsPanel() {
        JPanel buttonsPanel = new JPanel(new GridLayout(NUMBER_OF_BUTTONS, 1, 0, VERTICAL_BUTTONS_GAP));
        buttonsPanel.setPreferredSize(BUTTON_PANEL_DIMENSIONS);
        buttonsPanel.setOpaque(false);

        String playButtonText = propertiesManager.getMessage("playButton");
        JButton playButton = createButton(playButtonText, e -> switchToGamePanel());
        buttonsPanel.add(playButton);

        String multiplayerButtonText = propertiesManager.getMessage("multiplayerButton");
        JButton multiplayerButton = createButton(multiplayerButtonText, e -> switchToMultiplayerPanel());
        buttonsPanel.add(multiplayerButton);

        String rulesButtonText = propertiesManager.getMessage("rulesButton");
        JButton rulesButton = createButton(rulesButtonText, e -> switchToRulesPanel());
        buttonsPanel.add(rulesButton);

        String settingsButtonText = propertiesManager.getMessage("settingsButton");
        JButton settingsButton = createButton(settingsButtonText, e -> switchToSettingsPanel());
        buttonsPanel.add(settingsButton);

        return buttonsPanel;
    }

}
