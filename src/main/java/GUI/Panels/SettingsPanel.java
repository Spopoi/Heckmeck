package GUI.Panels;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static GUI.HeckmeckGUI.*;
import static GUI.Panels.MenuPanel.createButton;
import static Utils.GUI.LabelHandler.*;

public class SettingsPanel extends JPanel {
    private static final int CENTER_PANEL_SIDE_SPACING = 150;
    private static final int CENTER_PANEL_BOTTOM_SPACING = 70;
    private static final int INPUT_PANEL_BOTTOM_SPACING = 70;
    private static final int INPUT_PANEL_TOP_SPACING = 70;
    private static final int INPUT_PANEL_SIDE_SPACING = 50;
    private static final int HORIZONTAL_BUTTONS_GAP = 50;
    private static final int BACKGROUND_NUMBER = 3;
    private String background_path = BACKGROUND_IMAGE_PATH;

    public SettingsPanel() {
        setLayout(new BorderLayout());
        add(createBackToMenuPanel(), BorderLayout.NORTH);
        add(createCenterPanel(), BorderLayout.CENTER);
    }

    private JPanel createBackToMenuPanel() {
        JButton backToMenuButton = createButton("Back to Menu", e -> switchToMenuPanel());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(backToMenuButton);
        return buttonPanel;
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(new EmptyBorder(0, CENTER_PANEL_SIDE_SPACING, CENTER_PANEL_BOTTOM_SPACING, CENTER_PANEL_SIDE_SPACING));

        JLabel titleLabel = getTitleLabel("Choose background:", JLabel.CENTER);
        centerPanel.add(titleLabel, BorderLayout.NORTH);
        JPanel inputPanel = createInputPanel();
        centerPanel.add(inputPanel, BorderLayout.CENTER);
        JButton applyButton = createButton("Apply", e->applySettings());
        centerPanel.add(applyButton, BorderLayout.SOUTH);
        return centerPanel;
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new GridLayout(1, BACKGROUND_NUMBER, HORIZONTAL_BUTTONS_GAP, 0));
        inputPanel.setBorder(new EmptyBorder(INPUT_PANEL_TOP_SPACING, INPUT_PANEL_SIDE_SPACING, INPUT_PANEL_BOTTOM_SPACING, INPUT_PANEL_SIDE_SPACING));

        inputPanel.add(createBackgroundButton(YELLOW_BACKGROUND_PATH));
        inputPanel.add(createBackgroundButton(GREEN_BACKGROUND_PATH));
        inputPanel.add(createBackgroundButton(BLUE_BACKGROUND_PATH));
        return inputPanel;
    }

    private JButton createBackgroundButton(String imagePath) {
        JButton button = new JButton(new ImageIcon(imagePath));
        button.addActionListener(e -> background_path = imagePath);
        return button;
    }

    public void applySettings() {
       setBackgroundImagePath(background_path);
    }
}
