package GUI.Panels;

import Heckmeck.Dice;
import Heckmeck.Die;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static GUI.GUIIOHandler.getDieIcon;
import static java.awt.GridBagConstraints.NORTH;

public class DicePanel extends JPanel {

    private Die.Face chosenFace;

    public DicePanel(){
        super();
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(0, 5, 50, 5));
        setOpaque(false);
    }

    public void updateDice(Dice dice){
        this.removeAll();
        chosenFace = null;
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 20, 30, 20); // Add spacing between components
        gbc.anchor = NORTH; // Center the panel
        gbc.fill = GridBagConstraints.NONE; // Don't fill the available space

        for (Die die : dice.getDiceList()) {
            JToggleButton dieButton = new JToggleButton();
            Die.Face dieFace = die.getDieFace();
            dieButton.setIcon(getDieIcon(dieFace, 65));
            dieButton.setSelectedIcon(getDieIcon(dieFace, 65));
            dieButton.addActionListener(e -> chosenFace = dieFace);
            dieButton.setPreferredSize(new Dimension(60, 60));

            dieButton.setBorder(null);
            dieButton.putClientProperty("originalFace", dieFace); // Memorizza l'immagine originale del dado
            add(dieButton, gbc);

            gbc.gridx++;
            if (gbc.gridx > 3) {
                gbc.gridx = 0;
                gbc.gridy++;
            }
        }
    }

    public void rollDiceAnimation(){
        Timer timer = new Timer(100, new ActionListener() {
            private int rollCount = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (rollCount < 10) {
                    for (Component component : getComponents()) {
                        if (component instanceof JToggleButton dieButton) {
                            Die.Face randomFace = Die.generateDie().getDieFace();
                            dieButton.setIcon(getDieIcon(randomFace, 65));
                        }
                    }
                    repaint();
                    rollCount++;
                } else {
                    ((Timer) e.getSource()).stop();
                    for (Component component : getComponents()) {
                        if (component instanceof JToggleButton dieButton) {
                            Die.Face originalFace = (Die.Face) dieButton.getClientProperty("originalFace");
                            dieButton.setIcon(getDieIcon(originalFace, 65));
                        }
                    }
                    repaint();
                }
            }
        });
        timer.start();
    }

    public Die.Face getChosenFace(){
        return chosenFace;
    }
}
