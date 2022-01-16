package Shogigui;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JFrame;

public class MenuFrame extends JFrame {
    
    private Component component;
    public Menu menu;               //encapsulate and add accessor method?

    public MenuFrame()
    {
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setTitle("Shogi Menu");
        this.setResizable(false);
        component = new Menu(this);
        this.add(component, BorderLayout.CENTER);

        menu= (Menu) component;

        this.setLocation(200, 50);
        this.pack();
        this.setVisible(true);
    }
}
