package GUI.Panels;

import Heckmeck.Components.Dice;
import Heckmeck.Components.Die;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static Utils.GUI.IconHandler.getDieIcon;
import static java.awt.GridBagConstraints.NORTH;

public class DicePanel extends JPanel {
    private Die.Face chosenFace;
    private final static int topEmptyBorder = 0;
    private final static int sideEmptyBorder = 5;
    private final static int bottomEmptyBorder = 50;
    private final static int gbcInsets = 20;
    private final static int maxDicePerRow = 3;
    private final static int rollingAnimationDuration = 100;
    private final static int rollingAnimationNumberOfChangingIcons = 10;

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
        if (gbc.gridx > maxDicePerRow) {
            gbc.gridx = 0;
            gbc.gridy++;
        }
    }

    private static GridBagConstraints initGridBagConstraints() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(gbcInsets, gbcInsets, gbcInsets, gbcInsets);
        gbc.anchor = NORTH;
        gbc.fill = GridBagConstraints.NONE;
        return gbc;
    }

    private JToggleButton makeDiceButton(Die die) {
        JToggleButton dieButton = new JToggleButton();
        Die.Face dieFace = die.getDieFace();
        dieButton.setIcon(getDieIcon(dieFace));
        dieButton.setBorder(null);
        dieButton.putClientProperty("originalFace", dieFace);
        dieButton.addActionListener(e -> chosenFace = dieFace);
        return dieButton;
    }

    public void rollDiceAnimation() {
        Timer timer = new Timer(rollingAnimationDuration, new ActionListener() {
            private int rollCount = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (rollCount < rollingAnimationNumberOfChangingIcons) {
                    for (Component component : getComponents()) {
                        if (component instanceof JToggleButton dieButton) {
                            dieButton.setEnabled(false);
                            Die.Face randomFace = Die.generateDie().getDieFace();
                            dieButton.setIcon(getDieIcon(randomFace));
                            dieButton.setDisabledIcon(getDieIcon(randomFace));
                        }
                    }
                    repaint();
                    rollCount++;
                } else {
                    ((Timer) e.getSource()).stop();
                    for (Component component : getComponents()) {
                        if (component instanceof JToggleButton dieButton) {
                            Die.Face originalFace = (Die.Face) dieButton.getClientProperty("originalFace");
                            dieButton.setIcon(getDieIcon(originalFace));
                            dieButton.setEnabled(true);
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
