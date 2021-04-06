package game.logic;

import game.GameConstants;
import game.logic.player.Direction;
import game.logic.player.Player;
import game.logic.player.Position;

import java.util.Random;

public class GameLogic {

    private Random rand = new Random();
    private FieldObjects[][] board;
    private Player player1 = new Player(FieldObjects.LIGHT1);
    private Player player2 = new Player(FieldObjects.LIGHT2);

    public GameLogic(){}

    /**
     * Új játék
     * (inicializálja a játékosok pozíióját)
     */
    public void newGame(){
        initBoard();
        player1.setPosition(new Position(0,0));
        player2.setPosition(new Position(0,0));
        setPlayerStartPosition(player1);
        setPlayerStartPosition(player2);
    }

    /**
     * a board változót megtölti FieldObjectekkel (széleken fallal, középen talapzattal)
     */
    private void initBoard(){
        board = new FieldObjects[GameConstants.BOARD_SIZE][GameConstants.BOARD_SIZE];
        for (int row = 0; row < GameConstants.BOARD_SIZE; ++row) {
            for (int column = 0; column < GameConstants.BOARD_SIZE; ++column) {
                if (row==0 || column==0 || row==GameConstants.BOARD_SIZE-1 || column==GameConstants.BOARD_SIZE-1)
                {
                    board[row][column] = FieldObjects.WALL;
                } else {
                    board[row][column] = FieldObjects.EMPTY;
                }
            }
        }
    }

    /**
     *
     * @param player
     */
    private void setPlayerStartPosition(Player player){
        int randomRow = rand.nextInt(2);
        int[] randomNums = generatePosition();

        if (randomRow == 1){
            while ((player1.getPosition().getX()==randomNums[0] && player1.getPosition().getY()==randomNums[1]) ||
                    (player2.getPosition().getX()==randomNums[0] && player2.getPosition().getY()==randomNums[1]) ){
                randomNums = generatePosition();
            }
            setPlayerProps(player, randomNums[0], randomNums[1]);
        } else {
            while ((player1.getPosition().getX()==randomNums[1] && player1.getPosition().getY()==randomNums[0]) ||
                    (player2.getPosition().getX()==randomNums[1] && player2.getPosition().getY()==randomNums[0]) ){
                randomNums = generatePosition();
            }
            setPlayerProps(player, randomNums[1], randomNums[0]);
        }
    }

    private int[] generatePosition(){
        int[] nums = new int[2];
        nums[0] = rand.nextInt(GameConstants.BOARD_SIZE-3)+1; //szinte bármi
        nums[1] = rand.nextInt(2); //vagy 1 vagy n-2
        if (nums[1] == 0)  nums[1] = (GameConstants.BOARD_SIZE-2);
        return nums;
    }

    /**
     * attól függően, hogy hol helyezkedik al a játékos a táblán, beállítja a kezdetleges haladási irányát
     * @param player
     * @param row
     * @param column
     */
    public void setPlayerProps(Player player, int row, int column){
        board[row][column] = player.getLight();
        player.setPosition(new Position(row, column));

        if (column == 1){player.setDirection(Direction.DOWN);}
        else if (column == GameConstants.BOARD_SIZE-2) {player.setDirection(Direction.UP);}
        else if (row == 1) {player.setDirection(Direction.RIGHT);}
        else if (row == GameConstants.BOARD_SIZE-2) {player.setDirection(Direction.LEFT);}
    }

    /**
     * mozgatja a játékost eggyel abba az irányba, amerre halad, ha ez lehetséges
     * @param player mozgatott játékos
     * @return igaz, ha lehetett mozgatni a játékost
     */
    public boolean movePlayer(Player player){
        Position previous = player.getPosition();
        Position next = previous.move(player.getDirection());
        if (isCauseOfLose(next)) {
            return false;
        } else {
            player.setPosition(next);
            board[player.getPosition().getX()][player.getPosition().getY()] = player.getLight();
            return true;
        }
    }

    public Player getPlayer1(){return player1;}
    public Player getPlayer2(){return player2;}

    public FieldObjects getFiledObject(int i, int j){ return board[i][j]; }

    /**
     * vizsgálja, hogy a következő helyre lehet e lépni
     * @param nextField
     * @return igaz, ha lehet a megadott helyre lépni
     */
    private boolean isCauseOfLose(Position nextField){
        if (board[nextField.getX()][nextField.getY()] == FieldObjects.WALL ||
                board[nextField.getX()][nextField.getY()] == FieldObjects.LIGHT1 ||
                board[nextField.getX()][nextField.getY()] == FieldObjects.LIGHT2 ) {
            return true;
        } else return false;
    }

}
