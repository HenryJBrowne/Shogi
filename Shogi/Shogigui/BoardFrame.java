package Shogigui;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JFrame;

/**
 * The BoardFrame class is used to create a JFrame window 
 * that stores the Board component / GUI
 * 
 * @Author Henry Browne – 37733273
 */
public class BoardFrame extends JFrame {

    private Component component;

    /**
     * The BoardFrame constructor method is used to intilize the jframe window
     * where board components resides
     * 
     * @param menu        The menu instance where board settings are / have been selected
     * @param customBoard True if the board component is a customizable piece layout
     *                    board, false if the board is a game board
     */
    public BoardFrame(Menu menu, Boolean customBoard) {

        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setTitle("Shogi");
        this.setResizable(false);

        if (!(customBoard)) {
            component = new Board(menu.getPlayerCol(), menu.getHintsOn(), menu.getTutorialOn(), this,
                    menu.getCustomPieces(), menu.getPlayerTurn());
        } else {
            CustomBoard custom = new CustomBoard(this, menu);
            component = (Component) custom;
        }

        this.add(component, BorderLayout.CENTER);

        menu.getMenuFrame().dispose();

        this.setLocation(200, 50);
        this.pack();
        this.setVisible(true);
    }
}
