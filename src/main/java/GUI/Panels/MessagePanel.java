package GUI.Panels;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.util.concurrent.Semaphore;

import static Utils.GUI.LabelHandler.textFont;

public class MessagePanel extends JPanel {
    private final JTextPane logTextPane;
    private JPanel buttonPanel;
    private boolean dialogResult;
    private final Semaphore semaphore = new Semaphore(0);

    public MessagePanel() {
        setLayout(new BorderLayout());
        setOpaque(false);

        initYesNoPanel();
        logTextPane = initTextPane();
        JScrollPane scrollPane = initScrollPane();

        add(scrollPane, BorderLayout.CENTER);
        setOpaque(false);
    }

    private JScrollPane initScrollPane() {
        JScrollPane scrollPane = new JScrollPane(logTextPane);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setOpaque(false);
        scrollPane.setBorder(null);

        scrollPane.getViewport().setBackground(new Color(0, 0, 0, 0));
        return scrollPane;
    }

    private JTextPane initTextPane() {
        final JTextPane logTextPane;
        logTextPane = new JTextPane();
        logTextPane.setEditable(false);
        logTextPane.setFont(textFont);
        logTextPane.setFocusable(false);
        return logTextPane;
    }

    private void initYesNoPanel() {
        JButton yesButton = new JButton("Yes");
        yesButton.setVisible(false);
        JButton noButton = new JButton("No");
        noButton.setVisible(false);

        buttonPanel = new JPanel();
        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);

        yesButton.addActionListener(e -> {
            dialogResult = true;
            hideButtonPanel();
            showLogMessage("Yes");
            semaphore.release();
        });

        noButton.addActionListener(e -> {
            dialogResult = false;
            hideButtonPanel();
            showLogMessage("No");
            semaphore.release();
        });

        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void showLogMessage(String message) {
        StyledDocument doc = logTextPane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);

        try {
            doc.insertString(doc.getLength(), "\n" + message, null);
            doc.setParagraphAttributes(0, doc.getLength(), center, false);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> logTextPane.setCaretPosition(doc.getLength()));
    }

    public boolean showYesNoPanel(String message) {
        showLogMessage(message);
        showButtonPanel();
        revalidate();

        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return dialogResult;
    }

    private void showButtonPanel() {
        for (Component component : buttonPanel.getComponents()) {
            component.setVisible(true);
        }
        revalidate();
        repaint();
    }

    private void hideButtonPanel() {
        for (Component component : buttonPanel.getComponents()) {
            component.setVisible(false);
        }
        revalidate();
        repaint();
    }

    // TODO: Prop
    public static String showInputDialog(String message) {
        return JOptionPane.showInputDialog(null, message, "Heckmeck", JOptionPane.QUESTION_MESSAGE);
    }
}