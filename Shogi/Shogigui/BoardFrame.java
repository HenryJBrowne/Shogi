package Shogigui;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JFrame;

public class BoardFrame extends JFrame {
    
    private Component component;
    private boolean PlayerCol;
    private boolean HintsOn;
    private boolean TutorialOn;
    
    public BoardFrame(boolean PlayerCol, boolean HintsOn, boolean TutorialOn)
    {
        this.PlayerCol=PlayerCol;
        this.HintsOn=HintsOn;
        this.TutorialOn=TutorialOn;

        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setTitle("Shogi");
        this.setResizable(false);
        component = new Board(this.PlayerCol, this.HintsOn, this.TutorialOn);
        this.add(component, BorderLayout.CENTER);
        
        this.setLocation(200, 50);
        this.pack();
        this.setVisible(true);
    }

}
