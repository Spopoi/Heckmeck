package GUI.Panels;

import javax.swing.*;
import java.awt.*;
import static GUI.HeckmeckGUI.switchToMenuPanel;

public class RulesPanel extends JPanel {
    public RulesPanel() {
        setLayout(new GridBagLayout());

        JPanel imagesPanel = createImagesPanel();
        JScrollPane scrollPane = new JScrollPane(imagesPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        JButton backToMenuButton = createBackToMenuButton();

        add(backToMenuButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
        add(scrollPane, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    }

    private JPanel createImagesPanel() {
        JPanel imagesPanel = new JPanel(new GridLayout(2, 1));

        ImageIcon rulesImage1 = new ImageIcon("src/main/resources/Icons/Rules1.jpg");
        JLabel rulesLabel1 = new JLabel(rulesImage1);
        imagesPanel.add(rulesLabel1);

        ImageIcon rulesImage2 = new ImageIcon("src/main/resources/Icons/Rules2.jpg");
        JLabel rulesLabel2 = new JLabel(rulesImage2);
        imagesPanel.add(rulesLabel2);

        return imagesPanel;
    }

    private JButton createBackToMenuButton() {
        JButton backToMenuButton = new JButton("Torna al Menu");
        backToMenuButton.addActionListener(e -> switchToMenuPanel());
        backToMenuButton.setPreferredSize(new Dimension(70, 70));
        backToMenuButton.setBackground(Color.ORANGE);

        return backToMenuButton;
    }
}
