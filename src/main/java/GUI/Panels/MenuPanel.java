package GUI.Panels;

import com.formdev.flatlaf.FlatIntelliJLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import static GUI.HeckmeckGUI.*;

public class MenuPanel extends ImagePanel {

    private final Font buttonFont = new Font(UIManager.getFont("Button.font").getName(), Font.BOLD, 14);

    public MenuPanel() {

        super("src/main/java/GUI/Icons/heckmeckBackground.jpg");
        setLayout(new GridBagLayout());

        JPanel buttonsPanel = createButtonsPanel();
        add(buttonsPanel, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(180, 0, 0, 0), 0, 0));
    }

    private JPanel createButtonsPanel() {
        JPanel buttonsPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        buttonsPanel.setPreferredSize(new Dimension(350, 250));
        buttonsPanel.setOpaque(false);

        JButton playButton = createButton("PLAY", e -> switchToGamePanel());
        playButton.setFont(buttonFont);
        buttonsPanel.add(playButton);

        JButton multiplayerButton = createButton("MULTIPLAYER", e -> switchToMultiplayerPanel());
        multiplayerButton.setFont(buttonFont);
        buttonsPanel.add(multiplayerButton);

        JButton rulesButton = createButton("RULES", e -> switchToRulesPanel());
        rulesButton.setFont(buttonFont);
        buttonsPanel.add(rulesButton);

        JButton settingsButton = createButton("SETTINGS", e -> switchToSettingsPanel());
        settingsButton.setFont(buttonFont);
        buttonsPanel.add(settingsButton);

        return buttonsPanel;
    }

    private JButton createButton(String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.addActionListener(actionListener);
        button.setBackground(Color.ORANGE);
        return button;
    }
}
