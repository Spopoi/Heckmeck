package GUI.Panels;

import Heckmeck.Components.Dice;
import Heckmeck.Components.Die;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static Utils.GUI.IconHandler.getDieIcon;
import static java.awt.GridBagConstraints.NORTH;

//TODO: refactoring, adding an init method and modify updateDice()
public class DicePanel extends JPanel {
    private Die.Face chosenFace;
    private final static int topEmptyBorder = 0;
    private final static int sideEmptyBorder = 5;
    private final static int bottomEmptyBorder = 50;
    private final static int gbcInsets = 15;
    private final static int maxDicePerRow = 3;
    private final static int rollingAnimationDuration = 130;
    private final static int rollingAnimationNumberOfChangingIcons = 15;

    public DicePanel(){
        super();
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(topEmptyBorder, sideEmptyBorder, bottomEmptyBorder, sideEmptyBorder));
        setOpaque(false);
    }

    public void updateDice(Dice dice){
        this.removeAll();
        reset();
        GridBagConstraints gbc = initGridBagConstraints();
        for (Die die : dice.getDiceList()) {
            RoundedPanel roundedDie = new RoundedPanel(null);
            JToggleButton dieButton = makeDiceButton(die);
            roundedDie.add(dieButton);
            add(roundedDie, gbc);
            updateGridBagConstraints(gbc);
        }
    }
    public void reset(){
        chosenFace = null;
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
        dieButton.setFocusPainted(false);
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
                        if (component instanceof RoundedPanel roundedPanel) {
                            JToggleButton dieButton = (JToggleButton) roundedPanel.getComponent(0);
                            dieButton.setEnabled(false);
                            Die randomDie = Die.generateDie();
                            randomDie.rollDie();
                            Die.Face randomFace = randomDie.getDieFace();
                            dieButton.setIcon(getDieIcon(randomFace));
                            dieButton.setDisabledIcon(getDieIcon(randomFace));
                        }
                    }
                    repaint();
                    rollCount++;
                } else {
                    ((Timer) e.getSource()).stop();
                    for (Component component : getComponents()) {
                        if (component instanceof RoundedPanel roundedPanel) {
                            JToggleButton dieButton = (JToggleButton) roundedPanel.getComponent(0);
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
