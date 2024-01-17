package Utils.GUI;

import Heckmeck.Components.Die;

import javax.swing.*;
import java.awt.*;

import static Utils.GUI.IconHandler.*;

public class LabelHandler {
    //TODO: getter
    public static final Font TITLE_FONT = new Font("Serif", Font.BOLD, 26);
    public static final Font TEXT_FONT = new Font("Serif", Font.PLAIN, 20);

    public static JLabel getLabel(String text, int alignment){
        JLabel label = new JLabel(text);
        label.setFont(TEXT_FONT);
        label.setHorizontalAlignment(alignment);
        return label;
    }

    public static JLabel getLabel(String text, int width, int height){
        JLabel label = makeLabel(width, height);
        label.setText(text);
        label.setFont(TEXT_FONT);
        return label;
    }

    //TODO: usati solo in PlayerDataPanel
    public static JLabel getTitleLabel(String text , int width, int height){
        JLabel label = makeLabel(width, height);
        label.setText(text);
        label.setFont(TITLE_FONT);
        return label;
    }

    public static JLabel getTitleLabel(String text , int alignment){
        JLabel label = new JLabel(text);
        label.setFont(TITLE_FONT);
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
        label.setIcon(getChosenDieIcon(face));
        return label;
    }

}
