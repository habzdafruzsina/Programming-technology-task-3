package game.gui;

import game.GameConstants;

import javax.swing.*;
import java.time.Duration;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.Instant;

public class GameInfoPanel extends JPanel {

    private static Timer timer;
    private static JLabel timeLabel = new JLabel("");
    private static JLabel player1NameLabel = new JLabel("");
    private static JLabel player2NameLabel = new JLabel("");
    private static final JLabel VS_LABEL = new JLabel(" VS ");

    /**
     * GameInfoPanel konstruktora
     * beállítja a méretet, szövegek stílusát
     */
    public GameInfoPanel(){
        timer = new Timer(1000, new TimerAction(timeLabel));

        VS_LABEL.setFont(GameConstants.VS_FONT);
        player1NameLabel.setFont(GameConstants.NAME_FONT);
        player2NameLabel.setFont(GameConstants.NAME_FONT);
        timeLabel.setFont(GameConstants.TIME_FONT);

        setPreferredSize( new Dimension( GameConstants.INFO_PANEL_WIDTH, GameConstants.INFO_PANEL_HEIGHT ) );
    }

    /**
     * inicializálja a szövegek tartalmát
     * @param name1 első játékos neve
     * @param name2 második játékos neve
     */
    public void newGame(String name1, String name2){
        removeAll();
        addLabels();
        player1NameLabel.setText(name1);
        player2NameLabel.setText(name2);

        if(timer != null) {
            timer.stop();
        }
        timeLabel.setText("00:00");
        timer = new Timer(1000, new TimerAction (timeLabel));
        timer.start();
    }

    /**
     * hozzáadja a panelhez a szövegeket
     */
    private void addLabels(){
        add(timeLabel);
        add(player1NameLabel);
        add(VS_LABEL);
        add(player2NameLabel);
    }

    /**
     * megállítja az időzítőt
     */
    public void stopTimer(){
        timer.stop();
    }

    /**
     *
     */
    public void continueTimer(){
        timer.start();
    }

    private class TimerAction extends AbstractAction{

        private final JLabel timerLabel;
        private final Instant startTime;

        public TimerAction(final JLabel timerLabel) {
            this.timerLabel = timerLabel;
            this.startTime = Instant.now();
        }

        /**
         * beállítja az időzítő szövegét
         * @param ae
         */
        @Override
        public void actionPerformed(ActionEvent ae) {
            timerLabel.setText(formatDuration(Duration.between(startTime, Instant.now())));
        }

        /**
         * adott formátumra alakítja az eltelt időt
         * @param duration eltelt idő
         * @return adott formátumú szöveget az vissza
         */
        private String formatDuration(final Duration duration){
            final long seconds = duration.getSeconds();
            return String.format("%02d:%02d", (seconds % 3600) /60, seconds % 60);
        }
    }
}
