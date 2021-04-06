package game.gui.dialog.newgame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public abstract class OKCancelDialog extends JDialog {
    public static final int     OK = 1;
    public static final int     CANCEL = 0;

    protected int       btnCode;
    protected JPanel    btnPanel ;
    protected JButton   btnOK;
    protected JButton   btnCancel;

    /**
     * Egy párbeszédablak létrehozása
     * @param frame a keret, amihez a párbeszédablak tartozik
     * @param name a párbeszédablak címe
     */
    protected OKCancelDialog(JFrame frame, String name)
    {
        super(frame, name, true);
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        btnCode = CANCEL; //??

        btnOK = new JButton(actionOK);
        btnOK.setMnemonic('O');
        btnOK.setPreferredSize(new Dimension(90, 25));

        btnCancel = new JButton(actionCancel);
        KeyStroke cancelKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        InputMap inputMap = btnCancel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = btnCancel.getActionMap();
        if (inputMap != null && actionMap != null)
        {
            inputMap.put(cancelKeyStroke, "cancel");
            actionMap.put("cancel", actionCancel);
        }
        btnCancel.setPreferredSize(new Dimension(90, 25));

        getRootPane().setDefaultButton(btnOK); //???
        btnPanel = new JPanel(new FlowLayout());
        btnPanel.add(btnOK);
        btnPanel.add(btnCancel);

        setLocationRelativeTo(null);
    }

    /**
     * Az ablak bezárását okozó gomb lekérdezése
     * @return a gomb kódja
     */
    public int getButtonCode() { return btnCode; }

    /**
     * az OK megnyomásakor elvégzendő ellenőrzések, műveletek
     * @return true, ha a gomb lenyomása elfogadott
     */
    protected abstract boolean processOK();

    /**
     * a Cancel megnyomásakor elvégzendő tevékenységek
     */
    protected abstract void processCancel();

    /**
     * Az OK gomb eseménykezelője
     */
    private AbstractAction  actionOK = new AbstractAction("OK")
    {
        public void actionPerformed(ActionEvent e)
        {
            if ( processOK() )
            {
                btnCode = OK;
                OKCancelDialog.this.setVisible(false);
            }
        }
    };

    /**
     * A Mégsem gomb esemény kezelője
     */
    private AbstractAction actionCancel = new AbstractAction("Cancel")
    {
        public void actionPerformed(ActionEvent e)
        {
            processCancel();
            btnCode = CANCEL;
            OKCancelDialog.this.setVisible(false);
        }
    };
}
