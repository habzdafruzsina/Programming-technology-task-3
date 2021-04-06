package game.gui.dialog.newgame;

import game.gui.GameInfoPanel;
import game.logic.player.Colors;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class NewGameDialog extends OKCancelDialog{

    private final GameInfoPanel gameInfoPanel;

    private JRadioButton[] colorButtons1;
    private JRadioButton[] colorButtons2;
    private int player1Selected = -1;
    private int player2Selected = -1;

    private static final JLabel NAME1_LABEL = new JLabel("Name of Player1: ");
    private static final JLabel NAME2_LABEL = new JLabel("Name of Player2: ");
    private static JTextField name1TextField = new JTextField("");
    private static JTextField name2TextField = new JTextField("");


    /**
     * Létrehoz egy új párbeszédablakot, két szöveg beviteli mezővel, és radiobutton-ökkel
     * @param frame ehhez tartozik az ablak
     * @param name a fejléc tartalma
     * @param gameInfoPanel információs panel, melyet új játék indításánál megváltoztat
     */

    public NewGameDialog(JFrame frame, String name, GameInfoPanel gameInfoPanel){
        super(frame, name);
        this.gameInfoPanel = gameInfoPanel;

        JPanel namePanel = new JPanel(new GridLayout(2, 2));
        namePanel.setBorder(new TitledBorder(new EtchedBorder(), "Name: "));

        namePanel.add(NAME1_LABEL);
        namePanel.add(NAME2_LABEL);
        namePanel.add(name1TextField);
        namePanel.add(name2TextField);

        JPanel colorPanel = new JPanel(new GridLayout(1, 2));
        colorPanel.setBorder(new TitledBorder(new EtchedBorder(), "Colors: "));

        ButtonGroup buttonGroup1 = new ButtonGroup();
        ButtonGroup buttonGroup2 = new ButtonGroup();
        colorButtons1 = new JRadioButton[Colors.getNumberOfColors()];
        colorButtons2 = new JRadioButton[Colors.getNumberOfColors()];
        Box verticalBox1 = Box.createVerticalBox();
        Box verticalBox2 = Box.createVerticalBox();

        createButtons(colorButtons1, buttonGroup1, verticalBox1);
        createButtons(colorButtons2, buttonGroup2, verticalBox2);

        colorPanel.add(verticalBox1);
        colorPanel.add(verticalBox2);

        setLayout(new BorderLayout());
        add("North", namePanel);
        add("Center", colorPanel);
        add("South", btnPanel);

        pack();
        setResizable(false);
        setLocationRelativeTo(null);
    }

    /**
     * radiobutton-öket hoz létre, minden színhez
     * @param colorButtons tömb, melyhez hozzáfűzi a radiobutton-öket
     * @param buttonGroup csoport, melybe belerakja a létrehozott gombot
     * @param box doboz, melybe belerakja a létrehozott gombot
     */
    private void createButtons(JRadioButton[] colorButtons, ButtonGroup buttonGroup, Box box){
        int i=0;
        for (Colors color: Colors.values()){
            colorButtons[i] = RadioButton(color);
            box.add(colorButtons[i]);
            buttonGroup.add(colorButtons[i]);
            i++;
        }
    }

    /**
     * Egy paraméterben megadott témát állít be az ablakra
     * @param theme
     */
    public void applyLookAndFeel(String theme){
        try{
            UIManager.setLookAndFeel(theme);
        }catch(Exception ignored){}
    }

    public int getPlayer1Color() {return player1Selected;}
    public int getPlayer2Color() {return player2Selected;}

    public String getPlayer1Name() {return name1TextField.getText();}
    public String getPlayer2Name() {return name2TextField.getText();}

    /**
     * OK gomb lenyomására adott parancsot hajt végre
     * itt figyelmeztet, ha valami nincs rendben
     * ha rendben van minden, megváltoztatja az információs panel tartalmát
     * @return sikeres volt e a paracs végrehajtása
     */
    @Override
    protected boolean processOK()
    {
        if(areNamesNotTheSame() == false || areColorsNotTheSame() == false){
            JOptionPane.showMessageDialog(this, "The name or the color should be dissimilar.", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        } else if(!(isNameNotEmpty())){
            JOptionPane.showMessageDialog(this, "You didn't give a name.", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        } else {
            for (int i = 0; i < colorButtons1.length; i++){
                if(colorButtons1[i].isSelected()){
                    player1Selected = i;
                }
                if(colorButtons2[i].isSelected()){
                    player2Selected = i;
                }
            }
            if(colorNotSelected()){
                JOptionPane.showMessageDialog(this, "You didn't choose a color.", "Warning", JOptionPane.WARNING_MESSAGE);
                return false;
            } else {
                gameInfoPanel.newGame(getPlayer1Name(), getPlayer2Name());
                return true;
            }
        }
    }

    /**
     *
     * @return igaz, ha nem ugyanaz a két megadott név
     */
    private boolean areNamesNotTheSame(){
        return (!(name1TextField.getText().equals(name2TextField.getText())));
    }

    /**
     *
     * @return igaz, ha nem ugyanazok a színek vannak kiválasztva
     */
    private boolean areColorsNotTheSame(){
        for (int i = 0; i < colorButtons1.length; i++){
            if(colorButtons1[i].isSelected() && colorButtons2[i].isSelected()){
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @return igaz, ha van név megadva
     */
    private boolean isNameNotEmpty(){
        return (!(name1TextField.getText().equals("") && name2TextField.getText().equals("")));
    }

    /**
     *
     * @return igaz, ha nincs kiválasztva szín
     */
    private boolean colorNotSelected(){ return (player1Selected==-1 || player2Selected==-1);}

    /**
     * Cancel gomb lenyomása esetén hajt végre parancsot
     * Jelen esetben nem csinál semmit
     */
    @Override
    protected void processCancel(){
    }

    /**
     * Egy színnek megfelelő rádiógombot hoz létre
     * @return rádiógomb
     */
    private JRadioButton RadioButton(Colors color){
        JRadioButton button = new JRadioButton(color.getColorName());
        return button;
    }

}
