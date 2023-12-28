package GUI.Panels;

import Heckmeck.Dice;
import Heckmeck.Die;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static Heckmeck.FileReader.getDieIcon;
import static java.awt.GridBagConstraints.NORTH;

public class DicePanel extends JPanel {
    private Die.Face chosenFace;
    private final static int topEmptyBorder = 0;
    private final static int sideEmptyBorder = 5;
    private final static int bottomEmptyBorder = 50;

    public DicePanel(){
        super();
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(topEmptyBorder, sideEmptyBorder, bottomEmptyBorder, sideEmptyBorder));
        setOpaque(false);
    }

    public void updateDice(Dice dice){
        this.removeAll();
        chosenFace = null;
        GridBagConstraints gbc = initGridBagConstraints();
        for (Die die : dice.getDiceList()) {
            JToggleButton dieButton = makeDiceButton(die);
            add(dieButton, gbc);
            updateGridBagConstraints(gbc);
        }
    }

    private void updateGridBagConstraints(GridBagConstraints gbc) {
        gbc.gridx++;
        if (gbc.gridx > 3) {
            gbc.gridx = 0;
            gbc.gridy++;
        }
    }

    private static GridBagConstraints initGridBagConstraints() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 20, 30, 20);
        gbc.anchor = NORTH;
        gbc.fill = GridBagConstraints.NONE;
        return gbc;
    }

    private JToggleButton makeDiceButton(Die die) {
        JToggleButton dieButton = new JToggleButton();
        Die.Face dieFace = die.getDieFace();
        dieButton.setIcon(getDieIcon(dieFace, 65));
        dieButton.setBorder(null);
        dieButton.putClientProperty("originalFace", dieFace);
        dieButton.addActionListener(e -> chosenFace = dieFace);
        return dieButton;
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
