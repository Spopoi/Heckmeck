package GUI.Panels;

import Utils.PropertiesManager;

import javax.swing.*;
import java.awt.*;

import static GUI.HeckmeckGUI.getPathManager;
import static GUI.HeckmeckGUI.switchToMenuPanel;
import static Heckmeck.Launcher.getPropertiesManager;
import static Utils.GUI.ButtonHandler.createButton;

public class RulesPanel extends JPanel {
    private static final int SCROLLING_UNIT_INCREMENT = 16;
    private final PropertiesManager pathManager;

    public RulesPanel() {
        setLayout(new GridBagLayout());
        pathManager = getPathManager();
        PropertiesManager propertiesManager = getPropertiesManager();

        JScrollPane scrollPane = getRulesScrollPane();
        String backToMenu = propertiesManager.getMessage("backToMenu");
        JButton backToMenuButton = createButton(backToMenu, e -> switchToMenuPanel());

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

        String RULES_IMAGE_PATH_1 = pathManager.getMessage("RULES_IMAGE_PATH_1");
        ImageIcon rulesImage1 = new ImageIcon(RULES_IMAGE_PATH_1);
        JLabel rulesLabel1 = new JLabel(rulesImage1);
        imagesPanel.add(rulesLabel1);

        String RULES_IMAGE_PATH_2 = pathManager.getMessage("RULES_IMAGE_PATH_2");
        ImageIcon rulesImage2 = new ImageIcon(RULES_IMAGE_PATH_2);
        JLabel rulesLabel2 = new JLabel(rulesImage2);
        imagesPanel.add(rulesLabel2);

        return imagesPanel;
    }
}
