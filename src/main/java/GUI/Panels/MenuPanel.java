package GUI.Panels;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import static GUI.HeckmeckGUI.*;

public class MenuPanel extends ImagePanel {

    //TODO: manage path
    private static final String HECKMECK_BACKGROUND_PATH = "src/main/resources/GUI/heckmeckBackground.jpg";
    private static final Font buttonFont = new Font(UIManager.getFont("Button.font").getName(), Font.BOLD, 14);
    private static final int BUTTON_PANEL_TOP_INSET = 180;
    private static final Dimension BUTTON_PANEL_DIMENSIONS = new Dimension(350, 250);
    private static final int NUMBER_OF_BUTTONS = 4;
    private static final int VERTICAL_BUTTONS_GAP = 10;
    private static final Color BUTTON_COLOR = Color.ORANGE;

    public MenuPanel() {
        super(HECKMECK_BACKGROUND_PATH);
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

    private JButton createButton(String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.addActionListener(actionListener);
        button.setBackground(BUTTON_COLOR);
        button.setFont(buttonFont);
        return button;
    }
}
