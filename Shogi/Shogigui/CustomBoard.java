package Shogigui;

import Shogigui.pieces.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;

public class CustomBoard extends Board implements Cloneable {

    private final String custom_board_images_file_path = "images" + File.separator + "custom_board" + File.separator;
    private final String play_button_file_path = custom_board_images_file_path + "play_button.png";
    private final String delete_button_file_path = custom_board_images_file_path + "delete_button.png";
    private final String white_turn_button_file_path = custom_board_images_file_path + "white_turn_button.png";
    private final String black_turn_button_file_path = custom_board_images_file_path + "black_turn_button.png";
    private String turn_button_file_path;

    private Boolean turn_is_white = true;

    private BoardFrame boardFrame;
    private Menu menu;

    private JLabel Txt;

    /**
     * The CustomBoard constuctor method is used to create a custom board instance
     * when initilzed the user can customize / populate a board with a layout and
     * setting of their choosing to play custom scenarios
     * 
     * @param boardFrame the jframe in which the board component is contained within
     * @param menu       The menu instance where board settings are / have been
     *                   selected
     */
    public CustomBoard(BoardFrame boardFrame, Menu menu) {
        super(false, false, false, boardFrame, null, false);

        this.boardFrame = boardFrame;
        this.menu = menu;

        Txt = new JLabel();
        Txt.setBounds(600, 75, 750, 400);
        Txt.setFont(new Font("Segoe Script", Font.PLAIN, 20));
        Txt.setForeground(new Color(255, 255, 255));
        Txt.setText(
                "<html> <center> Click a piece, once selected click a square <br/> on the board to place <br/> Click the piece again to de-select the piece <br/> Press delete on selected piece to remove piece</center> </html>");

        update();

    }

    /**
     * The initPieces method populates a list of white piece and a list of black
     * pieces. The pieces added to the lists create allow for the user to select any
     * piece varient within the game
     */
    @Override
    public void initPieces() {

        // populate lists of pieces for customizable board layout

        White_Pieces.add(new King(9, 0, true, "King.png", this, false));
        White_Pieces.add(new Lance(10, 0, true, "Lance.png", this, false));
        White_Pieces.add(new Knight(11, 0, true, "Knight.png", this, false));
        White_Pieces.add(new SilverGeneral(12, 0, true, "SilverGeneral.png", this, false));
        White_Pieces.add(new GoldGeneral(13, 0, true, "GoldGeneral.png", this, false));
        White_Pieces.add(new Bishop(9, 1, true, "Bishop.png", this, false));
        White_Pieces.add(new Rook(10, 1, true, "Rook.png", this, false));
        White_Pieces.add(new Pawn(11, 1, true, "Pawn.png", this, false));
        White_Pieces.add(new Lance(12, 1, true, "Lance.png", this, true));
        White_Pieces.add(new Knight(13, 1, true, "Knight.png", this, true));
        White_Pieces.add(new SilverGeneral(9, 2, true, "SilverGeneral.png", this, true));
        White_Pieces.add(new Bishop(10, 2, true, "Bishop.png", this, true));
        White_Pieces.add(new Rook(11, 2, true, "Rook.png", this, true));
        White_Pieces.add(new Pawn(12, 2, true, "Pawn.png", this, true));
        Black_Pieces.add(new King(9, 6, false, "King.png", this, false));
        Black_Pieces.add(new Lance(10, 6, false, "Lance.png", this, false));
        Black_Pieces.add(new Knight(11, 6, false, "Knight.png", this, false));
        Black_Pieces.add(new SilverGeneral(12, 6, false, "SilverGeneral.png", this, false));
        Black_Pieces.add(new GoldGeneral(13, 6, false, "GoldGeneral.png", this, false));
        Black_Pieces.add(new Bishop(9, 7, false, "Bishop.png", this, false));
        Black_Pieces.add(new Rook(10, 7, false, "Rook.png", this, false));
        Black_Pieces.add(new Pawn(11, 7, false, "Pawn.png", this, false));
        Black_Pieces.add(new Lance(12, 7, false, "Lance.png", this, true));
        Black_Pieces.add(new Knight(13, 7, false, "Knight.png", this, true));
        Black_Pieces.add(new SilverGeneral(9, 8, false, "SilverGeneral.png", this, true));
        Black_Pieces.add(new Bishop(10, 8, false, "Bishop.png", this, true));
        Black_Pieces.add(new Rook(11, 8, false, "Rook.png", this, true));
        Black_Pieces.add(new Pawn(12, 8, false, "Pawn.png", this, true));

        All_Pieces.addAll(White_Pieces);
        All_Pieces.addAll(Black_Pieces);

        for (Piece piece : All_Pieces) {
            piece.captured(true);
        }

        updateBoardStatus();

    }

