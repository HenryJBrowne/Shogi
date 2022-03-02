package Shogigui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

import Shogigui.pieces.Piece;

public class Menu extends JComponent {

    private ArrayList<ImageFactory> Menu_Images;

    private final String Menu_Images_File_path = "images" + File.separator + "menu" + File.separator;
    private final String Background_Image_File_Path = Menu_Images_File_path + "Background.png";
    private final String Play_Button_File_Path = Menu_Images_File_path + "PlayButton.png";
    private final String Controls_Button_File_Path = Menu_Images_File_path + "ControlsButton.png"; // ++ change to info
                                                                                                   // button
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

    //public boolean PlayPressed = false; // encapsulate and add accessor methods?...
    public boolean PlayerIsWhite = true;
    public boolean HintsOn = true;
    public boolean TutorialOn = true;

    private ArrayList<Piece> customPieces;
    private boolean turn_is_white = true;

    private MenuFrame Frame;

    /**
     * The Menu constuctor creates a menu instance where the user can select setting
     * for the shogi game and navigate the program
     * 
     * @param Frame The menu jframe where the menu component resides
     */
    public Menu(MenuFrame Frame) {

        Menu_Images = new ArrayList<ImageFactory>();
        this.Frame = Frame;

        this.setBackground(new Color(255, 255, 255));
        this.setPreferredSize(new Dimension(1100, 580));
        this.setMinimumSize(new Dimension(100, 100));
        this.setMaximumSize(new Dimension(1000, 1000));

        this.addMouseListener(new MouseListener(this));

        this.setVisible(true);
        this.requestFocus();

        drawMenu();
    }

    /**
     * The drawMenu method is used to add to / update the lists containing buttons
     * and all the images that make up the menu
     */
    private void drawMenu() {

        Menu_Images.clear();

        // add static buttons

        Menu_Images.add(new ImageFactory(Play_Button_File_Path, 75, 275));

        Menu_Images.add(new ImageFactory(Controls_Button_File_Path, 400, 275));

        Menu_Images.add(new ImageFactory(Quit_Button_File_Path, 725, 275));

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

        Menu_Images.add(new ImageFactory(Custom_Board_Button_File_Path, 250, 200));

        this.repaint();
    }

    /**
     * The toggle method is used to flip a boolean value
     * 
     * @param Setting The boolean value to be flipped (if on set off for example)
     * @return The flipped boolean value of the parameter given
     */
    private boolean toggle(boolean Setting) {

        return (Setting == true) ? false : true;

    }

    /**
     * The MouseListener class inherits methods from the MouseAdapter allowing the
     * menu class to receive mouse input
     */
    class MouseListener extends MouseAdapter { // ++ make neater

        Menu menu;

        public MouseListener(Menu menu) {
            this.menu = menu;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            menu.mousePressed(e);
        }
    }

    /**
     * The mousePressed method is called from within the MouseListener class, once a
     * mouse button press has been detected this method is called, depending on the
     * sequence and location on the window of the button press / pressed the
     * corresponding action is generated. This method is used to allow the user to
     * click the play button within the menu for example, dependent on the event
     * generated the menu display is drawn / updated accordingly
     * 
     * @param e The mouseEvent that was detected by a MouseListener class
     */
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
                    BoardFrame boardframe = new BoardFrame(this, false); // use accessor methods?
                    boardframe.setVisible(true);
                }
                if (image.getFilePath() == Controls_Button_File_Path) {
                    // + +
                }
                if (image.getFilePath() == Quit_Button_File_Path) {
                    System.exit(1);
                }
                if (image.getFilePath() == Hints_Button_File_Path) {
                    HintsOn = toggle(HintsOn);
                }
                if (image.getFilePath() == Tutorial_Button_File_Path) {
                    TutorialOn = toggle(TutorialOn);
                }
                if (image.getFilePath() == PlayerCol_Button_File_Path) {
                    PlayerIsWhite = toggle(PlayerIsWhite);
                }
                if (image.getFilePath() == Custom_Board_Button_File_Path) {
                    BoardFrame boardframe = new BoardFrame(this, true);
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

    /**
     * The addBackground method is used to add the object that make up the
     * background image displayed within the menu component contained in the menu
     * jframe window
     * 
     * @param g2 The graphics used for rendering images objects within component
     */
    private void addBackground(Graphics2D g2) {

        ImageFactory Background = new ImageFactory(Background_Image_File_Path, 0, 0);
        Background.drawImage(g2);

    }

    /**
     * The addImages method is used to add all the image objects that populate the
     * menu component display contained in the menu jframe window that makes the
     * graphical display of objects
     * 
     * @param g2 The graphics used for rendering images objects within component
     */
    private void addImages(Graphics2D g2) {
        for (ImageFactory image : Menu_Images) {
            image.drawImage(g2);
        }
    }

    /**
     * getPlayerCol accessor method
     * @return True if the user has selected to play as white pieces, false if not
     */
    public Boolean getPlayerCol() {
        return PlayerIsWhite;
    }

    /**
     * getHintsOn accessor method
     * @return True if the user has selected to play with hints on, false if not
     */
    public Boolean getHintsOn() {
        return HintsOn;
    }

    /**
     * getTutorialOn
     * @return True if the user has selected to play with tutorials on, false if not
     */
    public Boolean getTutorialOn() {
        return TutorialOn;
    }

    /**
     * setCustomPieces set method (ie used if the user selects to customize a board layout)
     * @param customPieces_ Array list of pieces created to determin a custom board layout of pieces 
     */
    public void setCustomPieces(ArrayList<Piece> customPieces_) {
        customPieces = customPieces_;
    }

    /**
     * getCustoPieces accessor method
     * @return Arraylist of pieces that determin a custom board layout of pieces created by the user, null if the user has not customized a board layout
     */
    public ArrayList<Piece> getCustomPieces() {
        return customPieces;
    }

    /**
     * setPlayerTurn set method
     * @param turn_is_white_ True if the player has selected the first turn to be white piece to move, false if not
     */
    public void setPlayerTurn(Boolean turn_is_white_) {
        turn_is_white = turn_is_white_;
    }

    /**
     * getPlayerTurn accessor method
     * @return True if the player has selected the first turn to be white piece to move, false if not
     */
    public boolean getPlayerTurn() {
        return turn_is_white;
    }

    /**
     * getMenuFrame accessor method
     * @return Menuframe that this menu component is contained within
     */
    public MenuFrame getMenuFrame() {
        return this.Frame;
    }
}
