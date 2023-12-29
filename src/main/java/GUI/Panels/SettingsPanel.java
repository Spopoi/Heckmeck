package GUI.Panels;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;


import static GUI.GUIIOHandler.textFont;
import static GUI.GUIIOHandler.titleFont;
import static GUI.HeckmeckGUI.switchToMenuPanel;

public class SettingsPanel extends JPanel {
    private JTextField heightTextField;
    private JTextField widthTextField;
    private final Frame frame;

    public SettingsPanel(Frame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());
        setPreferredSize(frame.getSize());

        JPanel centerPanel = createCenterPanel();

        add(createButtonPanel(), BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel createButtonPanel() {
        JButton backButton = new JButton("Back to menu");
        backButton.addActionListener(e -> switchToMenuPanel());
        backButton.setBackground(Color.ORANGE);
        backButton.setFont(textFont);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(backButton);

        return buttonPanel;
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(new EmptyBorder(0, 150, 50, 150));

        JLabel titleLabel = new JLabel("Settings");
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setFont(titleFont);
        int titleSpacing = 30;
        titleLabel.setBorder(new EmptyBorder(20, 0, titleSpacing, 0));

        centerPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel inputPanel = createInputPanel();
        centerPanel.add(inputPanel, BorderLayout.CENTER);

        JButton applyButton = createApplyButton();
        centerPanel.add(applyButton, BorderLayout.SOUTH);

        return centerPanel;
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 50));
        inputPanel.setBorder(new EmptyBorder(70, 50, 100, 50));

        JLabel heightLabel = new JLabel("Frame height:");
        heightLabel.setFont(titleFont);
        heightTextField = new JTextField();
        JLabel widthLabel = new JLabel("Frame width:");
        widthLabel.setFont(titleFont);
        widthTextField = new JTextField();

        inputPanel.add(heightLabel);
        inputPanel.add(heightTextField);
        inputPanel.add(widthLabel);
        inputPanel.add(widthTextField);

        return inputPanel;
    }

    private JButton createApplyButton() {
        JButton applyButton = new JButton("Apply");
        applyButton.addActionListener(e -> applySettings());
        applyButton.setFont(textFont);
        applyButton.setBackground(Color.ORANGE);

        return applyButton;
    }

    private void applySettings() {
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
