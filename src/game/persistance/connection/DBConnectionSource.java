package game.persistance.connection;

import game.io.PropertiesReader;

import java.sql.Connection;
import java.sql.SQLException;

public final class DBConnectionSource {

    private final DBConnection connection;

    /**
     * DBConnection konstruktor
     * létrehozza a kapcsolatot
     */
    private DBConnectionSource(){
        this.connection = new DBConnection(PropertiesReader.readProperties("./config/config.properties"));
    }

    /**
     * visszaadja a DBConnectionSource példányát
     * @return
     */
    public static DBConnectionSource getInstance(){
        return DBConnectionInstanceHolder.INSTANCE;
    }

    private static class DBConnectionInstanceHolder{
        private static final DBConnectionSource INSTANCE = new DBConnectionSource();
    }

    public Connection getConnection() throws SQLException {
        return connection.getConnection();
    }

}