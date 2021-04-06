package game;
import game.gui.GameFrame;
import game.logic.GameLogic;

import java.io.IOException;

public class Boot {

    public static void main(String args[]){

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new GameFrame(new GameLogic()).setVisible(true);
                }catch(IOException e){
                    System.err.println("Képbetöltési hiba");
                }
            }
        });
    }
}
