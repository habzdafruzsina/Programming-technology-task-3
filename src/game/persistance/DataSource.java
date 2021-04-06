package game.persistance;

public class DataSource {

    private final HighScoreDao highScoreDao;

    /**
     * DataSource konstruktora, létrehozza az adatbázist reprezentáló osztályt (HighScoreDao-t)
     */
    private DataSource(){
        this.highScoreDao = new HighScoreDao();
    }

    private static class DataSourceInstance{
        private static final DataSource INSTANCE = new DataSource();
    }

    /**
     * lekéri és visszaadja a DataSource példányát
     * @return
     */
    public static DataSource getInstance(){
        return DataSourceInstance.INSTANCE;
    }

    public HighScoreDao getHighScoreDao() {
        return highScoreDao;
    }
}