package game.persistance.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    private final String url;
    private final String user;
    private final String password;

    /**
     * DBConnection konstruktor, lekéri a konfiguráció tulajdonságait
     * @param properties
     */
    DBConnection(Properties properties) {
        if (properties == null) {
            throw new IllegalStateException("A konfiguráció nem lehet üres!");
        }

        url = properties.getProperty("dbUrl");
        user = properties.getProperty("dbUser");
        password = properties.getProperty("dbPassword");
    }

    /**
     * visszaadja a kapcsolatot
     * @return
     * @throws SQLException
     */
    Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

}
