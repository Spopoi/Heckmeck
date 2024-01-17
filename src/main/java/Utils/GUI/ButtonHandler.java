package Utils.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

public class ButtonHandler {
    private static final Font buttonFont = new Font(UIManager.getFont("Button.font").getName(), Font.BOLD, 14);
    private static final Color BUTTON_COLOR = Color.ORANGE;

    public static JButton createButton(String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.addActionListener(actionListener);
        button.setBackground(BUTTON_COLOR);
        button.setFont(buttonFont);
        return button;
    }

    public static JButton createYesNoButton(String buttonText, Consumer<String> buttonClickHandler) {
        JButton button = new JButton(buttonText);
        button.setVisible(false);
        button.addActionListener(e -> buttonClickHandler.accept(buttonText));
        return button;
    }

    public static JButton createBackgroundButton(String imagePath) {
        return new JButton(new ImageIcon(imagePath));
    }

    public static JToggleButton makeDieButton() {
        JToggleButton dieButton = new JToggleButton();
        dieButton.setFocusPainted(false);
        dieButton.setBorder(null);
        return dieButton;
    }
}
