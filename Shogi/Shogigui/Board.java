package Shogigui;

import Shogigui.pieces.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class Board extends JComponent {

    private int turnCounter = 0;

    private final int Square_Width = 65;
    private ArrayList<Piece> White_Pieces;
    private ArrayList<Piece> Black_Pieces;

    private ArrayList<ImageFactory> Static_Images;
    private ArrayList<ImageFactory> Piece_Images; // use image factory interface...?

    private Piece Active_Piece;
    private Piece Previous_Peice;
    private ArrayList<Piece> possiblePromotion = new ArrayList<Piece>();
    private boolean PromotionButtonOn = false;

    private boolean HintsDisplayed;

    private final int rows = 9;
    private final int cols = 9;
    private Integer[][] BoardGrid;

    private final String board_images_file_path = "images" + File.separator + "board" + File.separator;

    private final String board_file_path = board_images_file_path + "board.png";
    private final String active_square_file_path = board_images_file_path + "active_square.png";
    private final String capturable_square_file_path = board_images_file_path + "capturable_square.png";
    private final String promote_button_file_path = board_images_file_path + "promote_button.png";
    private final String dont_promote_button_file_path = board_images_file_path + "dont_promote_button.png";
    private final String white_pieces_file_path = board_images_file_path + "white_pieces" + File.separator;
    private final String black_pieces_file_path = board_images_file_path + "black_pieces" + File.separator;
    private final String promoted_white_pieces_file_path = white_pieces_file_path + "Promoted" + File.separator;
    private final String promoted_black_pieces_file_path = black_pieces_file_path + "Promoted" + File.separator;

    private boolean PlayerIsWhite;
    private boolean HintsOn;
    private boolean TutorialOn; // ++

    private void initBoard() {

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                BoardGrid[i][j] = 0;
            }
        }

        initPieces();
    }

    // ++
    // move pieces with least moves range to front of list to improve efficiancy
    // when searching through list and checking pieces moves when checking for
    // protected pieces...
    // ++

    private void initPieces() {

        White_Pieces.add(new King(4, 0, true, "King.png", this, false));

        White_Pieces.add(new Lance(0, 0, true, "Lance.png", this, false));
        White_Pieces.add(new Lance(8, 0, true, "Lance.png", this, false));

        White_Pieces.add(new Knight(1, 0, true, "Knight.png", this, false));
        White_Pieces.add(new Knight(7, 0, true, "Knight.png", this, false));

        White_Pieces.add(new SilverGeneral(2, 0, true, "SilverGeneral.png", this, false));
        White_Pieces.add(new SilverGeneral(6, 0, true, "SilverGeneral.png", this, false));

        White_Pieces.add(new GoldGeneral(3, 0, true, "GoldGeneral.png", this, false));
        White_Pieces.add(new GoldGeneral(5, 0, true, "GoldGeneral.png", this, false));

        White_Pieces.add(new Bishop(7, 1, true, "Bishop.png", this, false));
        White_Pieces.add(new Rook(1, 1, true, "Rook.png", this, false));

        White_Pieces.add(new Pawn(0, 2, true, "Pawn.png", this, false));
        White_Pieces.add(new Pawn(1, 2, true, "Pawn.png", this, false));
        White_Pieces.add(new Pawn(2, 2, true, "Pawn.png", this, false));
        White_Pieces.add(new Pawn(3, 2, true, "Pawn.png", this, false));
        White_Pieces.add(new Pawn(4, 2, true, "Pawn.png", this, false));
        White_Pieces.add(new Pawn(5, 2, true, "Pawn.png", this, false));
        White_Pieces.add(new Pawn(6, 2, true, "Pawn.png", this, false));
        White_Pieces.add(new Pawn(7, 2, true, "Pawn.png", this, false));
        White_Pieces.add(new Pawn(8, 2, true, "Pawn.png", this, false));

        Black_Pieces.add(new King(4, 8, false, "King.png", this, false));

        Black_Pieces.add(new Lance(0, 8, false, "Lance.png", this, false));
        Black_Pieces.add(new Lance(8, 8, false, "Lance.png", this, false));

        Black_Pieces.add(new Knight(1, 8, false, "Knight.png", this, false));
        Black_Pieces.add(new Knight(7, 8, false, "Knight.png", this, false));

        Black_Pieces.add(new SilverGeneral(2, 8, false, "SilverGeneral.png", this, false));
        Black_Pieces.add(new SilverGeneral(6, 8, false, "SilverGeneral.png", this, false));

        Black_Pieces.add(new GoldGeneral(3, 8, false, "GoldGeneral.png", this, false));
        Black_Pieces.add(new GoldGeneral(5, 8, false, "GoldGeneral.png", this, false));

        Black_Pieces.add(new Bishop(1, 7, false, "Bishop.png", this, false));
        Black_Pieces.add(new Rook(7, 7, false, "Rook.png", this, false));

        Black_Pieces.add(new Pawn(0, 6, false, "Pawn.png", this, false));
        Black_Pieces.add(new Pawn(1, 6, false, "Pawn.png", this, false));
        Black_Pieces.add(new Pawn(2, 6, false, "Pawn.png", this, false));
        Black_Pieces.add(new Pawn(3, 6, false, "Pawn.png", this, false));
        Black_Pieces.add(new Pawn(4, 6, false, "Pawn.png", this, false));
        Black_Pieces.add(new Pawn(5, 6, false, "Pawn.png", this, false));
        Black_Pieces.add(new Pawn(6, 6, false, "Pawn.png", this, false));
        Black_Pieces.add(new Pawn(7, 6, false, "Pawn.png", this, false));
        Black_Pieces.add(new Pawn(8, 6, false, "Pawn.png", this, false));

    }

    public Board(Boolean PlayerIsWhite, Boolean HintsOn, Boolean TutorialOn) {

        this.PlayerIsWhite = PlayerIsWhite;
        this.HintsOn = HintsOn;
        this.TutorialOn = TutorialOn;

        BoardGrid = new Integer[rows][cols];
        Static_Images = new ArrayList<ImageFactory>();
        Piece_Images = new ArrayList<ImageFactory>();
        White_Pieces = new ArrayList<Piece>();
        Black_Pieces = new ArrayList<Piece>();

        initBoard();

        this.setBackground(new Color(245, 255, 250));
        this.setPreferredSize(new Dimension(1100, 580)); // board: 580x580
        this.setMinimumSize(new Dimension(100, 100));
        this.setMaximumSize(new Dimension(1000, 1000));

        this.addMouseListener(mouseAdapter);
        // this.addKeyListener(keyAdapter);

        this.setVisible(true);
        this.requestFocus();

        drawBoard();
    }

    private boolean toggle(boolean Display) {

        if (Display == true) {
            return false;
        } else {
            return true;
        }
    }

    private void drawBoard() {

        Piece_Images.clear();
        Static_Images.clear();

        // add board grid

        Static_Images.add(new ImageFactory(board_file_path, 0, 0));

        // add active square if a piece is clicked

        if (Active_Piece != null) {
            Static_Images.add(new ImageFactory(active_square_file_path, Square_Width * Active_Piece.getX(),
                    Square_Width * Active_Piece.getY()));
        }

        // add pieces /update piece positions 

        String piece_file_path;

        for (Piece piece : White_Pieces) {
            int COL = piece.getX();
            int ROW = piece.getY();

            if (piece.is_promoted() == false) {
                piece_file_path = white_pieces_file_path + piece.getFilePath();
            } else {
                piece_file_path = promoted_white_pieces_file_path + piece.getFilePath();
            }
            Piece_Images.add(new ImageFactory(piece_file_path, Square_Width * COL, Square_Width * ROW));
        }

        for (Piece piece : Black_Pieces) {
            int COL = piece.getX();
            int ROW = piece.getY();

            if (piece.is_promoted() == false) {
                piece_file_path = black_pieces_file_path + piece.getFilePath();
            } else {
                piece_file_path = promoted_black_pieces_file_path + piece.getFilePath();
            }
            Piece_Images.add(new ImageFactory(piece_file_path, Square_Width * COL, Square_Width * ROW));
        }
        // add promotion buttons if promotion is availiable 

        if (PromotionButtonOn == true) {

            Static_Images.add(new ImageFactory(promote_button_file_path, Square_Width * 15, Square_Width * 8));

            Static_Images.add(new ImageFactory(dont_promote_button_file_path, Square_Width * 16, Square_Width * 8));
        }

        // add / update hints (dependent on turn) if player hints were turned on

        if (this.HintsOn == true) {
            if (HintsDisplayed == true) {

                if (getCaptureablePieces() != null) {

                    for (Piece captureablePiece : getCaptureablePieces()) {

                        Static_Images.add(new ImageFactory(capturable_square_file_path,
                                Square_Width * captureablePiece.getX(), Square_Width * captureablePiece.getY()));
                    }
                }
            }
        }
        this.repaint();
    }

    public Piece getPiece(int x, int y) {
        for (Piece p : White_Pieces) {
            if (p.getX() == x && p.getY() == y) {
                return p;
            }
        }
        for (Piece p : Black_Pieces) {
            if (p.getX() == x && p.getY() == y) {
                return p;
            }
        }
        return null;
    }

    private void CapturedPeice(Piece piece) { // make more efficient...?

        int ROW = 0;
        int COL = 0;
        int Captured_White_Pieces = 0;
        int Captured_Black_Pieces = 0;

        // set piece to captured and organise rows of captured peices

        piece.captured(true);

        if (piece.isWhite() == true) {

            ROW = 0;
            COL = 8;
            for (Piece p : White_Pieces) {
                if (p.is_captured() == true) {
                    COL = COL + 1;
                    if (Captured_White_Pieces % 4 == 0 && Captured_White_Pieces != 0) {
                        ROW = ROW + 1;
                        COL = 9;
                    }
                    Captured_White_Pieces = Captured_White_Pieces + 1;
                }
            }
            piece.setX(COL);
            piece.setY(ROW);

        } else {

            ROW = 8;
            COL = 8;
            for (Piece p : Black_Pieces) {
                if (p.is_captured() == true) {
                    COL = COL + 1;
                    if (Captured_Black_Pieces % 4 == 0 && Captured_Black_Pieces != 0) {
                        ROW = ROW - 1;
                        COL = 9;
                    }
                    Captured_Black_Pieces = Captured_Black_Pieces + 1;
                }
            }
            piece.setX(COL);
            piece.setY(ROW);
        }
    }

    private void updateProtectedPieces() {

        ArrayList<Piece> Pieces;

        // select the array of pieces/ colour of pieces that we're checking for
        // protected pieces within (depending on player colour)

        if (PlayerIsWhite == true) {
            Pieces = Black_Pieces;
        } else {
            Pieces = White_Pieces;
        }

        // reset pieces protected status
        for (Piece p : Pieces) {

            p.set_protected(false);
        }

        for (int x = 0; x <= 8; x++) {
            for (int y = 0; y <= 8; y++) {

                // check if square contains peice, if so check if a different piece of the same
                // colour can move to protect this piece -by using the canMove method but by
                // also allowing the piece to move to positions containing same colour pieces
                // (using set_checking)- if so set piece to protected

                if (getPiece(x, y) != null) {

                    if (Pieces.contains(getPiece(x, y))) { // check col

                        for (Piece p : Pieces) {

                            p.set_checking(true);

                            if (p.canMove(x, y) && p.getX() != x && p.getY() != y) { // dont check that the piece we're
                                                                                     // checking is protected
                                                                                     // can move to protect itself
                                getPiece(x, y).set_protected(true);
                                break;

                            }
                            p.set_checking(false);
                        }
                    }
                }
            }
        }
    }

    private ArrayList<Piece> getCaptureablePieces() {

        ArrayList<Piece> Pieces;
        ArrayList<Piece> capturablePieces = new ArrayList<Piece>();

        updateProtectedPieces();

        // select the array of pieces/ the colour of pieces that we're checking for
        // capturable pieces within (depending on player colour)

        if (PlayerIsWhite == true) {
            Pieces = White_Pieces;
        } else {
            Pieces = Black_Pieces;
        }

        for (int x = 0; x <= 8; x++) {
            for (int y = 0; y <= 8; y++) {

                // check if square contains piece, if so check pieces colour and and if its
                // protected, if not protected check if an opposite colour piece can move to
                // prieces position, if so it is captureable

                if (getPiece(x, y) != null) {

                    if (Pieces.contains(getPiece(x, y)) == false && getPiece(x, y).is_protected() == false) {

                        for (Piece p : Pieces) {

                            if (p.canMove(x, y)) {

                                capturablePieces.add(getPiece(x, y));

                            }
                        }
                    }
                }
            }
        }

        if (capturablePieces.isEmpty()) {
            capturablePieces = null;
        }

        return capturablePieces;
    }

    private MouseAdapter mouseAdapter = new MouseAdapter() {

        /*
         * @Override
         * public void mouseClicked(MouseEvent e) {
         * 
         * }
         */

        @Override
        public void mousePressed(MouseEvent e) {
            int mouse_X = e.getX();
            int mouse_Y = e.getY();
            int Clicked_Column = mouse_X / Square_Width;
            int Clicked_Row = mouse_Y / Square_Width;
            boolean whites_turn = true;

            if (turnCounter % 2 == 1) {
                whites_turn = false;
            }

            // make hints displayed dependent on player colour
            if (whites_turn == true) {
                HintsDisplayed = true;
            } else {
                HintsDisplayed = false;
            }

            Piece Clicked_Piece = getPiece(Clicked_Column, Clicked_Row);

            // dont allow move until player chooses to promote or not
            if (PromotionButtonOn != true) {

                if (Active_Piece == null && Clicked_Piece != null &&
                        ((whites_turn && Clicked_Piece.isWhite()) || (!whites_turn && Clicked_Piece.isBlack()))) {
                    Active_Piece = Clicked_Piece;

                }

                else if (Active_Piece != null && Active_Piece.getX() == Clicked_Column
                        && Active_Piece.getY() == Clicked_Row) {

                    Active_Piece = null;
                }

                else if (Active_Piece != null && Active_Piece.canMove(Clicked_Column, Clicked_Row)
                        && ((whites_turn && Active_Piece.isWhite())
                                || (!whites_turn && Active_Piece.isBlack()))) {

                    // if piece is there, remove promotion (if promoted) and remove it/ capture
                    // piece so active piece can be there
                    if (Clicked_Piece != null) {
                        Clicked_Piece.promote(false);
                        CapturedPeice(Clicked_Piece);

                    }
                    // do move
                    Active_Piece.setX(Clicked_Column);
                    Active_Piece.setY(Clicked_Row);

                    // save previous piece incase it can be promoted
                    Previous_Peice = Active_Piece;

                    if (PromotionButtonOn == true) {
                        PromotionButtonOn = toggle(PromotionButtonOn);
                    }
                    // if piece must be promoted promote, if not display PromotionButtonOn
                    if (((Active_Piece.isWhite() && Active_Piece.getY() == 8)
                            || (Active_Piece.isBlack() && Active_Piece.getY() == 0))
                            && ((Active_Piece.getClass().equals(Pawn.class))
                                    || (Active_Piece.getClass().equals(Lance.class))
                                    || (Active_Piece.getClass().equals(Knight.class)))) {

                        if (possiblePromotion.contains(Active_Piece)) {
                            possiblePromotion.remove(Active_Piece);
                        }

                        Active_Piece.promote(true);

                    }
                    // toggle promotion buttons on and select/ remove pieces for possible promotion
                    else if ((Active_Piece.getClass().equals(King.class) == false
                            && Active_Piece.getClass().equals(GoldGeneral.class) == false)
                            && (Active_Piece.is_promoted() == false
                                    && (Active_Piece.getY() > 5 && Active_Piece.isWhite()
                                            || Active_Piece.getY() < 3 && Active_Piece.isBlack()))) {

                        possiblePromotion.add(Active_Piece);

                        PromotionButtonOn = toggle(PromotionButtonOn);

                    } else if (possiblePromotion.contains(Active_Piece)) {

                        if (Active_Piece.is_promoted() == false) {
                            PromotionButtonOn = toggle(PromotionButtonOn);
                        }

                        possiblePromotion.remove(Active_Piece);

                    }
                    // end turn and add hints for other players turn

                    Active_Piece = null;

                    turnCounter++;

                    HintsDisplayed = toggle(HintsDisplayed);

                }

            } else {

                // detect promote button pressed
                if (PromotionButtonOn == true && Clicked_Column == 15 && Clicked_Row == 8) {

                    Previous_Peice.promote(true);
                    PromotionButtonOn = toggle(PromotionButtonOn);

                    if (possiblePromotion.contains(Active_Piece)) {
                        possiblePromotion.remove(Active_Piece);
                    }

                }
                // detect do not promote button pressed
                if (PromotionButtonOn == true && Clicked_Column == 16 && Clicked_Row == 8) {
                    PromotionButtonOn = toggle(PromotionButtonOn);

                }
            }

            drawBoard();
        }
        /*
         * @Override
         * public void mouseDragged(MouseEvent e) {
         * }
         * 
         * @Override
         * public void mouseReleased(MouseEvent e) {
         * }
         * 
         * @Override
         * public void mouseWheelMoved(MouseWheelEvent e) {
         * }
         */
    };

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        addBackgroundCol(g2);
        addImages(g2);
    }

    private void addBackgroundCol(Graphics2D g2) {
        g2.setColor(getBackground());
        g2.fillRect(0, 0, getWidth(), getHeight());
    }

    private void addImages(Graphics2D g2) {
        for (ImageFactory image : Static_Images) {
            image.drawImage(g2);
        }
        for (ImageFactory image : Piece_Images) {
            image.drawImage(g2);
        }
    }
}

/*
 * 
 * private KeyAdapter keyAdapter = new KeyAdapter() {
 * 
 * @Override
 * public void keyPressed(KeyEvent e) {
 * 
 * }
 * 
 * @Override
 * public void keyReleased(KeyEvent e) {
 * 
 * }
 * 
 * @Override
 * public void keyTyped(KeyEvent e) {
 * 
 * }
 * };
 */