package Shogigui;

import Shogigui.pieces.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;

public class Board extends JComponent {

    private int moveCounter = 0; // ++
    private boolean whites_turn=true;

    private final int Square_Width = 65;
    private ArrayList<Piece> White_Pieces;
    private ArrayList<Piece> Black_Pieces;

    private ArrayList<ImageFactory> Static_Images;
    private ArrayList<ImageFactory> Piece_Images; // ++ [use image factory interface...?]

    private Piece Active_Piece;
    private Piece Previous_Peice;
    private ArrayList<Piece> possiblePromotion = new ArrayList<Piece>();
    private boolean PromotionButtonOn = false;

    private boolean HintsDisplayed;

    private final int ROWS = 9;
    private final int COLS = 9;
    private Integer[][] BoardGrid;

    private boolean WhiteIsChecked = false;
    private boolean BlackIsChecked = false;
    private ArrayList<Piece> PiecesCheckingBlackKing;
    private ArrayList<Piece> PiecesCheckingWhiteKing;

    private int Captured_White_Pieces = 0;
    private int Captured_Black_Pieces = 0;

    private ArrayList<Square> whiteAttackingSquares;
    private ArrayList<Square> blackAttackingSquares;

    private ArrayList<Square> getOutOfcheckMoves = null;
    private ArrayList<Square> getOutOfCheckDrops = null;

    private ArrayList<Square> checkMateMoves=null; // ++ 

    private final String board_images_file_path = "images" + File.separator + "board" + File.separator;

    private final String board_file_path = board_images_file_path + "board.png";
    private final String active_square_file_path = board_images_file_path + "active_square.png";
    private final String good_square_file_path = board_images_file_path + "good_square.png";
    private final String drop_square_file_path = board_images_file_path + "drop_square.png";
    private final String promote_button_file_path = board_images_file_path + "promote_button.png";
    private final String dont_promote_button_file_path = board_images_file_path + "dont_promote_button.png";
    private final String white_pieces_file_path = board_images_file_path + "white_pieces" + File.separator;
    private final String black_pieces_file_path = board_images_file_path + "black_pieces" + File.separator;
    private final String promoted_white_pieces_file_path = white_pieces_file_path + "Promoted" + File.separator;
    private final String promoted_black_pieces_file_path = black_pieces_file_path + "Promoted" + File.separator;

    private boolean PlayerIsWhite; 
    private boolean HintsOn;
    private boolean TutorialOn; // ++

    public class Square {
        public int x;
        public int y;

