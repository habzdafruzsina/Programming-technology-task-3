package game.logic.player;

import java.awt.*;

public enum Colors{

    LIGHT_BLUE(new Color(13, 225, 225),"light blue"),
    BLUE(new Color(2, 56, 220),"blue"),
    PINK(new Color(252, 44, 169),"pink"),
    GREEN(new Color(129, 254, 35),"green");


    private Color color;
    private String colorName;

    private static final int NUMBER_OF_COLORS = 4;

    /**
     * Color konstruktor
     * @param color szín
     * @param colorName szín megnevezése
     */
    private Colors(Color color, String colorName){
            this.color = color;
            this.colorName = colorName;
    }

    public Color getColor() { return color; }

    public String getColorName() { return colorName; }

    public static int getNumberOfColors(){return NUMBER_OF_COLORS;}
}
