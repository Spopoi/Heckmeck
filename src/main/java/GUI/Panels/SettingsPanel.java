package GUI.Panels;

import Utils.PropertiesManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static GUI.HeckmeckGUI.*;
import static Utils.GUI.ButtonHandler.createBackgroundButton;
import static Utils.GUI.ButtonHandler.createButton;
import static Utils.GUI.LabelHandler.*;

public class SettingsPanel extends JPanel {
    private static final int CENTER_PANEL_SIDE_SPACING = 150;
    private static final int CENTER_PANEL_BOTTOM_SPACING = 70;
    private static final int INPUT_PANEL_BOTTOM_SPACING = 70;
    private static final int INPUT_PANEL_TOP_SPACING = 70;
    private static final int INPUT_PANEL_SIDE_SPACING = 50;
    private static final int HORIZONTAL_BUTTONS_GAP = 50;
    private static final int NUM_OF_BACKGROUNDS = 3;
    private String background_path = BACKGROUND_IMAGE_PATH;
    private final PropertiesManager propertiesManager;
    private final PropertiesManager pathManager;

    public SettingsPanel() {
        setLayout(new BorderLayout());
        propertiesManager = getPropertiesManager();
        pathManager = getPathManager();
        add(createBackToMenuPanel(), BorderLayout.NORTH);
        add(createCenterPanel(), BorderLayout.CENTER);
    }

    private JPanel createBackToMenuPanel() {
        String backToMenu = propertiesManager.getMessage("backToMenu");
        JButton backToMenuButton = createButton(backToMenu, e -> switchToMenuPanel());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(backToMenuButton);
        return buttonPanel;
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout());
        EmptyBorder centerPanelBorder = new EmptyBorder(0, CENTER_PANEL_SIDE_SPACING, CENTER_PANEL_BOTTOM_SPACING, CENTER_PANEL_SIDE_SPACING);
        centerPanel.setBorder(centerPanelBorder);

        String chooseBackground = propertiesManager.getMessage("chooseBackground");
        JLabel titleLabel = getTitleLabel(chooseBackground, JLabel.CENTER);
        centerPanel.add(titleLabel, BorderLayout.NORTH);
        centerPanel.add(getBackgroundButtonsPanel(), BorderLayout.CENTER);

        String applyButtonKey = propertiesManager.getMessage("applyButton");
        JButton applyButton = createButton(applyButtonKey, e->applySettings());
        centerPanel.add(applyButton, BorderLayout.SOUTH);
        return centerPanel;
    }

    private JPanel getBackgroundButtonsPanel() {
        JPanel inputPanel = new JPanel(new GridLayout(1, NUM_OF_BACKGROUNDS, HORIZONTAL_BUTTONS_GAP, 0));
        inputPanel.setBorder(new EmptyBorder(INPUT_PANEL_TOP_SPACING, INPUT_PANEL_SIDE_SPACING, INPUT_PANEL_BOTTOM_SPACING, INPUT_PANEL_SIDE_SPACING));

        String yellowBackground = pathManager.getMessage("YELLOW_BACKGROUND_PATH");
        inputPanel.add(getBackgroundButton(yellowBackground));
        String greenBackground = pathManager.getMessage("GREEN_BACKGROUND_PATH");
        inputPanel.add(getBackgroundButton(greenBackground));
        String blueBackground = pathManager.getMessage("BLUE_BACKGROUND_PATH");
        inputPanel.add(getBackgroundButton(blueBackground));
        return inputPanel;
    }

    private JButton getBackgroundButton(String imagePath) {
        JButton button = createBackgroundButton(imagePath);
        button.addActionListener(e -> background_path = imagePath);
        return button;
    }

    public void applySettings() {
       setBackgroundImagePath(background_path);
    }
}
