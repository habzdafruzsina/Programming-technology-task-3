package game.io;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ImageLoader {

    /**
     * visszaad egy képet az elérési útvonal alapján
     * @param imgPath a betöltendő kép elérési útvonala
     * @return betöltött kép
     * @throws IOException képbetöltés okozhatja
     */
    public static Image loadImage(String imgPath) throws IOException {
        Image img = null;
        img = ImageIO.read(new File(imgPath));
        return img;
    }
}
