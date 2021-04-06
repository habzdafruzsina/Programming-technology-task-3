package game.gui.dialog.top10;

import game.GameConstants;
import game.persistance.DataSource;
import game.persistance.HighScoreEntity;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Top10Frame extends JFrame {

    private final static JLabel TITLE_LABEL = new JLabel(" Top list: ");

    /**
     * Létrehoz egy top10 ablakot
     */
    public Top10Frame(){
        setFrameProps();
        addContent();
        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Beállítja az ablak tulajdonságait
     */
    private void setFrameProps(){
        applyLookAndFeelTheme();
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setIconImage(Toolkit.getDefaultToolkit().getImage("resources/icon.png"));
        setResizable(false);
        setVisible(true);
    }

    /**
     * Hozzáadja az ablakhoz a címet, és egy szöveg területet
     */
    private void addContent(){
        TITLE_LABEL.setFont(GameConstants.SCORE_TITLE_FONT);
        getContentPane().add(TITLE_LABEL, BorderLayout.NORTH);

        JTextArea textArea = new JTextArea();
        textArea.setFont(GameConstants.SCORES_FONT);
        textArea.setText(getScores());
        textArea.setEditable(false);
        getContentPane().add(textArea, BorderLayout.CENTER);
    }

    /**
     * Lekéri az adatbázisból a toplistát
     * @return szöveges formában visszaadja (sorszám név pontszám)
     */
    private String getScores(){
        ArrayList<HighScoreEntity> highScores = DataSource.getInstance().getHighScoreDao().getTop10();

        StringBuilder sb = new StringBuilder();
        for(int i=0; i<highScores.size(); i++){
            HighScoreEntity entity = highScores.get(i);
            sb.append("  " + (i+1) + ".  " + entity.getIdName() + "   " + entity.getScore() + "  ");
            sb.append(System.lineSeparator());
        }
        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }

    /**
     * Egy megadott témát állít be az ablakra
     */
    private void applyLookAndFeelTheme(){
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if (GameConstants.LOOK_AND_FEEL_THEME.equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {
        }
    }

}