    /**
     * The update method is used to update the list of images that are displayed
     * within the custom board component, dependent on user / mouse input
     */
    public void update() {

        cbImages = new ArrayList<ImageFactory>();

        if (InGameMenuIsDisplay) {
            this.remove(Txt);
        } else {
            this.add(Txt);

            cbImages.add(new ImageFactory(play_button_file_path, 910, 500));

            if (turn_is_white) {
                turn_button_file_path = white_turn_button_file_path;
            } else {
                turn_button_file_path = black_turn_button_file_path;
            }
            cbImages.add(new ImageFactory(turn_button_file_path, 910, 425));

            cbImages.add(new ImageFactory(delete_button_file_path, 910, 350));
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {

        int mouse_X = e.getX();
        int mouse_Y = e.getY();
        int Clicked_Column = mouse_X / Square_Width;
        int Clicked_Row = mouse_Y / Square_Width;

        Txt.setText(
                "<html> <center> Click a piece, once selected click a square <br/> on the board to place <br/> Click the piece again to de-select the piece <br/> Press delete on selected piece to remove piece</center> </html>");
        Txt.setForeground(new Color(255, 255, 255));
        Txt.setBounds(600, 75, 750, 400);

        for (ImageFactory image : cbImages) {

            if (mouse_X > image.getRect().getX() && mouse_X < image.getRect().getX() + image.getWidth()
                    && mouse_Y > image.getRect().getY() && mouse_Y < image.getRect().getY() + image.getHeight()) {

                // check what button was pressed

                if (image.getFilePath() == play_button_file_path) {

                    // ++ check two kings have been placed

                    int whiteKingPlaced = 0;
                    int blackKingPlaced = 0;

                    for (Piece piece : All_Pieces) {

                        if (piece.getClass() == King.class
                                && piece.moveIsOutOfBounds(piece.getX(), piece.getY()) == false && piece.isWhite()) {
                            whiteKingPlaced = whiteKingPlaced + 1;
                        }
                        if (piece.getClass() == King.class
                                && piece.moveIsOutOfBounds(piece.getX(), piece.getY()) == false
                                && piece.isWhite() == false) {
                            blackKingPlaced = blackKingPlaced + 1;
                        }
                    }

                    if (blackKingPlaced == 1 && whiteKingPlaced == 1) {
                        boardFrame.dispose();
                        new BoardFrame(menu, false);
                    } else {
                        Txt.setText("<html> <center> Must place one white and black king to play! </center> </html>");
                        Txt.setForeground(new Color(255, 0, 0));
                        Txt.setBounds(600, 75, 750, 400);
                    }

                }
                if (image.getFilePath() == turn_button_file_path) {

                    turn_is_white = toggle(turn_is_white);

                }
                if (image.getFilePath() == delete_button_file_path) {

                    if (Active_Piece != null
                            && !(Active_Piece.moveIsOutOfBounds(Active_Piece.getX(), Active_Piece.getY()))) {

                        if (Active_Piece.isWhite()) {
                            White_Pieces.remove(Active_Piece);
                        } else {
                            Black_Pieces.remove(Active_Piece);
                        }
                        Active_Piece = null;
                    }

                }
            }
        }

        Piece Clicked_Piece = getPiece(Clicked_Column, Clicked_Row);

        // in game menu

        if (mouse_X > 14.5 * Square_Width && mouse_Y < 0.8 * Square_Width) {
            InGameMenuIsDisplay = true;
        }

        if (InGameMenuIsDisplay) {
            inGameMenu.detectButtonPress(mouse_X, mouse_Y);

        } else {

            if (Active_Piece == null && Clicked_Piece != null) {

                Active_Piece = Clicked_Piece;

            } else if (Active_Piece != null && Active_Piece.getX() == Clicked_Column
                    && Active_Piece.getY() == Clicked_Row) {

                Active_Piece = null;
            }

            else if (Active_Piece != null && Active_Piece.canBeDropped(Clicked_Column, Clicked_Row)) {

                Piece newPiece = null;
                try {
                    newPiece = (Piece) Active_Piece.clone();
                } catch (CloneNotSupportedException e1) {
                    e1.printStackTrace();
                }

                newPiece.setX(Clicked_Column);
                newPiece.setY(Clicked_Row);

                if (newPiece.isWhite()) {
                    White_Pieces.add(newPiece);
                } else {
                    Black_Pieces.add(newPiece);
                }

                Active_Piece = null;
            }
        }
        update();

        // updateBoardStatus();

        updateCustomStatus();

        drawBoard();
    }

    /**
     * The updateCustomStatus method is used to update the captured status of pieces
     * added to the board layout / scenerio and update the menu settings (ie list of
     * pieces / piece layout) created in a customboard instance by the user
     */
    public void updateCustomStatus() {

        All_Pieces.clear();
        All_Pieces.addAll(White_Pieces);
        All_Pieces.addAll(Black_Pieces);

        ArrayList<Piece> customPieces = new ArrayList<Piece>();

        for (Piece piece : All_Pieces) {

            if (piece.moveIsOutOfBounds(piece.getX(), piece.getY()) == false) {
                piece.captured(false);
                customPieces.add(piece);
            }
        }

        menu.setCustomPieces(customPieces);

        menu.setPlayerTurn(turn_is_white);

    }
}
