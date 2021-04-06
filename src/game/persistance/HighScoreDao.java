package game.persistance;

import game.persistance.connection.DBConnectionSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class HighScoreDao {

    private static final String tableName = "high_score";
    private static final String ID_ATTR_NAME = "name";
    private static final String SCORE_ATTR_NAME = "score";
    private static final String selectWholeSqlString = "SELECT * FROM " + tableName;
    private static final String selectTop10String = "SELECT * FROM " + tableName + " ORDER BY " + SCORE_ATTR_NAME + " DESC FETCH FIRST 10 ROWS ONLY";
    /**
     * frissíti az adott névvel rendelkező sor pontszámát
     * updateScoreString1 + score + updateScoreString2 + name
     */
    private static final String updateScoreString1 = "UPDATE " + tableName + " SET " + SCORE_ATTR_NAME + " = "; //+ lekérdezett szám 1-el növelve
    private static final String updateScoreString2 = " WHERE " + ID_ATTR_NAME + " = "; //a lekerdeeztt nevvel

    /**
     * lekérdezi az adatbázisból a legjobb 10 játékost (pontszám alapján)
     * @return HighScoreEntity-ket ad vissza
     */
    public ArrayList<HighScoreEntity> getTop10() {
        try(Connection connection = DBConnectionSource.getInstance().getConnection();
            Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = statement.executeQuery(selectTop10String)){
                final ArrayList<HighScoreEntity> entities = new ArrayList<>();
                while (rs.next()) {
                    final HighScoreEntity entity = new HighScoreEntity();
                    setEntityAttributes(entity, rs);
                    entities.add(entity);
                }
            return entities;
        } catch(SQLException e){
            System.err.println(e.getMessage());
            return null;
        }
    }

    /**
     * A paraméterül magadott játékosnak a pontszámát kéri le az adatbázisból
     * @param winner játékos neve
     * @return 0-t ad vissza, ha nem szerepelt a név az adatbázisban, 0-tól nagyobb számot, ha igen
     */
    private int getScore(String winner){
        String statementString = "SELECT * FROM " + tableName + " WHERE "+ ID_ATTR_NAME + " = '" + winner + "'";
        try(Connection connection = DBConnectionSource.getInstance().getConnection();
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = statement.executeQuery(statementString)){
            rs.next();
            final HighScoreEntity entity = new HighScoreEntity();
            setEntityAttributes(entity, rs);
            return entity.getScore();
        }catch(SQLException e){
            System.err.println(e.getMessage());
            return 0;
        }
    }

    /**
     * Az paraméterül kapott entitásnak növel eggyel a pontszámát az adatbázisban
     * @param entity
     */
    private void updateScore(HighScoreEntity entity){
        try(Connection connection = DBConnectionSource.getInstance().getConnection();
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);) {
            statement.executeUpdate(updateScoreString1 + entity.getScore() + updateScoreString2 + "'" + entity.getIdName() + "'");
            } catch (SQLException e){
            System.err.println(e.getMessage());
        }
    }

    /**
     * eldönti, hogy a paraméterül kapott játékos szerepel e az adatbázisban, ha nem hozzáfűzi,
     * ha igen, meghívja az updateScore()-t
     * @param winner
     */
    public void addEntity(String winner){
        try(Connection connection = DBConnectionSource.getInstance().getConnection();
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = statement.executeQuery(selectWholeSqlString)){
            HighScoreEntity entity = newEntity(winner,(getScore(winner)+1));
            if (entity.getScore()==1){
                rs.moveToInsertRow();
                getEntityAttributes(rs, entity);
                rs.insertRow();
            } else {
                updateScore(entity);
            }
        } catch(SQLException e){
            System.err.println(e.getMessage());
        }
    }

    /**
     * létrehoz egy új entitást
     * @param name játékos neve
     * @param score játékos pontja
     * @return a létrehozott entitást adja vissza
     */
    private HighScoreEntity newEntity(String name, int score) {
        HighScoreEntity entity = new HighScoreEntity();
        entity.setIdName(name);
        entity.setScore(score);
        return entity;
    }

    /**
     * a paraméterül kapott ResultSet-be a paraméterül kapott entitás adatait bemásolja
     * @param resultSet
     * @param entity
     * @throws SQLException
     */
    private void getEntityAttributes(ResultSet resultSet, HighScoreEntity entity) throws SQLException {
        resultSet.updateString(ID_ATTR_NAME, entity.getIdName());
        resultSet.updateInt(SCORE_ATTR_NAME, entity.getScore());
    }

    /**
     *  a paraméterül kapott entitásba a paraméterül kapott ResultSetből átmásolja attribútumokat
     * @param entity
     * @param resultSet
     * @throws SQLException
     */
    private void setEntityAttributes(HighScoreEntity entity, ResultSet resultSet) throws SQLException {
        entity.setIdName(resultSet.getString(ID_ATTR_NAME));
        entity.setScore(resultSet.getInt(SCORE_ATTR_NAME));
    }
}
