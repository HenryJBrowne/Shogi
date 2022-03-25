package Shogigui;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JFrame;

public class MenuFrame extends JFrame {

    private Component component; 

    /**
     * The MenuFrame constructor method is used to intilize the jframe window
     * where menu components resides
     * 
     */
    public MenuFrame() {
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setTitle("Shogi Menu");
        this.setResizable(false);
        component = new Menu(this);
        this.add(component, BorderLayout.CENTER);
        this.setLocation(200, 50);
        this.pack();
        this.setVisible(true);
    }
}
