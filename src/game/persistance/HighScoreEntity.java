package game.persistance;

public class HighScoreEntity {

    private String idName;
    private int score;

    public String getIdName() {
        return idName;
    }

    public void setIdName(String id) {
        this.idName = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }

        if (!(object.getClass().equals(this.getClass()))) {
            return false;
        }

        final HighScoreEntity other = (HighScoreEntity) object;
        return (this.getIdName() != null || other.getIdName() == null)
                && (this.getIdName() == null || this.getIdName().equals(other.getIdName()));
    }

    @Override
    public int hashCode() {
        int hash = 0;
        final String id = getIdName();
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }
}
