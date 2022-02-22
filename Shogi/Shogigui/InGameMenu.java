package Shogigui;

import java.io.*;
import java.util.*;

public class InGameMenu {

    private ArrayList<ImageFactory> inGame_Menu_Images;

    private final String InGame_Menu_Images_File_path = "images" + File.separator + "inGame_menu" + File.separator;
    private final String Background_Image_File_Path = InGame_Menu_Images_File_path + "Background.png";
    private final String Resume_Button_File_Path = InGame_Menu_Images_File_path + "ResumeButton.png";
    private final String Menu_Button_File_Path = InGame_Menu_Images_File_path + "MenuButton.png";
    private final String Revert_Button_File_Path = InGame_Menu_Images_File_path + "RevertButton.png";
    private final String Quit_Button_File_Path = InGame_Menu_Images_File_path + "QuitButton.png";
    private Board board;

    /**
     * The InGameMenu constuctor method is used to create an in game menu (ie where
     * the user can navigate the program)
     * 
     * @param board The board where the in game menu resides
     */
    public InGameMenu(Board board) { // ++ extends menu?

        this.board = board;

        initMenu();
    }

    /**
     * The initMenu method initilzes a list of static images / buttons that make up
     * the in game menu
     */
    private void initMenu() {

        inGame_Menu_Images = new ArrayList<ImageFactory>();

        inGame_Menu_Images.add(new ImageFactory(Background_Image_File_Path, 0, 0));

        inGame_Menu_Images.add(new ImageFactory(Resume_Button_File_Path, 200, 150));

        inGame_Menu_Images.add(new ImageFactory(Menu_Button_File_Path, 200, 275));

        inGame_Menu_Images.add(new ImageFactory(Revert_Button_File_Path, 525, 150));

        inGame_Menu_Images.add(new ImageFactory(Quit_Button_File_Path, 525, 275));

    }

    /**
     * The detectButtonPress method is used to generate a responce dependant on the
     * where in the board window the mouse was clicked
     * 
     * @param mouse_X The location of the mouse click within the board window on the
     *                x axis
     * @param mouse_Y The location of the mouse click within the board window on the
     *                y axis
     */
    public void detectButtonPress(int mouse_X, int mouse_Y) {

        for (ImageFactory image : inGame_Menu_Images) {

            if (mouse_X > image.getRect().getX() && mouse_X < image.getRect().getX() + image.getWidth()
                    && mouse_Y > image.getRect().getY() && mouse_Y < image.getRect().getY() + image.getHeight()) {

                // check what button was pressed

                if (image.getFilePath() == Resume_Button_File_Path) {
                    board.closeInGameMenu();
                }
                if (image.getFilePath() == Menu_Button_File_Path) {
                    board.setVisible(false);
                    board.getBoardFrame().dispose();
                    MenuFrame boardframe = new MenuFrame();
                    boardframe.setVisible(true);
                }
                if (image.getFilePath() == Revert_Button_File_Path) {
                    board.revertLastMove();
                    board.closeInGameMenu();
                }
                if (image.getFilePath() == Quit_Button_File_Path) {
                    System.exit(1);
                }
            }
        }
    }

    /**
     * getImages accessor method
     * @return Array list of imageFactory objects that make up the in game menu
     */
    public ArrayList<ImageFactory> getImages() {
        return inGame_Menu_Images;
    }
}
