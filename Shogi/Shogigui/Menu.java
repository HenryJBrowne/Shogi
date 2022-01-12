package Shogigui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class Menu extends JComponent {
    
    private ArrayList<ImageFactory> Menu_Images;

    private final int Button_Height = 100; 
    private final int Button_Width = 300;

    private final String Menu_Images_File_path= "images" + File.separator + "menu" + File.separator;
    private final String Background_Image_File_Path= Menu_Images_File_path + "Background.png";
    private final String Play_Button_File_Path = Menu_Images_File_path + "PlayButton.png";
    private final String Controls_Button_File_Path = Menu_Images_File_path + "ControlsButton.png";
    private final String Quit_Button_File_Path = Menu_Images_File_path + "QuitButton.png";
    private final String Hints_ButtonON_File_Path = Menu_Images_File_path + "HintsONButton.png";
    private final String Hints_ButtonOFF_File_Path = Menu_Images_File_path + "HintsOffButton.png";
    private final String Tutorial_ButtonON_File_Path = Menu_Images_File_path + "TutorialOnButton.png";
    private final String Tutorial_ButtonOFF_File_Path = Menu_Images_File_path + "TutorialOffButton.png";
    private final String White_Button_File_Path = Menu_Images_File_path + "WhiteButton.png";
    private final String Black_Button_File_Path = Menu_Images_File_path + "BlackButton.png";

    private String Hints_Button_File_Path;
    private String Tutorial_Button_File_Path;
    private String PlayerCol_Button_File_Path;

    public boolean PlayPressed = false;   // encapsulate and add accessor methods?...
    public boolean PlayerIsWhite = true;
    public boolean HintsOn = true;
    public boolean TutorialOn = true;
    

    public Menu() {

        Menu_Images = new ArrayList<ImageFactory>();

        this.setBackground(new Color(255, 255, 255));
        this.setPreferredSize(new Dimension(1100, 580));
        this.setMinimumSize(new Dimension(100, 100));
        this.setMaximumSize(new Dimension(1000, 1000));

        this.addMouseListener(mouseAdapter);

        this.setVisible(true);
        this.requestFocus();

        drawMenu();
    }

    private void drawMenu() {

        Menu_Images.clear();

        // add static buttons

        Menu_Images.add(new ImageFactory(Play_Button_File_Path, 75, 275)); 
        
        Menu_Images.add(new ImageFactory(Controls_Button_File_Path, 400,275));

        Menu_Images.add(new ImageFactory(Quit_Button_File_Path, 725,275));

        // add non static buttons 

        if (HintsOn) {
            Hints_Button_File_Path = Hints_ButtonON_File_Path;
        } else {
            Hints_Button_File_Path = Hints_ButtonOFF_File_Path;
        }

        Menu_Images.add(new ImageFactory(Hints_Button_File_Path, 75, 400));

        if (TutorialOn) {
            Tutorial_Button_File_Path = Tutorial_ButtonON_File_Path;
        } else {
            Tutorial_Button_File_Path = Tutorial_ButtonOFF_File_Path;
        }

        Menu_Images.add(new ImageFactory(Tutorial_Button_File_Path, 400, 400));

        if (PlayerIsWhite) {
            PlayerCol_Button_File_Path = White_Button_File_Path;
        } else {
            PlayerCol_Button_File_Path = Black_Button_File_Path;
        }

        Menu_Images.add(new ImageFactory(PlayerCol_Button_File_Path, 725, 400));

        this.repaint();
    }

    private boolean toggle(boolean Setting) {

        if (Setting == true) {
            return false;
        } else {
            return true;
        }
    }

    private MouseAdapter mouseAdapter = new MouseAdapter() {

        @Override
        public void mousePressed(MouseEvent e) {
            int mouse_X = e.getX();
            int mouse_Y = e.getY();

            // detect button pressed

            for (ImageFactory image : Menu_Images) {

                if (mouse_X > image.getRect().getX() && mouse_X < image.getRect().getX() + Button_Width
                        && mouse_Y > image.getRect().getY() && mouse_Y < image.getRect().getY() + Button_Height) {

                    // check what button was pressed

                    if (image.getFilePath() == Play_Button_File_Path) {
                        PlayPressed=true;
                    }
                    if (image.getFilePath() == Controls_Button_File_Path) {
                        // + + 
                    }
                    if (image.getFilePath() == Quit_Button_File_Path) {
                        System.exit(1);
                    }
                    if (image.getFilePath() == Hints_Button_File_Path) {
                        HintsOn= toggle(HintsOn);
                    }
                    if (image.getFilePath() == Tutorial_Button_File_Path) {
                        TutorialOn= toggle(TutorialOn);
                    }
                    if (image.getFilePath() == PlayerCol_Button_File_Path) {
                        PlayerIsWhite= toggle(PlayerIsWhite);
                    }
                }
            }

            drawMenu();
        }
    };

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        addBackground(g2);
        addImages(g2);
    }

    private void addBackground(Graphics2D g2) {

        ImageFactory Background= new ImageFactory(Background_Image_File_Path, 0 , 0);
        Background.drawImage(g2);

    }

    private void addImages(Graphics2D g2) {
        for (ImageFactory image : Menu_Images) {
            image.drawImage(g2);
        }
    }
}
