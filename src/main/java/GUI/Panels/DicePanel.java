package GUI.Panels;

import Heckmeck.Components.Dice;
import Heckmeck.Components.Die;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static Heckmeck.Rules.INITIAL_NUMBER_OF_DICE;
import static Utils.GUI.ButtonHandler.makeDieButton;
import static Utils.GUI.IconHandler.getDieIcon;
import static java.awt.GridBagConstraints.NORTH;

public class DicePanel extends JPanel {
    private Die.Face chosenFace;
    private final static int topEmptyBorder = 0;
    private final static int sideEmptyBorder = 5;
    private final static int bottomEmptyBorder = 50;
    private final static int gbcInsets = 15;
    private final static int maxDicePerRow = 3;
    private final static int rollingAnimationDuration = 130;
    private final static int NUM_OF_ROLLING_ICONS = 15;
    private ArrayList<RoundedPanel> diceList;

    public DicePanel(){
        super();
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(topEmptyBorder, sideEmptyBorder, bottomEmptyBorder, sideEmptyBorder));
        setOpaque(false);
        init();
    }

    private void init(){
        GridBagConstraints gbc = initGridBagConstraints();
        diceList = new ArrayList<>();
        for (int i = 0; i < INITIAL_NUMBER_OF_DICE ; i++){
            RoundedPanel roundedDie = new RoundedPanel(null);
            JToggleButton dieButton = makeDieButton();
            roundedDie.add(dieButton);
            add(roundedDie, gbc);
            diceList.add(roundedDie);
            updateGridBagConstraints(gbc);
        }
    }

    public void updateDice(Dice dice){
        reset();
        int i = 0;
        for(Die die : dice.getDiceList()){
            RoundedPanel diePanel = diceList.get(i);
            diePanel.setVisible(true);
            JToggleButton button = (JToggleButton) diePanel.getComponent(0);
            updateDieButton(button, die.getDieFace());
            i++;
        }
        while(i < INITIAL_NUMBER_OF_DICE){
            RoundedPanel diePanel = diceList.get(i);
            diePanel.setVisible(false);
            i++;
        }
    }

    private void updateDieButton(JToggleButton button, Die.Face face){
        button.setIcon(getDieIcon(face));
        button.setActionCommand(face.toString());
        button.addActionListener(e -> chosenFace = Die.Face.valueOf(button.getActionCommand()));
        button.setSelected(false);
    }

    public void reset(){
        chosenFace = null;
    }

    public Die.Face getChosenFace(){
        return chosenFace;
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

    public void rollDiceAnimation() {
        Timer timer = new Timer(rollingAnimationDuration, new ActionListener() {
            private int rollCount = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (rollCount < NUM_OF_ROLLING_ICONS) {
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
                            Die.Face originalFace = Die.Face.valueOf(dieButton.getActionCommand());
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
}
