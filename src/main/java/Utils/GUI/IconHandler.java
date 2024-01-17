package Utils.GUI;

import GUI.Panels.RoundedPanel;
import Heckmeck.Components.Die;
import Heckmeck.Components.Tile;
import Utils.PropertiesManager;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static Utils.PropertiesManager.getPathPropertiesPath;

public class IconHandler {
    private static final int DICE_SIZE = 60;
    public static final int CHOSEN_DICE_SIZE = 45;
    private static final int PLAYER_TILE_ICON_WIDTH = 40;
    private static final int PLAYER_TILE_ICON_HEIGHT = 50;
    private static final int TILE_ROUNDED_WIDTH = 6;
    private static final int TILE_ROUNDED_HEIGHT = 25;
    private static final int TILE_HEIGHT = 70;
    private static final int TILE_WIDTH = 50;
    private static final PropertiesManager path_manager;

    static {
        try {
            path_manager = new PropertiesManager(getPathPropertiesPath());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    private static final Map<Die.Face, ImageIcon> dieIcons = initDieIcons();
    private static final Map<Die.Face, ImageIcon> chosenDieIcons = initChosenDieIcons();
    private static final Map<Integer, RoundedPanel> tileIcons = initTileIcons();
    private static final Map<Integer, RoundedPanel> playerTileIcons = initPlayerTileIcons();

    private static Map<Integer, RoundedPanel> initTileIcons() {
        Map<Integer, RoundedPanel> tileNumberToIconMap = new HashMap<>();
        for (int tileNumber = Tile.tileMinNumber; tileNumber <= Tile.tileMaxNumber; tileNumber++) {
            tileNumberToIconMap.put(tileNumber, createTilePanel(tileNumber, TILE_HEIGHT, TILE_WIDTH));
        }
        return Collections.unmodifiableMap(tileNumberToIconMap);
    }

    private static Map<Integer, RoundedPanel> initPlayerTileIcons() {
        Map<Integer, RoundedPanel> playerTileIconsMap = new HashMap<>();
        for (int tileNumber = Tile.tileMinNumber; tileNumber <= Tile.tileMaxNumber; tileNumber++) {
            playerTileIconsMap.put(tileNumber, createTilePanel(tileNumber, PLAYER_TILE_ICON_HEIGHT, PLAYER_TILE_ICON_WIDTH));
        }
        return Collections.unmodifiableMap(playerTileIconsMap);
    }

    private static Map<Die.Face, ImageIcon> initDieIcons() {
        Map<Die.Face, ImageIcon> faceToIconMap = new HashMap<>();
        for (Die.Face face : Die.Face.values()) {
            faceToIconMap.put(face, createDieIcon(face, DICE_SIZE, DICE_SIZE));
        }
        return Collections.unmodifiableMap(faceToIconMap);
    }
    private static Map<Die.Face, ImageIcon> initChosenDieIcons() {
        Map<Die.Face, ImageIcon> chosenDieIconsMap = new HashMap<>();
        for (Die.Face face : Die.Face.values()) {
            chosenDieIconsMap.put(face, createDieIcon(face, CHOSEN_DICE_SIZE, CHOSEN_DICE_SIZE));
        }
        return Collections.unmodifiableMap(chosenDieIconsMap);
    }

    public static RoundedPanel getTileIcon(int tileNumber) {
        return tileIcons.get(tileNumber);
    }

    public static RoundedPanel getPlayerTileIcon(int tileNumber) {
        return playerTileIcons.get(tileNumber);
    }

    public static ImageIcon getDieIcon(Die.Face face) {
        return dieIcons.get(face);
    }

    public static ImageIcon getChosenDieIcon(Die.Face face) {
        return chosenDieIcons.get(face);
    }

    private static ImageIcon createDieIcon(Die.Face face, int height, int width) {
        String path = path_manager.getMessage(face.name());
        ImageIcon icon = new ImageIcon(path);
        return resizeIcon(icon, height, width);
    }

    private static RoundedPanel createTilePanel(int tileNumber, int height, int width) {
        String path = path_manager.getMessage(String.valueOf(tileNumber));
        ImageIcon icon = resizeIcon(new ImageIcon(path), height, width);
        JLabel iconLabel = new JLabel(icon);
        RoundedPanel roundedTilePanel = new RoundedPanel( null);
        roundedTilePanel.setLayout(new BorderLayout());
        Dimension roundedPanelDim = new Dimension(width+TILE_ROUNDED_WIDTH,height+TILE_ROUNDED_HEIGHT);
        roundedTilePanel.setPreferredSize(roundedPanelDim);
        roundedTilePanel.setMaximumSize(roundedPanelDim);
        roundedTilePanel.setMinimumSize(roundedPanelDim);
        roundedTilePanel.add(iconLabel, BorderLayout.CENTER);
        return roundedTilePanel;
    }

    private static ImageIcon resizeIcon(ImageIcon icon, int height, int width) {
        Image img = icon.getImage();
        Image newImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(newImg);
    }
}