package GUI.Panels;

import javax.swing.*;
import java.awt.*;
import static GUI.HeckmeckGUI.switchToMenuPanel;
import static GUI.Panels.MenuPanel.createButton;

public class RulesPanel extends JPanel {
    private static final String RULES_IMAGE_PATH_1 = "src/main/resources/GUI/Rules1.jpg";
    private static final String RULES_IMAGE_PATH_2 = "src/main/resources/GUI/Rules2.jpg" ;
    private static final int SCROLLING_UNIT_INCREMENT = 16;
    public RulesPanel() {
        setLayout(new GridBagLayout());

        JScrollPane scrollPane = getRulesScrollPane();
        JButton backToMenuButton = createButton("Back to Menu", e -> switchToMenuPanel());

        add(backToMenuButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));

        add(scrollPane, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    }

    private JScrollPane getRulesScrollPane() {
        JPanel imagesPanel = createImagesPanel();
        JScrollPane scrollPane = new JScrollPane(imagesPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(SCROLLING_UNIT_INCREMENT);
        return scrollPane;
    }

    private JPanel createImagesPanel() {
        JPanel imagesPanel = new JPanel(new GridLayout(2, 1));

        ImageIcon rulesImage1 = new ImageIcon(RULES_IMAGE_PATH_1);
        JLabel rulesLabel1 = new JLabel(rulesImage1);
        imagesPanel.add(rulesLabel1);

        ImageIcon rulesImage2 = new ImageIcon(RULES_IMAGE_PATH_2);
        JLabel rulesLabel2 = new JLabel(rulesImage2);
        imagesPanel.add(rulesLabel2);

        return imagesPanel;
    }
}
