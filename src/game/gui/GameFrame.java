package game.gui;

import game.GameConstants;
import game.gui.dialog.newgame.NewGameDialog;
import game.gui.dialog.newgame.OKCancelDialog;
import game.logic.player.Direction;
import game.logic.GameLogic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class GameFrame extends JFrame {

    private final GameLogic logic;
    private final GameInfoPanel gameInfoPanel;
    private final GameMenuBar gameMenuBar;
    private GameBoard gameBoard;
    private final NewGameDialog newGameDialog;

    public GameFrame(final GameLogic gameLogic) throws IOException {
        this.logic = gameLogic;
        setFrameProps();

        gameInfoPanel = new GameInfoPanel();
        getContentPane().add(gameInfoPanel, BorderLayout.NORTH);
        newGameDialog = new NewGameDialog(this, "New Game", gameInfoPanel);
        gameMenuBar = new GameMenuBar(this);
        setJMenuBar(gameMenuBar);
        gameBoard = new GameBoard(this, newGameDialog, logic);
        getContentPane().add(gameBoard, BorderLayout.CENTER);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent ke) {
                super.keyPressed(ke);
                if (!gameBoard.getGameHasStarted()) return;
                int keyCode = ke.getKeyCode();
                switch (keyCode){
                    case KeyEvent.VK_LEFT:  gameLogic.getPlayer1().setDirection(Direction.UP); break;
                    case KeyEvent.VK_RIGHT: gameLogic.getPlayer1().setDirection(Direction.DOWN); break;
                    case KeyEvent.VK_UP:    gameLogic.getPlayer1().setDirection(Direction.LEFT); break;
                    case KeyEvent.VK_DOWN:  gameLogic.getPlayer1().setDirection(Direction.RIGHT); break;
                    case KeyEvent.VK_W:     gameLogic.getPlayer2().setDirection(Direction.LEFT); break;
                    case KeyEvent.VK_S:     gameLogic.getPlayer2().setDirection(Direction.RIGHT); break;
                    case KeyEvent.VK_A:     gameLogic.getPlayer2().setDirection(Direction.UP); break;
                    case KeyEvent.VK_D:     gameLogic.getPlayer2().setDirection(Direction.DOWN); break;
                }
            }
        });
        pack();
        setLocationRelativeTo(null);
    }

    /**
     * beállítja a frame tulajdonságait (téma, bezárás, cím, ikon, layout...)
     */
    private void setFrameProps(){
        applyLookAndFeelTheme();
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                showExitConfirmDialog();
            }
        });
        setTitle("Tron Game");
        setIconImage(Toolkit.getDefaultToolkit().getImage("src/game/resources/icon.png"));
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(200, 200));
        setResizable(false);
    }

    /**
     * Egy megadott témát állít be az ablakra
     */
    private void applyLookAndFeelTheme(){
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if (GameConstants.LOOK_AND_FEEL_THEME.equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    newGameDialog.applyLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {
        }
    }

    /**
     * új játékot kezd (megállítja az információ időzítőjét, előhozza az új játék menü ablakot, amit ha leOK-zunk
     * új játékot indíttat el a logikában, és a GameBoard-ban)
     * starts new game in logic and in gameBoard
     */
    public void newGame(){
        gameInfoPanel.stopTimer();
        newGameDialog.setVisible(true);
        if ( newGameDialog.getButtonCode() != OKCancelDialog.OK ) {
            gameInfoPanel.continueTimer();
        } else {
            logic.newGame();
            gameBoard.newGame();
            pack();
        }
    }

    /**
     * Alkalmazás bezárására kérdez vissza, egy igen-nem dialog ablakkal
     */
    private void showExitConfirmDialog(){
        int answer = JOptionPane.showConfirmDialog(this, "Would you like to exit?",
                "Exit", JOptionPane.YES_NO_OPTION);
        if (answer == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}