        public Square(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private void initBoard() {

        for (int x = 0; x < ROWS; x++) {
            for (int y = 0; y < COLS; y++) {
                BoardGrid[x][y] = 0;
            }
        }

        initPieces();
    }

    // ++
    // [move pieces with least moves range to front of list to improve efficiancy
    // when searching through list and checking pieces moves when checking for
    // protected pieces...]
    // ++

    private void initPieces() {

        // [testing pieces]

        White_Pieces.add(new King(5, 4, true, "King.png", this, false));

        /*White_Pieces.add(new Lance(0, 0, true, "Lance.png", this, false));
        White_Pieces.add(new Lance(8, 0, true, "Lance.png", this, false));

        White_Pieces.add(new Knight(1, 0, true, "Knight.png", this, false));
        White_Pieces.add(new Knight(7, 0, true, "Knight.png", this, false));

        White_Pieces.add(new SilverGeneral(2, 0, true, "SilverGeneral.png", this, false));
        White_Pieces.add(new SilverGeneral(6, 0, true, "SilverGeneral.png", this, false));

        White_Pieces.add(new GoldGeneral(3, 0, true, "GoldGeneral.png", this, false));
        White_Pieces.add(new GoldGeneral(5, 0, true, "GoldGeneral.png", this, false));*/

        White_Pieces.add(new Bishop(7, 1, true, "Bishop.png", this, false));
        White_Pieces.add(new Rook(1, 1, true, "Rook.png", this, false));

        /*White_Pieces.add(new Pawn(0, 2, true, "Pawn.png", this, false));
        White_Pieces.add(new Pawn(1, 2, true, "Pawn.png", this, false));
        White_Pieces.add(new Pawn(2, 2, true, "Pawn.png", this, false));
        White_Pieces.add(new Pawn(3, 2, true, "Pawn.png", this, false));
        White_Pieces.add(new Pawn(4, 2, true, "Pawn.png", this, false));*/
        White_Pieces.add(new Pawn(5, 2, true, "Pawn.png", this, true));

        White_Pieces.add(new Pawn(6, 2, true, "Pawn.png", this, true));
        White_Pieces.add(new Pawn(7, 2, true, "Pawn.png", this, true));
        White_Pieces.add(new Pawn(8, 2, true, "Pawn.png", this, true));

        Black_Pieces.add(new King(4, 8, false, "King.png", this, false));

        Black_Pieces.add(new Lance(0, 8, false, "Lance.png", this, false));

        Black_Pieces.add(new Lance(8, 8, false, "Lance.png", this, false));

        Black_Pieces.add(new Knight(1, 8, false, "Knight.png", this, false));

        /*Black_Pieces.add(new Knight(7, 8, false, "Knight.png", this, false));

        Black_Pieces.add(new SilverGeneral(2, 8, false, "SilverGeneral.png", this, false));
        Black_Pieces.add(new SilverGeneral(6, 8, false, "SilverGeneral.png", this, false));

        Black_Pieces.add(new GoldGeneral(3, 8, false, "GoldGeneral.png", this, false));
        Black_Pieces.add(new GoldGeneral(5, 8, false, "GoldGeneral.png", this, false));*/

        Black_Pieces.add(new Bishop(1, 7, false, "Bishop.png", this, false));
        Black_Pieces.add(new Rook(7, 7, false, "Rook.png", this, false));

        /*Black_Pieces.add(new Pawn(0, 6, false, "Pawn.png", this, false));
        Black_Pieces.add(new Pawn(1, 6, false, "Pawn.png", this, false));
        Black_Pieces.add(new Pawn(2, 6, false, "Pawn.png", this, false));
        Black_Pieces.add(new Pawn(3, 6, false, "Pawn.png", this, false));

        */Black_Pieces.add(new Pawn(4, 6, false, "Pawn.png", this, true));
        Black_Pieces.add(new Pawn(5, 6, false, "Pawn.png", this, true));
        Black_Pieces.add(new Pawn(6, 6, false, "Pawn.png", this, true));
        /*Black_Pieces.add(new Pawn(7, 6, false, "Pawn.png", this, false));
        Black_Pieces.add(new Pawn(8, 6, false, "Pawn.png", this, false));*/

        updateBoardStatus();

    }

    public Board(Boolean PlayerIsWhite, Boolean HintsOn, Boolean TutorialOn) {

        this.PlayerIsWhite = PlayerIsWhite;
        this.HintsOn = HintsOn;
        this.TutorialOn = TutorialOn;

        HintsDisplayed =(PlayerIsWhite)?true:false;

        BoardGrid = new Integer[ROWS][COLS];
        Static_Images = new ArrayList<ImageFactory>();
        Piece_Images = new ArrayList<ImageFactory>();
        White_Pieces = new ArrayList<Piece>();
        Black_Pieces = new ArrayList<Piece>();

        PiecesCheckingWhiteKing = new ArrayList<Piece>();
        PiecesCheckingBlackKing = new ArrayList<Piece>();

        initBoard();

        this.setBackground(new Color(245, 255, 250));
        this.setPreferredSize(new Dimension(1100, 580)); // board: 580x580
        this.setMinimumSize(new Dimension(100, 100));
        this.setMaximumSize(new Dimension(1000, 1000));

        this.addMouseListener(mouseAdapter);

        this.setVisible(true);
        this.requestFocus();

        drawBoard();
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

        // add pieces / update piece positions

        String piece_file_path;

        for (Piece piece : White_Pieces) {
            int COL = piece.getX();
            int ROW = piece.getY();

            piece_file_path= (piece.is_promoted())?promoted_white_pieces_file_path + piece.getFilePath(): white_pieces_file_path + piece.getFilePath();

            Piece_Images.add(new ImageFactory(piece_file_path, Square_Width * COL, Square_Width * ROW));
        }

        for (Piece piece : Black_Pieces) {
            int COL = piece.getX();
            int ROW = piece.getY();

            piece_file_path= (piece.is_promoted())?promoted_black_pieces_file_path + piece.getFilePath():black_pieces_file_path + piece.getFilePath(); 

            Piece_Images.add(new ImageFactory(piece_file_path, Square_Width * COL, Square_Width * ROW));
        }
        // add promotion buttons if promotion is availiable

        if (PromotionButtonOn == true) {

            Static_Images.add(new ImageFactory(promote_button_file_path, Square_Width * 15, Square_Width * 8));

            Static_Images.add(new ImageFactory(dont_promote_button_file_path, Square_Width * 16, Square_Width * 8));
        }

        // ADD HINTS

        // add / update hints (dependent on turn) if player hints were turned on

        if (this.HintsOn == true) {
            if (HintsDisplayed == true) {

                // (if a king is checked) add get out of check hints

                if (whiteIschecked() || blackIschecked()) {

                    // ++ [currently hints show every possible moves out of check, not best moves] 
                    // ++ [change get out of check hints so that if king can move out of check this 
                    // move over pieces blocking check by moving into capturable position] ++

                    ArrayList<Square> getOutOfCheckMoveHints = new ArrayList<Square>();
                    ArrayList<Square> getOutOfCheckDropHints = new ArrayList<Square>();

                    ArrayList<Piece> pieces= (PlayerIsWhite)? White_Pieces: Black_Pieces; 

                    // get squares that can stop the check if a piece can move or be dropped into one of these
                    // squares add corresponding image to this to give player hint to get out of check

                    if (getOutOfcheckMoves != null) {

                        for (Square moveSquare : getOutOfcheckMoves) {
                            Static_Images.add(new ImageFactory(good_square_file_path,
                                    Square_Width * moveSquare.x, Square_Width * moveSquare.y));
                        }
                    }

                    if (getOutOfCheckDrops != null && ((pieces == White_Pieces && Captured_White_Pieces > 0)
                            || (pieces == Black_Pieces && Captured_Black_Pieces > 0))) {

                        for (Square dropSquare : getOutOfCheckDrops) {
                            Static_Images.add(new ImageFactory(drop_square_file_path,
                                    Square_Width * dropSquare.x, Square_Width * dropSquare.y));
                        }
                    }
                }
                // (if king is not checked) add capturable pieces hints

                // ++ [currently hints show every possible capturable piece, not the best piece to capture] 
                // ++ [change hints so that if their are multiple captureable pieces give user hint to capture most valueable piece]

                else if (getCaptureablePieces() != null) {

                    for (Piece captureablePiece : getCaptureablePieces()) {

                        Static_Images.add(new ImageFactory(good_square_file_path,
                                Square_Width * captureablePiece.getX(), Square_Width * captureablePiece.getY()));
                    }
                }
            }
        }

        // add check display

        if (whiteIschecked() || blackIschecked()) {

            System.out.println("check"); // ++ [add check graphics]
        }

        // add check display

        if ((getOutOfcheckMoves==null)
                && (whiteIschecked() || blackIschecked())) {

            System.out.println("checkmate!"); // ++ [add check mate graphics]

        }

        // add check mate hints 

        // ++ [...]

        this.repaint();
    }

    public void updateAttackingSquares() {

        ArrayList<Piece> allPieces = new ArrayList<Piece>();

        allPieces.addAll(Black_Pieces);
        allPieces.addAll(White_Pieces);

        blackAttackingSquares = new ArrayList<Square>();
        whiteAttackingSquares = new ArrayList<Square>();

        for (int x = 0; x < ROWS; x++) {
            for (int y = 0; y < COLS; y++) {

                for (Piece piece : allPieces) {

                    if (piece.canMove(x, y) && piece.is_captured() == false) {

                        if (piece.isWhite()) {
                            whiteAttackingSquares.add(new Square(x, y));
                        } else {
                            blackAttackingSquares.add(new Square(x, y));
                        }
                    }
                }
            }
        }
        if (blackAttackingSquares.isEmpty()) {
            blackAttackingSquares = null;
        }
        if (whiteAttackingSquares.isEmpty()) {
            whiteAttackingSquares = null;
        }
    }

    public void updatePiecesAttackersAndDefenders() {

        ArrayList<Piece> allPieces = new ArrayList<Piece>();

        allPieces.addAll(Black_Pieces);
        allPieces.addAll(White_Pieces);

        for (Piece piece : allPieces) {
            piece.setAttackers(new ArrayList<Piece>());
                   piece.setDefenders(new ArrayList<Piece>());
        }

        for (int x = 0; x < ROWS; x++) {
            for (int y = 0; y < COLS; y++) {

                for (Piece piece : allPieces) {

                    if (piece.canMove(x, y) && piece.is_captured() == false && getPiece(x, y) != null
                            && (getPiece(x, y).isWhite() == piece.isBlack())) {
                        getPiece(x, y).addAttackers(piece);

                    }
                    piece.set_checking_if_defender(true); // when checking_if_defender is true pieces can move ontop of same colour pieces

                    if (piece.canMove(x, y) && getPiece(x, y) != null & getPiece(x, y) != piece
                            && piece.isWhite() == getPiece(x, y).isWhite()) {

                        getPiece(x, y).addDefenders(piece);
                    }

                    piece.set_checking_if_defender(false);
                }
            }
        }
    }
  
    private void updateProtectedPieces() {

        ArrayList<Piece> allPieces = new ArrayList<Piece>();

        allPieces.addAll(Black_Pieces);
        allPieces.addAll(White_Pieces);

        for (Piece piece : allPieces) {
            if (piece.getDefenders().size() > 0) {

                for (Piece defender : piece.getDefenders()) {

                    if (defender.getClass() == King.class && isKingProtectingPiece(piece) == false
                            && piece.getDefenders().size() == 1) {
                        piece.set_protected(false);
                    } else {
                        piece.set_protected(true);
                    }
                }
            } else {
                piece.set_protected(false);
            }
        }
    }

    private boolean isKingProtectingPiece(Piece piece) { // ++ test

        if (piece.getAttackers().size() > 1) {
            return false;
        } else if (piece.getAttackers().size() == 0) {
            return true;
        } else {

            // if the piece has one attacker check if other pieces are behind and defending
            // the attacker,
            // if so check if this piece would be defending this piece if it captured Piece

            Piece attacker = piece.getAttackers().get(0);

            String direction = Piece.getMoveDirection(attacker.getX(),attacker.getY(),piece.getX(), piece.getY());

            for (Piece attackerDefender : attacker.getDefenders()) {

                if (attackerDefender.getMovementRange(direction) != null) {

                    for (Square Square : attackerDefender.getMovementRange(direction)) {

                        if (piece.getX() == Square.x && piece.getY() == Square.y) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }
    }

    public void updatePiecesBlockingCheck() { // ++ [make more efficiant...?] ++ test

        ArrayList<Piece> pieces = White_Pieces;
        ArrayList<Piece> opposingPieces = Black_Pieces;

        for (int player = 0; player < 2; player++) {

            // reset
            for (Piece p : pieces) {
                p.set_is_blocking_check(false);
                p.is_blocking_check_from(null);
            }

            Square kingCoord = new Square(getKing(pieces).getX(), getKing(pieces).getY());

            // get pieces that can check king when no pieces are in the way

            for (Piece opposingPiece : opposingPieces) {

                ArrayList<Piece> PiecesBetween = new ArrayList<Piece>();

                ArrayList<Piece> PiecesPotentialCheck = new ArrayList<Piece>();

                ArrayList<Square> squaresBetween = getSquaresBetween(opposingPiece.getX(), opposingPiece.getY(), kingCoord.x,
                        kingCoord.y);

                // check if the king is in the pieces movement range / path if so

                String movementDirection = Piece.getMoveDirection(opposingPiece.getX(),opposingPiece.getY(),kingCoord.x, kingCoord.y);

                ArrayList<Square> pMovementRange = opposingPiece.getMovementRange(movementDirection);

                boolean kingInPath = false;

                if (pMovementRange != null) {

                    for (Square square : pMovementRange) {

                        if (kingCoord.x == square.x && kingCoord.y == square.y) {

                            kingInPath = true;
                        }
                    }
                }

                if (squaresBetween != null && kingInPath) {

                    // for all black pieces: check squares between king and black piece if their is
                    // only one white piece between it is blocking check and that black piece is
                    // threatening check and vice versa

                    for (Square square : squaresBetween) {

                        if (getPiece(square.x, square.y) != null) {

                            PiecesPotentialCheck.add(opposingPiece);

                            PiecesBetween.add(getPiece(square.x, square.y));
                        }
                    }
                    if (PiecesBetween.size() == 1 && pieces.contains(PiecesBetween.get(0))) {
                        (PiecesBetween.get(0)).set_is_blocking_check(true);

                        PiecesBetween.get(0).is_blocking_check_from(PiecesPotentialCheck.get(0));
                    }
                }
            }

            pieces = Black_Pieces;
            opposingPieces = White_Pieces;
        }
    }

    public void updateGetOutOfCheckMovesAndDrops(ArrayList<Piece> pieces){

        getOutOfcheckMoves = new ArrayList<Square>();
        getOutOfCheckDrops = new ArrayList<Square>();

        for (Piece piece : pieces) {

            for (int x = 0; x < ROWS; x++) {
                for (int y = 0; y < COLS; y++) {

                    if (piece.canBeDroppedToStopCheck(x, y) /*&& piece.canMove(x, y)*/) {

                        getOutOfCheckDrops.add(new Square(x, y));
                    }
                    if (piece.canMoveToStopCheck(x, y) && piece.canMove(x, y)) {

                        getOutOfcheckMoves.add(new Square(x, y));
                    }
                }
            }
        }

        if (getOutOfCheckDrops.isEmpty()) {
            getOutOfCheckDrops = null;
        }
        if (getOutOfcheckMoves.isEmpty()) {
            getOutOfcheckMoves = null;
        }
    }

    public void updateBoardStatus() {

        updatePiecesBlockingCheck();
        updateAttackingSquares();
        updatePiecesAttackersAndDefenders();
        updateProtectedPieces();

        if (checkForCheck(getKing(White_Pieces))){
            updateGetOutOfCheckMovesAndDrops(White_Pieces);
        }
        else if (checkForCheck(getKing(Black_Pieces))){
            updateGetOutOfCheckMovesAndDrops(Black_Pieces);
        }
    }

    public boolean checkForCheck(Piece king) {

        // reset
        PiecesCheckingWhiteKing = new ArrayList<Piece>();
        PiecesCheckingBlackKing = new ArrayList<Piece>();

        WhiteIsChecked = false;
        BlackIsChecked = false;

        // check all pieces opposing the king to see if they can move to where the king
        // is
        ArrayList<Piece>Pieces =(king.isWhite())? Black_Pieces:White_Pieces;

        for (Piece piece : Pieces) {
            if (piece.canMove(king.getX(), king.getY())) {

                if (king.isWhite()) {
                    PiecesCheckingWhiteKing.add(piece);
                    WhiteIsChecked = true;

                } else {
                    PiecesCheckingBlackKing.add(piece);
                    BlackIsChecked = true;
                }
            }
        }

        if (WhiteIsChecked == true || BlackIsChecked == true) {
            return true;
        }

        return false;
    }

    public boolean checkForCheckMateMoves() {

        checkMateMoves = new ArrayList<Square>();

        // ++ [...]


        return false;
    }

    public ArrayList<Square> getSquaresThatBlockCheck(Piece king) { // ++[is tripple check possible in shogi? can you block a double check?...] test method...

        ArrayList<Square> blockingSquares = new ArrayList<Square>();

        ArrayList<Piece> PiecesCheckingKing= (king.isWhite())? PiecesCheckingWhiteKing:PiecesCheckingBlackKing;
  

        // Add squares between the 1 piece checking the king and the king to blocking squares

        if (PiecesCheckingKing.size()==1 && getSquaresBetween(PiecesCheckingKing.get(0).getX(), PiecesCheckingKing.get(0).getY(), king.getX(), king.getY())!=null) {

            if (getSquaresBetween(PiecesCheckingKing.get(0).getX(), PiecesCheckingKing.get(0).getY(), king.getX(), king.getY())!=null){

                for (Square squareBetween : getSquaresBetween(PiecesCheckingKing.get(0).getX(), PiecesCheckingKing.get(0).getY(), king.getX(), king.getY())) {

                    blockingSquares.add(squareBetween);     
                }   
            }
        }
        else if (PiecesCheckingKing.size()==2){

            // double check: only add squares to blocking squares if the squares if they block both checking pieces paths to the king 

            for (Square squareBetweenP1AndKing : getSquaresBetween(PiecesCheckingKing.get(0).getX(), PiecesCheckingKing.get(0).getY(), king.getX(), king.getY())){

                for (Square squareBetweenP2AndKing : getSquaresBetween(PiecesCheckingKing.get(1).getX(), PiecesCheckingKing.get(1).getY(), king.getX(), king.getY())){
                    
                    if (squareBetweenP1AndKing.x == squareBetweenP2AndKing.x && squareBetweenP1AndKing.y==squareBetweenP2AndKing.y){

                        blockingSquares.add(squareBetweenP1AndKing);
                    }
                }
            }
        }

        if (blockingSquares.isEmpty()) {
            blockingSquares = null;
        }

        return blockingSquares;
    }

    private void CapturedPeice(Piece piece) { // ++ [make more efficient...?]

        int ROW = 0;
        int COL = 0;
        Captured_White_Pieces = 0;
        Captured_Black_Pieces = 0;

        // set piece to captured

        piece.captured(true);

        // change the colour of the piece catured so it can be dropped

        if (piece.isWhite()) {
            White_Pieces.remove(piece);
            Black_Pieces.add(piece);
        } else {
            Black_Pieces.remove(piece);
            White_Pieces.add(piece);
        }
        piece.changeColour();

        // organise the rows of captured peices
        
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

    private ArrayList<Piece> getCaptureablePieces() {   // ++ [calculate exchanges to determin capurable piece]
        ArrayList<Piece> capturablePieces = new ArrayList<Piece>();

        // select the array of pieces/ the colour of pieces that we're checking for
        // capturable pieces within (depending on player colour)

        ArrayList<Piece> Pieces= (PlayerIsWhite) ?White_Pieces:Black_Pieces;

        for (int x = 0; x < ROWS; x++) {
            for (int y = 0; y < COLS; y++) {

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

        @Override
        public void mousePressed(MouseEvent e) {
            int mouse_X = e.getX();
            int mouse_Y = e.getY();
            int Clicked_Column = mouse_X / Square_Width;
            int Clicked_Row = mouse_Y / Square_Width;

            Piece Clicked_Piece = getPiece(Clicked_Column, Clicked_Row);

            // dont allow move until player chooses to promote or not
            if (PromotionButtonOn != true) {

                if (Active_Piece == null && Clicked_Piece != null
                        && ((whites_turn && Clicked_Piece.isWhite()) || (!whites_turn && Clicked_Piece.isBlack()))) {
                    Active_Piece = Clicked_Piece;

                }

                else if (Active_Piece != null && Active_Piece.getX() == Clicked_Column
                        && Active_Piece.getY() == Clicked_Row) {

                    Active_Piece = null;
                }

                // if player is in check, active piece is not null and active piece can move to
                // stop check is false dont move

                else if ((WhiteIsChecked || BlackIsChecked) && Active_Piece != null
                        && Active_Piece.canMoveToStopCheck(Clicked_Column, Clicked_Row) != true
                        && Active_Piece.canBeDroppedToStopCheck(Clicked_Column, Clicked_Row) != true) {

                    Active_Piece = null;

                }

                else if (Active_Piece != null
                        && (Active_Piece.canMove(Clicked_Column, Clicked_Row)
                                || Active_Piece.canBeDropped(Clicked_Column, Clicked_Row))
                        && ((whites_turn && Active_Piece.isWhite()) || (!whites_turn && Active_Piece.isBlack()))) {

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
                                    || (Active_Piece.getClass().equals(Knight.class)))) {       //++ [fix extendability]

                        if (possiblePromotion.contains(Active_Piece)) {
                            possiblePromotion.remove(Active_Piece);
                        }

                        Active_Piece.promote(true);

                    }
                    // toggle promotion buttons on and select/ remove pieces for possible promotion
                    else if ((Active_Piece.getClass().equals(King.class) == false
                            && Active_Piece.getClass().equals(GoldGeneral.class) == false)                   //++ [fix extendability]
                            && (Active_Piece.is_promoted() == false
                                    && (Active_Piece.getY() > 5 && Active_Piece.isWhite()
                                            || Active_Piece.getY() < 3 && Active_Piece.isBlack()))) {

                        // if piece is dropped do not allow promotion on same turn

                        if (Active_Piece.is_captured() != true) {
                            PromotionButtonOn = toggle(PromotionButtonOn);
                        }
                        possiblePromotion.add(Active_Piece);

                    } else if (possiblePromotion.contains(Active_Piece)) {

                        if (Active_Piece.is_promoted() == false) {
                            PromotionButtonOn = toggle(PromotionButtonOn);
                        }

                        possiblePromotion.remove(Active_Piece);

                    }

                    // if active piece was dropped change captured status

                    if (Active_Piece.is_captured() == true) {
                        Active_Piece.captured(false);
                    }

                    // end turn, add hints for other players turn and update protected pieces status

                    Active_Piece = null;

                    moveCounter++;

                    toggleTurn();

                    HintsDisplayed = toggle(HintsDisplayed);

                    updateBoardStatus();

                }

            } else {

                // detect promote button pressed
                if (PromotionButtonOn == true && Clicked_Column == 15 && Clicked_Row == 8) {

                    Previous_Peice.promote(true);
                    PromotionButtonOn = toggle(PromotionButtonOn);

                    if (possiblePromotion.contains(Active_Piece)) {
                        possiblePromotion.remove(Active_Piece);
                    }
                    updateBoardStatus();

                }
                // detect do not promote button pressed
                if (PromotionButtonOn == true && Clicked_Column == 16 && Clicked_Row == 8) {
                    PromotionButtonOn = toggle(PromotionButtonOn);

                }
            }

            drawBoard();
        }
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

    private boolean toggle(boolean DisplayOn) {

        return (DisplayOn)? false:true;
    }

    private void toggleTurn(){

        whites_turn= (whites_turn)?false:true;
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

    public ArrayList<Square> getSquaresBetween(int x1, int y1, int x2, int y2) {

        String direction = Piece.getMoveDirection(x2 ,y2 ,x1, y1);

        ArrayList<Square> SquaresBetween = new ArrayList<Square>();

        int ChangeInX = 0;
        int ChangeInY = 0;

        int NumOfSqauresBetween= (direction.contains("E") || direction.contains("W")) ? Math.abs(x1 - x2) : Math.abs(y1 - y2);
 
        if (direction.contains("N")) {
            ChangeInY = -1;

        }
        if (direction.contains("S")) {
            ChangeInY = +1;

        }
        if (direction.contains("E")) {
            ChangeInX = +1;
        }
        if (direction.contains("W")) {
            ChangeInX = -1;
        }

        for (int square = 1; square < NumOfSqauresBetween; square++) {
            x2 = x2 + ChangeInX;
            y2 = y2 + ChangeInY;
            SquaresBetween.add(new Square(x2, y2));
        }

        if (SquaresBetween.isEmpty()) {
            SquaresBetween = null;
        }

        return SquaresBetween;
    }

    public int getROWS() {
        return ROWS;
    }

    public int getCOLS() {
        return COLS;
    }

    public ArrayList<Piece> getWhitePieces() {
        return White_Pieces;
    }

    public ArrayList<Piece> getBlackPieces() {
        return Black_Pieces;
    }

    public ArrayList<Piece> getPiecesCheckingWhiteKing() {
        return PiecesCheckingWhiteKing;
    }

    public ArrayList<Piece> getPiecesCheckingBlackKing() {
        return PiecesCheckingBlackKing;
    }

    public boolean whiteIschecked() {
        return WhiteIsChecked;
    }

    public boolean blackIschecked() {
        return BlackIsChecked;
    }
    
    public ArrayList<Square> getWhiteAttackingSquares() {
        return whiteAttackingSquares;
    }

    public ArrayList<Square> getBlackAttackingSquares() {
        return blackAttackingSquares;
    }

    public Piece getKing(ArrayList<Piece> Pieces) {
        for (Piece piece : Pieces) {
            if (piece.getClass().equals(King.class)) {
                return piece;
            }
        }
        return null;
    }
}

