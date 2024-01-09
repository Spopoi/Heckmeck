package Utils.GUI;

import javax.swing.*;
import java.awt.*;

public class InfoPanel extends JPanel {
    private JLabel messageLabel;

    public InfoPanel() {
        initializeComponents();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());

        messageLabel = new JLabel();
        add(messageLabel, BorderLayout.CENTER);
    }

    public int showConfirmDialog(String message, String title) {
        int result = JOptionPane.showConfirmDialog(null, this, title, JOptionPane.YES_NO_OPTION);
        clearMessage();
        return result;
    }

    public String showInputDialog(String message, String title) {
        String result = JOptionPane.showInputDialog(null, this, title, JOptionPane.QUESTION_MESSAGE);
        clearMessage();
        return result;
    }

    public void showMessage(String message) {
        messageLabel.setText("<html><div style='text-align: center;'>" + message + "</div></html>");
        JOptionPane.showMessageDialog(null, this, "Info", JOptionPane.INFORMATION_MESSAGE);
        clearMessage();
    }

    public void showError(String error) {
        messageLabel.setText("<html><div style='color: red; text-align: center;'>" + error + "</div></html>");
        JOptionPane.showMessageDialog(null, this, "Error", JOptionPane.ERROR_MESSAGE);
        clearMessage();
    }

    private void clearMessage() {
        messageLabel.setText(null);
    }
}
