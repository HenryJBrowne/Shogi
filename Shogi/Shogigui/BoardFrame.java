package Shogigui;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JFrame;

public class BoardFrame extends JFrame {
    
    private Component component;
    
    public BoardFrame(Menu menu, Boolean customBoard)
    {

        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setTitle("Shogi");
        this.setResizable(false);

        if (!(customBoard)){
            component = new Board(menu.getPlayerCol(), menu.getHintsOn(), menu.getTutorialOn(),this,menu.getCustomPieces(),menu.getPlayerTurn());
        }else{
            CustomBoard custom = new CustomBoard(this,menu);
            component= (Component) custom;
        }

        this.add(component, BorderLayout.CENTER);

        menu.getMenuFrame().dispose();
        
        this.setLocation(200, 50);
        this.pack();
        this.setVisible(true);
    }

}
