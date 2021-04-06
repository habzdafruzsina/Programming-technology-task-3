package game.logic.player;

public class Position {
    private int x, y;

    /**
     * Position konstruktor
     * @param x
     * @param y
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Position-t változtatja a megadott Direction alapján
     * @param d
     * @return
     */
    public Position move(Direction d){
        return new Position(x + d.x, y + d.y);
    }

    public int getX(){return x;}
    public int getY(){return y;}
}
