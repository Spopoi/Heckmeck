package GUI.Panels;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static GUI.Panels.MenuPanel.createButton;
import static GUI.HeckmeckGUI.switchToMenuPanel;
import static Utils.GUI.LabelHandler.*;

public class SettingsPanel extends JPanel {
    private JTextField heightTextField;
    private JTextField widthTextField;
    private final Frame frame;
    private static final int CENTER_PANEL_SIDE_SPACING = 150;
    private static final int CENTER_PANEL_BOTTOM_SPACING = 50;
    private static final int INPUT_PANEL_BOTTOM_SPACING = 100;
    private static final int INPUT_PANEL_TOP_SPACING = 70;
    private static final int INPUT_PANEL_SIDE_SPACING = 50;

    public SettingsPanel(Frame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());
        setPreferredSize(frame.getSize());

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

        JLabel titleLabel = getTitleLabel("Settings", JLabel.CENTER);
        titleLabel.setBorder(new EmptyBorder(20, 0, 30, 0));
        centerPanel.add(titleLabel, BorderLayout.NORTH);
        JPanel inputPanel = createInputPanel();
        centerPanel.add(inputPanel, BorderLayout.CENTER);
        JButton applyButton = createButton("Apply", e->applySettings());
        centerPanel.add(applyButton, BorderLayout.SOUTH);
        return centerPanel;
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 50));
        inputPanel.setBorder(new EmptyBorder(INPUT_PANEL_TOP_SPACING, INPUT_PANEL_SIDE_SPACING, INPUT_PANEL_BOTTOM_SPACING, INPUT_PANEL_SIDE_SPACING));

        JLabel heightLabel = getTitleLabel("Frame height:", JLabel.LEFT);
        heightTextField = getTextField();
        JLabel widthLabel = getTitleLabel("Frame width:", JLabel.LEFT);
        widthTextField = getTextField();

        inputPanel.add(heightLabel);
        inputPanel.add(heightTextField);
        inputPanel.add(widthLabel);
        inputPanel.add(widthTextField);

        return inputPanel;
    }

    private JTextField getTextField(){
        JTextField textField = new JTextField();
        textField.setFont(textFont);
        textField.setHorizontalAlignment(JLabel.CENTER);
        return textField;
    }

    public void applySettings() {
        try {
            int newHeight = Integer.parseInt(heightTextField.getText());
            int newWidth = Integer.parseInt(widthTextField.getText());
            frame.setSize(newWidth, newHeight);
            frame.revalidate();
            frame.repaint();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Insert valid numbers for height and width");
        }
    }
}
