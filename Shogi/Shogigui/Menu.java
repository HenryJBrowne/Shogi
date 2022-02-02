package Shogigui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

import Shogigui.pieces.Piece;

public class Menu extends JComponent {
    
    private ArrayList<ImageFactory> Menu_Images;

    private final String Menu_Images_File_path= "images" + File.separator + "menu" + File.separator;
    private final String Background_Image_File_Path= Menu_Images_File_path + "Background.png";
    private final String Play_Button_File_Path = Menu_Images_File_path + "PlayButton.png";
    private final String Controls_Button_File_Path = Menu_Images_File_path + "ControlsButton.png"; // ++ change to info button
    private final String Quit_Button_File_Path = Menu_Images_File_path + "QuitButton.png";
    private final String Hints_ButtonON_File_Path = Menu_Images_File_path + "HintsONButton.png";
    private final String Hints_ButtonOFF_File_Path = Menu_Images_File_path + "HintsOffButton.png";
    private final String Tutorial_ButtonON_File_Path = Menu_Images_File_path + "TutorialOnButton.png";
    private final String Tutorial_ButtonOFF_File_Path = Menu_Images_File_path + "TutorialOffButton.png";
    private final String White_Button_File_Path = Menu_Images_File_path + "WhiteButton.png";
    private final String Black_Button_File_Path = Menu_Images_File_path + "BlackButton.png";
    private final String Custom_Board_Button_File_Path = Menu_Images_File_path + "CustomBoardButton.png";

    private String Hints_Button_File_Path;
    private String Tutorial_Button_File_Path;
    private String PlayerCol_Button_File_Path;

    public boolean PlayPressed = false;   // encapsulate and add accessor methods?...
    public boolean PlayerIsWhite = true;
    public boolean HintsOn = true;
    public boolean TutorialOn = true;

    private ArrayList<Piece> customPieces;
    private boolean turn_is_white=true;
    
    
    MenuFrame Frame;

    public Menu(MenuFrame Frame) {

        Menu_Images = new ArrayList<ImageFactory>();
        this.Frame=Frame;

        this.setBackground(new Color(255, 255, 255));
        this.setPreferredSize(new Dimension(1100, 580));
        this.setMinimumSize(new Dimension(100, 100));
        this.setMaximumSize(new Dimension(1000, 1000));

        this.addMouseListener(new MouseListener(this));

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

        Menu_Images.add(new ImageFactory(Custom_Board_Button_File_Path,250,200));

        this.repaint();
    }

    private boolean toggle(boolean Setting) {

        return (Setting == true)?false:true;

    }

    class MouseListener extends MouseAdapter {   // ++ make neater

        Menu menu;
    
        public MouseListener(Menu menu){
            this.menu=menu;
        }
        @Override
        public void mousePressed(MouseEvent e) {
           menu.mousePressed(e);
        }
    }
       
        public void mousePressed(MouseEvent e) {
            int mouse_X = e.getX();
            int mouse_Y = e.getY();

            // detect button pressed

            for (ImageFactory image : Menu_Images) {

                if (mouse_X > image.getRect().getX() && mouse_X < image.getRect().getX() + image.getWidth()
                        && mouse_Y > image.getRect().getY() && mouse_Y < image.getRect().getY() + image.getHeight()) {

                    // check what button was pressed

                    if (image.getFilePath() == Play_Button_File_Path) {
                         // visible false...?
                        Frame.dispose();
                        BoardFrame boardframe = new BoardFrame(this,false); //use accessor methods?
                        boardframe.setVisible(true);
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
                    if (image.getFilePath() == Custom_Board_Button_File_Path) {
                        BoardFrame boardframe = new BoardFrame(this,true);
                        boardframe.setVisible(true); 
                    }
                }
            }

            drawMenu();
        }
    

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

    public Boolean getPlayerCol() {
        return PlayerIsWhite;
    }

    public Boolean getHintsOn() {
        return HintsOn;
    }

    public Boolean getTutorialOn() {
        return TutorialOn;
    }

    public void setCustomPieces(ArrayList<Piece>customPieces_){
        customPieces=customPieces_;
    }

    public ArrayList<Piece> getCustomPieces(){
        return customPieces;
    }

    public void setPlayerTurn(Boolean turn_is_white_){
        turn_is_white=turn_is_white_;
    }
    public boolean getPlayerTurn(){
        return turn_is_white;
    }
    public MenuFrame getMenuFrame(){
        return this.Frame;
    }
}
