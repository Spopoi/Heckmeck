package Utils.GUI;

import Heckmeck.Components.Die;

import javax.swing.*;
import java.awt.*;

import static Utils.GUI.IconHandler.CHOSEN_DICE_SIZE;
import static Utils.GUI.IconHandler.getDieIcon;

public class LabelHandler {
    public static final Font titleFont = new Font("Serif", Font.BOLD, 30);
    public static final Font textFont = new Font("Serif", Font.PLAIN, 25);

    public static JLabel getLabel(int width, int height){
        JLabel label = makeLabel(width, height);
        label.setFont(textFont);
        return label;
    }

    public static JLabel getLabel(String text, int width, int height){
        JLabel label = makeLabel(width, height);
        label.setText(text);
        label.setFont(textFont);
        return label;
    }
    public static JLabel getTitleLabel(int width, int height){
        JLabel label = makeLabel(width, height);
        label.setFont(titleFont);
        return label;
    }
    public static JLabel getTitleLabel(String text , int width, int height){
        JLabel label = makeLabel(width, height);
        label.setText(text);
        label.setFont(titleFont);
        return label;

    }public static JLabel getTitleLabel(String text , int alignment){
        JLabel label = new JLabel(text);
        label.setFont(titleFont);
        label.setHorizontalAlignment(alignment);
        return label;
    }

    private static JLabel makeLabel(int width, int height){
        JLabel label = new JLabel();
        label.setPreferredSize(new Dimension(width, height));
        return label;
    }

    public static JLabel getChosenDieLabel(Die.Face face){
        JLabel label = new JLabel();
        label.setIcon(getDieIcon(face, CHOSEN_DICE_SIZE));
        return label;
    }

}
