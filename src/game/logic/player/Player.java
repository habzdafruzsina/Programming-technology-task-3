package game.logic.player;

import game.logic.FieldObjects;

public class Player {

    private Direction direction;
    private Position position;

    private FieldObjects light;

    /**
     * Player konstruktor
     * inicializálja a Playerhez tartozó FieldObjectet
     * @param light
     */
    public Player(FieldObjects light){
        this.light = light;
    }

    public Direction getDirection() {
        return direction;
    }

    public Position getPosition() {
        return position;
    }

    public FieldObjects getLight() {
        return light;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
