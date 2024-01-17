package Utils.GUI;

import GUI.Panels.SettingsPanel;
import Heckmeck.Components.Die;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

import static Utils.GUI.IconHandler.getDieIcon;

public class ButtonHandler {
    public static final Font buttonFont = new Font(UIManager.getFont("Button.font").getName(), Font.BOLD, 14);
    public static final Color BUTTON_COLOR = Color.ORANGE;

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

    //todo: no need to generalize
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
