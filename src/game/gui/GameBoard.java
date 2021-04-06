package game.gui;

import game.GameConstants;
import game.gui.dialog.newgame.NewGameDialog;
import game.logic.player.Colors;
import game.logic.FieldObjects;
import game.io.ImageLoader;
import game.logic.GameLogic;
import game.logic.player.Position;
import game.persistance.DataSource;

import javax.swing.*;
import java.awt.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;

public class GameBoard extends JPanel{

    private final GameFrame frame;
    private final NewGameDialog newGameDialog;
    private final GameLogic logic;
    private Image floor, wall, player, coverImage;
    private int boardUnitInPixels;
    private Colors light1;
    private Colors light2;
    private Timer timer;
    private boolean gameHasStarted;
    private boolean gameBoardReady;
    private ArrayList<Position> light1Pos = new ArrayList<>();
    private ArrayList<Position> light2Pos = new ArrayList<>();

    /**
     * GameBoard kontruktora, beállítja a Layout-ot, és a méretet
     * @param frame amihez tartozik a GameBoard
     * @param newGameDialog
     * @param logic
     * @throws IOException képbetöltés miatt keletkezhet
     */
    public GameBoard(GameFrame frame, NewGameDialog newGameDialog, GameLogic logic)throws IOException {
        this.frame = frame;
        this.newGameDialog = newGameDialog;
        this.logic = logic;
        gameHasStarted = false;
        gameBoardReady = false;

        loadImages();

        setLayout(new GridLayout(GameConstants.BOARD_SIZE, GameConstants.BOARD_SIZE));
        setPreferredSize(new Dimension(boardUnitInPixels*GameConstants.BOARD_SIZE,
                boardUnitInPixels*GameConstants.BOARD_SIZE));
    }

    public boolean getGameHasStarted(){return gameHasStarted;}

    /**
     * Betölti a képeket
     * @throws IOException képbetöltés miatt keletkezhet
     */
    public void loadImages() throws IOException{
        floor = ImageLoader.loadImage("src/game/resources/floor.jpg");
        wall = ImageLoader.loadImage("src/game/resources/wall.jpg");
        player = ImageLoader.loadImage("src/game/resources/player.png");
        coverImage = ImageLoader.loadImage("src/game/resources/cover1.jpg");
        boardUnitInPixels = floor.getWidth(null);
    }

    /**
     * Új játékot kezd, inicializálja az időzítőt
     */
    public void newGame(){
        timer = new Timer(1000, timerAction);
        timer.start();
        removeAll();
        initLights();
        gameHasStarted = true;
    }

    /**
     * a szín tömbökbe belerakja az első pozíciót, lekéri a játékosok által választott színt a NewGameDailog-tól
     */
    private void initLights(){
        light1Pos = new ArrayList<>();
        light2Pos = new ArrayList<>();
        updateLightsArray();
        light1 = Colors.values()[newGameDialog.getPlayer1Color()];
        light2 = Colors.values()[newGameDialog.getPlayer2Color()];
    }

    /**
     * felrajzolja a táblát (falakat, talapzatot) logic.getFieldObject() szerint
     * @param g Graphics2D objektum, ami a rajzolásért felelős
     */
    private void setupBoard(Graphics2D g){
        Image image = null;
        for (int row = 0; row < GameConstants.BOARD_SIZE; ++row) {
            for (int column = 0; column < GameConstants.BOARD_SIZE; ++column) {
                FieldObjects fieldObjects = logic.getFiledObject(column, row);
                switch (fieldObjects){
                    case WALL: image = wall; break;
                    case LIGHT1: image = floor; break;
                    case LIGHT2: image = floor; break;
                    case EMPTY: image = floor; break;
                }
                g.drawImage(image, column * boardUnitInPixels, row * boardUnitInPixels, boardUnitInPixels, boardUnitInPixels, this);
            }
        }
        gameBoardReady = true;
    }

    /**
     * hozzáfűzi a következő pozíciót a szín listákhoz
     */
    private void updateLightsArray(){
        Position player1Position = new Position(logic.getPlayer1().getPosition().getY()*boardUnitInPixels + boardUnitInPixels/2,
                logic.getPlayer1().getPosition().getX()*boardUnitInPixels + boardUnitInPixels/2);
        light1Pos.add(player1Position);

        Position player2Position = new Position(logic.getPlayer2().getPosition().getY()*boardUnitInPixels + boardUnitInPixels/2,
                logic.getPlayer2().getPosition().getX()*boardUnitInPixels + boardUnitInPixels/2);
        light2Pos.add(player2Position);
    }

