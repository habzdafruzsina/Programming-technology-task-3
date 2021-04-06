package game.io;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class PropertiesReader {

    private PropertiesReader(){}

    /**
     * tulajdonságokat olvas az adott elérési útvonalról
     * @param path elérési útvonal
     * @return tulajdonságokat ad vissza
     */
    public static Properties readProperties(String path){
        try (final FileInputStream fis = new FileInputStream(new File(path))){
            final Properties properties = new Properties();
            properties.load(fis);
            return properties;
        } catch (Exception ex) {
            throw new IllegalStateException("A megadott utvonalon nem érhető el a konfigurációs file!", ex);
        }
    }
}
