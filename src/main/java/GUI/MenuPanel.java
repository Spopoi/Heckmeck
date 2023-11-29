package GUI;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import static GUI.HeckmeckGUI.*;

public class MenuPanel extends ImagePanel {

    public MenuPanel() {
        //TODO: Verificare se ImagePanel e' necessario o si puo' fare come in RulesPanel (ImageIcon)
        super("src/main/java/GUI/Icons/heckmeckBackground.jpg");
        setLayout(new GridBagLayout());

        JPanel buttonsPanel = createButtonsPanel();

        add(buttonsPanel, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(180, 0, 0, 0), 0, 0));

        setVisible(true);
    }

    private JPanel createButtonsPanel() {
        JPanel buttonsPanel = new JPanel(new GridLayout(3, 1, 10, 20));
        buttonsPanel.setPreferredSize(new Dimension(300, 200));
        buttonsPanel.setOpaque(false);

        JButton playButton = createButton("Play", e -> switchToGamePanel());
        buttonsPanel.add(playButton);

        JButton rulesButton = createButton("Rules", e -> switchToRulesPanel());
        buttonsPanel.add(rulesButton);

        JButton settingsButton = createButton("Settings", e -> switchToSettings());
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