    /**
     * Megrajzolja a szín tömb szerint a vonalakat, adott színnel
     * @param g Graphics2D objektum, ami a rajzolásért felelős
     * @param light a megadott színű vonalat rajzol
     */
    private void paintLights(Graphics2D g, Colors light){
        BasicStroke lightStroke = new BasicStroke(8,
                BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
        g.setStroke(lightStroke);
        g.setColor(light.getColor());

        ArrayList<Position> lightPos;
        if (light.equals(light1)){
            lightPos = light1Pos;
        } else {
            lightPos = light2Pos;
        }

        for(int i=0; i<lightPos.size()-1; i++){
            g.drawLine(lightPos.get(i).getX(), lightPos.get(i).getY(), lightPos.get(i+1).getX(), lightPos.get(i+1).getY());
        }
    }

    /**
     * Felrajzolja a playereket a helyükre
     * @param g Graphics2D objektum, ami a rajzolásért felelős
     */
    private void paintPlayers(Graphics2D g){
        g.drawImage(player,logic.getPlayer1().getPosition().getY()*boardUnitInPixels,
                logic.getPlayer1().getPosition().getX()*boardUnitInPixels,
                boardUnitInPixels, boardUnitInPixels, null);

        g.drawImage(player,logic.getPlayer2().getPosition().getY()*boardUnitInPixels,
                logic.getPlayer2().getPosition().getX()*boardUnitInPixels,
                boardUnitInPixels, boardUnitInPixels, null);
    }

    /**
     * Felrajzolja az egész játékteret
     * @param g Graphics2D objektum, ami a rajzolásért felelős
     */
    private void updateBoard(Graphics2D g){
        setupBoard(g);

        updateLightsArray();
        paintLights(g, light1);
        paintLights(g, light2);

        paintPlayers(g);

        g.dispose();
    }

    /**
     * Játék kezdete előtti borító és cím felrajzolásáért felelős
     * @param g Graphics2D objektum, ami a rajzolásért felelős
     */
    private void setupCover(Graphics2D g){
        g.setColor(GameConstants.TITLE_COLOR);
        g.setFont(GameConstants.TITLE_FONT);
        g.drawString("TRON",(boardUnitInPixels*GameConstants.BOARD_SIZE/2)-95,
                GameConstants.TITLE_POS_Y);
        g.drawImage(coverImage,GameConstants.COVER_IMG_POS_X,GameConstants.COVER_IMG_POS_Y,
                boardUnitInPixels*GameConstants.BOARD_SIZE, (int)(boardUnitInPixels*GameConstants.BOARD_SIZE*0.8), null);
    }


    /**
     * a tábla felrajzolását végzi, ha még nem kezdődött el a játék, borítót rajzol, ha már folyik a játék, akkor táblát
     * ezen kívül mozgatja a játékosokat a logic.movePlayer()-rel, és vizsgálja hogy véget ért e a játék
     * @param g Graphics objektum, ami a rajzolásért felelős
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if(gameHasStarted) {
            if (gameBoardReady==false){
                updateBoard(g2);
            } else {
                if (logic.movePlayer(logic.getPlayer1())==false){
                    endGame(newGameDialog.getPlayer2Name());
                }
                if (logic.movePlayer(logic.getPlayer2())==false){
                    endGame(newGameDialog.getPlayer1Name());
                }
                updateBoard(g2);
            }
        }else{
            setupCover(g2);
        }
    }


    private final Action timerAction = new AbstractAction() {
        /**
         * adott időközönként újrafesti a GameBoard-ot repaint() meghívásával
         * @param e
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            repaint();
        }
    };

    /**
     * Játék végén kiírja egy dialog segítségével kiírja a győztest
     * @param winner a győztes neve
     */
    private void showGameOverMessage(String winner) {
        JOptionPane.showMessageDialog(this, "The winner: " + winner, "Congratulations!", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Befejezi a játékot, új játékot kezd
     * (megállítja az időzítőt, elmenti a nyertest az adatbázisba, meghívja a showGameOverMessage()-t,
     * és frame.newGame() segítségével új játékot kezd)
     * @param winner a győztes neve
     */
    private void endGame(String winner){
        timer.stop();
        gameHasStarted = false;
        gameBoardReady = false;
        DataSource.getInstance().getHighScoreDao().addEntity(winner);
        showGameOverMessage(winner);
        frame.newGame();
    }

}
