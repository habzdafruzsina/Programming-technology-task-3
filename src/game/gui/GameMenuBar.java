package game.gui;

import game.gui.dialog.top10.Top10Frame;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.*;

public class GameMenuBar extends JMenuBar {

    private final GameFrame frame;

    /**
     * GameMenuBar kostruktora
     * két JMenu-t hoz létre
     * @param frame amihez tartozik
     */
    public GameMenuBar(GameFrame frame){
        this.frame = frame;

        JMenuBar menuBar = new JMenuBar();
        newGameItem.setMnemonic(KeyEvent.VK_N);
        top10Item.setMnemonic(KeyEvent.VK_T);

        menuBar.add(newGameItem);
        menuBar.add(top10Item);
        add(menuBar);
    }


    private JMenuItem newGameItem = new JMenuItem(new AbstractAction("New Game") {
        @Override
        public void actionPerformed(ActionEvent e) {
            frame.newGame();
            frame.setVisible(true);
        }
    });

    private JMenuItem top10Item = new JMenuItem(new AbstractAction("Top10") {
        @Override
        public void actionPerformed(ActionEvent e) {
            new Top10Frame();
        }
    });
}
