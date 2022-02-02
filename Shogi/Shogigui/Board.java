package Shogigui;

import Shogigui.pieces.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;

public class Board extends JComponent {

    private int moveCounter = 0; // ++
    private boolean whites_turn;

    public final int Square_Width = 65;

    public ArrayList<Piece> White_Pieces;
    public ArrayList<Piece> Black_Pieces;
    public ArrayList<Piece> All_Pieces;

    private ArrayList<Piece>capturablePieces;
    private JLabel capturablePiecesTxtHint;

    private ArrayList<Arrow> arrows;
    private ArrayList<Arrow> getOutOfCheckArrows;
    private ArrayList<Arrow> checkmateArrows;

    private ImageFactory Background_Image;
    private ArrayList<ImageFactory> Static_Images;
    private ArrayList<ImageFactory> Piece_Images; // ++ [use image factory interface...?]
    private ImageFactory Tutorial_Image;
    private ArrayList<ImageFactory> Arrow_Images;

    public ArrayList<ImageFactory> cbImages;

    public Piece Active_Piece;
    private Piece Previous_Peice;
    private ArrayList<Piece> possiblePromotion = new ArrayList<Piece>();
    private boolean PromotionButtonOn = false;

    private final int ROWS = 9;
    private final int COLS = 9;

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
    private final String background_image_file_path= board_images_file_path + "Background.png";
    private final String red_background_image_file_path= board_images_file_path + "red_background.png";
    private final String menu_button= board_images_file_path + "menu_button.png";

    private final String board_file_path = board_images_file_path + "board.png";
    private final String active_square_file_path = board_images_file_path + "active_square.png";
    private final String good_square_file_path = board_images_file_path + "good_square.png";
    private final String bad_square_file_path = board_images_file_path + "bad_square.png";
    private final String checkmate_square_file_path = board_images_file_path + "checkmate_square.png";
    private final String drop_square_file_path = board_images_file_path + "drop_square.png";
    private final String promote_buttons_file_path = board_images_file_path + "promote_buttons.png";
    private final String white_pieces_file_path = board_images_file_path + "white_pieces" + File.separator;
    private final String black_pieces_file_path = board_images_file_path + "black_pieces" + File.separator;
    private final String promoted_white_pieces_file_path = white_pieces_file_path + "Promoted" + File.separator;
    private final String promoted_black_pieces_file_path = black_pieces_file_path + "Promoted" + File.separator;

    private final String default_tutorial = board_images_file_path + "controls.png";
    private final String tutorial_white_pieces_file_path = white_pieces_file_path + "Tutorial" + File.separator;
    private final String tutorial_black_pieces_file_path = black_pieces_file_path + "Tutorial" + File.separator;
    private final String promoted_tutorial_white_pieces_file_path = white_pieces_file_path + "Tutorial" + File.separator+ "Promoted" + File.separator;
    private final String promoted_tutorial_black_pieces_file_path = black_pieces_file_path + "Tutorial" + File.separator+ "Promoted" + File.separator;

    private final String check_file_path = board_images_file_path + "Check.png";
    private final String white_checkmate_file_path = board_images_file_path + "white_check_mate.png";
    private final String black_checkmate_file_path = board_images_file_path + "black_check_mate.png";

    private boolean PlayerIsWhite; 
    private boolean HintsOn;
    private boolean TutorialOn; 

    public InGameMenu inGameMenu;
    public Boolean InGameMenuIsDisplay=false;
    public ArrayList<ImageFactory> InGameMenu_Images;

    private ArrayList<ArrayList<Piece>> BoardStates;

    public ArrayList<Square> grid= new ArrayList<Square>();

    private BoardFrame boardFrame;

    private ArrayList<Piece> customPieces;


    public class Square {
        public int x;
        public int y;

        public Square(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public boolean isContainedWithin(ArrayList<Square> Squares) {

            if (Squares!=null){
                for (Square square: Squares){

                    if (square.x==this.x&&square.y==this.y){
                        return true;
                    }
                }
            }

            return false;
        }
    }

    public void initGrid(){

        for (int x = 0; x < ROWS; x++) {
            for (int y = 0; y < COLS; y++) {
                
                grid.add(new Square(x,y));  // ++
            }
        }
    }

    public Square getSquare(int x,int y){

        for (Square square: grid){
            if (square.x==x && square.y==y){
                return square;
            }
        }
        return null;
    }

    

    // ++
    // [move pieces with least moves range to front of list to improve efficiancy
    // when searching through list and checking pieces moves when checking for
    // protected pieces...]
    // ++

    public void initPieces() {

        if (customPieces==null){

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

        Black_Pieces.add(new Rook(8, 6, false, "Rook.png", this, false));
        
        All_Pieces.addAll(White_Pieces);
        All_Pieces.addAll(Black_Pieces);
        }
        else{
            
            All_Pieces.addAll(customPieces);

            for (Piece piece: All_Pieces){

                piece.setBoard(this);

                if (piece.isWhite()){
                    White_Pieces.add(piece);
                }
                else{
                    Black_Pieces.add(piece);
                }
            }
        }

        try {
            addBoardState();
        } catch (CloneNotSupportedException e1) {
            e1.printStackTrace();
        }

        updateBoardStatus();

    }

    public Board(Boolean PlayerIsWhite, Boolean HintsOn, Boolean TutorialOn, BoardFrame boardFrame, ArrayList<Piece> customPieces, boolean PlayersTurn) {

        this.PlayerIsWhite = PlayerIsWhite;
        this.HintsOn = HintsOn;
        this.TutorialOn = TutorialOn;
        this.boardFrame=boardFrame;
        this.customPieces=customPieces;

        moveCounter=(PlayersTurn)?2:1;  // ++ with customize menu add custom move count

        White_Pieces = new ArrayList<Piece>();
        Black_Pieces = new ArrayList<Piece>();
        All_Pieces = new ArrayList<Piece>();

        initGrid();

        inGameMenu= new InGameMenu(this);
        BoardStates= new ArrayList<ArrayList<Piece>>();

        Tutorial_Image = new ImageFactory(default_tutorial, Square_Width * 9, Square_Width * 2.6);

        PiecesCheckingWhiteKing = new ArrayList<Piece>();
        PiecesCheckingBlackKing = new ArrayList<Piece>();

        capturablePiecesTxtHint= new JLabel();
        capturablePiecesTxtHint.setBounds(25,520, 750,200);  
        capturablePiecesTxtHint.setFont(new Font("Segoe Script", Font.PLAIN, 15));
        capturablePiecesTxtHint.setForeground(new Color(57, 208, 255));

        initPieces();

        this.setBackground(new Color(0, 0, 0));
        //this.setPreferredSize(new Dimension(1100, 650)); // to use capturetxt
        this.setPreferredSize(new Dimension(1100, 580));// board: 580x580
        this.setMinimumSize(new Dimension(100, 100));
        this.setMaximumSize(new Dimension(1000, 1000));

        this.addMouseListener(new MouseListener(this));

        this.setVisible(true);
        this.requestFocus();

        drawBoard();
    }

    public void drawBoard() {

        
        Static_Images = new ArrayList<ImageFactory>();
        Piece_Images = new ArrayList<ImageFactory>();
        Arrow_Images= new ArrayList<ImageFactory>();
        
        this.remove(capturablePiecesTxtHint); 

        Tutorial_Image = (!(checkForCheckMate()||whiteIschecked() || blackIschecked()||PromotionButtonOn))?new ImageFactory(default_tutorial, Square_Width * 9, Square_Width * 2.6):null;
        
        // add background 

        Background_Image= new ImageFactory(background_image_file_path, 0, 0);

        // add menu button

        Static_Images.add(new ImageFactory(menu_button, 14.5*Square_Width, 0.1*Square_Width));

        // add menu if menu button is pressed

        InGameMenu_Images = (InGameMenuIsDisplay)?inGameMenu.getImages():null;

        // add board grid

        Static_Images.add(new ImageFactory(board_file_path, 0, 0));

        // add active square if a piece is clicked and tutorial image corresponding to piece (if tutorials are on)

        if (Active_Piece != null) {
            Static_Images.add(new ImageFactory(active_square_file_path, Square_Width * Active_Piece.getX(),
                    Square_Width * Active_Piece.getY()));

            if (TutorialOn && checkForCheckMate()==false && PromotionButtonOn==false){        
                
                String tutorial_file_path=default_tutorial;

                if (/*PlayerIsWhite &&*/Active_Piece.isWhite()){
                    tutorial_file_path= (!Active_Piece.is_promoted())?tutorial_white_pieces_file_path+Active_Piece.getFilePath():promoted_tutorial_white_pieces_file_path+Active_Piece.getFilePath();
                }
                else if ((/*PlayerIsWhite==false &&*/Active_Piece.isBlack())){
                    tutorial_file_path= (!Active_Piece.is_promoted())?tutorial_black_pieces_file_path+Active_Piece.getFilePath():promoted_tutorial_black_pieces_file_path+Active_Piece.getFilePath();
                }

                Tutorial_Image= new ImageFactory(tutorial_file_path, Square_Width * 9, Square_Width * 2.6);
            }
        }

        // add pieces / update piece positions

        String piece_file_path;

        for (Piece piece : All_Pieces) {
            int COL = piece.getX();
            int ROW = piece.getY();
            if (piece.isWhite()){
                piece_file_path= (piece.is_promoted())?promoted_white_pieces_file_path + piece.getFilePath(): white_pieces_file_path + piece.getFilePath();
            }
            else{
                piece_file_path= (piece.is_promoted())?promoted_black_pieces_file_path + piece.getFilePath():black_pieces_file_path + piece.getFilePath(); 
            }
            Piece_Images.add(new ImageFactory(piece_file_path, Square_Width * COL, Square_Width * ROW));
        }

        // add check mate display (when check mated)

        if (checkForCheckMate()) {

            String checkmate_file_path= whiteIschecked()?white_checkmate_file_path:black_checkmate_file_path;

            Static_Images.add(new ImageFactory(checkmate_file_path, Square_Width * 9, Square_Width * 2.6));
        }

           // add check display (when checked)

        else if (whiteIschecked() || blackIschecked()) {

            Background_Image= new ImageFactory(red_background_image_file_path, 0, 0);

            if (Active_Piece==null && PromotionButtonOn == false){
                Static_Images.add(new ImageFactory(check_file_path, Square_Width * 9, Square_Width * 2.6));
            }
        }

        //  add promotion buttons if promotion is availiable

        if (PromotionButtonOn == true && checkForCheckMate()==false){
                
            if (Active_Piece==null){
                Static_Images.add(new ImageFactory(promote_buttons_file_path, Square_Width * 9, Square_Width * 2.6));
            }   
        }

        // ADD HINTS / ASSISTS

        // add / update hints (if player hints are turned on)

        if (this.HintsOn == true && checkForCheckMate()==false) {
            
            /*if ((whites_turn&&PlayerIsWhite) || ((whites_turn==false&&PlayerIsWhite==false))) {*/  // ++ show hints only when players turn?

                // (if a king is checked) add get out of check hints

                if ((whiteIschecked() && PlayerIsWhite) || (blackIschecked() && PlayerIsWhite==false)) {

                    // ++ [currently hints show every possible moves out of check, not best moves] 
                    // ++ [change get out of check hints so that if king can move out of check this 
                    // move over pieces blocking check by moving into capturable position] ++

                    ArrayList<Square> getOutOfCheckMoveHints = new ArrayList<Square>();
                    ArrayList<Square> getOutOfCheckDropHints = new ArrayList<Square>();

                    // get squares that can stop the check if a piece can move or be dropped into one of these
                    // squares add corresponding image to this to give player hint to get out of check

                    if (getOutOfcheckMoves != null) {

                        for (Square moveSquare : getOutOfcheckMoves) {
                            Static_Images.add(new ImageFactory(good_square_file_path,
                                    Square_Width * moveSquare.x, Square_Width * moveSquare.y));
                        }
                        for (Arrow arrow: getOutOfCheckArrows){
                            Arrow_Images.add(new ImageFactory(arrow.getFilePath(),
                                Square_Width * arrow.getX(), Square_Width * arrow.getY()));
                        }

                    }

                    if (getOutOfCheckDrops != null) {

                        for (Square dropSquare : getOutOfCheckDrops) {
                            Static_Images.add(new ImageFactory(drop_square_file_path,
                                    Square_Width * dropSquare.x, Square_Width * dropSquare.y));
                        }
                    }
                }
                // (if king is not checked) add capturable pieces hints

                // ++ [currently hints show every possible capturable piece, not the best piece to capture] 
                // ++ [change hints so that if their are multiple captureable pieces give user hint to capture most valueable piece]

                else if (capturablePieces != null) {

                    for (Piece captureablePiece : capturablePieces) {

                        if (((PlayerIsWhite && captureablePiece.isWhite()==false) || (PlayerIsWhite==false && captureablePiece.isWhite()==true)) ){

                            Static_Images.add(new ImageFactory(good_square_file_path,
                                Square_Width * captureablePiece.getX(), Square_Width * captureablePiece.getY()));
                        }
                        
                        if (((PlayerIsWhite && captureablePiece.isWhite()) || (PlayerIsWhite==false && captureablePiece.isWhite()==false))){
                                Static_Images.add(new ImageFactory(bad_square_file_path,
                                    Square_Width * captureablePiece.getX(), Square_Width * captureablePiece.getY()));
                        }
                    }
                    if (InGameMenuIsDisplay==false || PromotionButtonOn==false){
                        this.add(capturablePiecesTxtHint); 
                    }

                    for (Arrow arrow: arrows){
            
                        Arrow_Images.add(new ImageFactory(arrow.getFilePath(),
                            Square_Width * arrow.getX(), Square_Width * arrow.getY()));
                    }
                }
                boolean possibleCheckMate= (PlayerIsWhite)?checkForCheckMateMoves(White_Pieces):checkForCheckMateMoves(Black_Pieces);

                if (possibleCheckMate){
                    for (Square checkMateSquare: checkMateMoves){       // ++ show user potential check mates moves against them
                        Static_Images.add(new ImageFactory(checkmate_square_file_path,
                            Square_Width * checkMateSquare.x, Square_Width * checkMateSquare.y));
                    }
                    for (Arrow arrow: checkmateArrows){
                        Arrow_Images.add(new ImageFactory(arrow.getFilePath(),
                            Square_Width * arrow.getX(), Square_Width * arrow.getY()));
                    }
                }
        }

        // add check mate hints 

        // ++ [...]

        this.repaint();
    }

    public void updateAttackingSquares() {

        blackAttackingSquares = new ArrayList<Square>();
        whiteAttackingSquares = new ArrayList<Square>();

        for (int x = 0; x < ROWS; x++) {
            for (int y = 0; y < COLS; y++) {

                for (Piece piece : All_Pieces) {

                    if (piece.canMove(x, y) && piece.is_captured() == false) {

                        if (piece.isWhite()) {
                            whiteAttackingSquares.add(getSquare(x, y));
                        } else {
                            blackAttackingSquares.add(getSquare(x, y));
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

        for (Piece piece : All_Pieces) {
            piece.setAttackers(new ArrayList<Piece>());
                   piece.setDefenders(new ArrayList<Piece>());
        }

        for (int x = 0; x < ROWS; x++) {
            for (int y = 0; y < COLS; y++) {

                for (Piece piece : All_Pieces) {

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

        for (Piece piece : All_Pieces) {
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

                if (attackerDefender.getMovementRange(attackerDefender.getX(),attackerDefender.getY(),direction) != null) {

                    for (Square Square : attackerDefender.getMovementRange(attackerDefender.getX(),attackerDefender.getY(),direction)) {

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

                ArrayList<Square> pMovementRange = opposingPiece.getMovementRange(opposingPiece.getX(),opposingPiece.getY(),movementDirection);

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

        getOutOfCheckArrows = new ArrayList<Arrow>();

        for (Piece piece : pieces) {

            for (int x = 0; x < ROWS; x++) {
                for (int y = 0; y < COLS; y++) {

                    if (piece.canBeDroppedToStopCheck(x, y) && piece.is_captured()/*&& piece.canMove(x, y)*/) {

                        getOutOfCheckDrops.add(new Square(x, y));
                    }
                    if (piece.canMoveToStopCheck(x, y) && piece.canMove(x, y)) {

                        getOutOfcheckMoves.add(new Square(x, y));

                        getOutOfCheckArrows.add(new Arrow(Piece.getMoveDirection(piece.getX(), piece.getY(), x, y), piece.getX(), piece.getY(), "black"));
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
                                             
    private void updateCaptureablePieces() {       // [BUG] doesnt show capturable pieces at start of game  // ++ make more efficiant and optimize   // ++ test  // ++ remove duplicate capturable squares?
        
        capturablePieces = new ArrayList<Piece>();              // ++ check xray defeners and attackers, depth 2 

        // check for capturable pieces, add to list and update Jlabel text accordingly

        for (int x = 0; x < ROWS; x++) {
            for (int y = 0; y < COLS; y++) {

                // check if piece is unprotected and can be captured 
                if (getPiece(x, y) != null) {

                    getPiece(x, y).resetCaptureWith();
                    
                    if (getPiece(x, y).getAttackers().size() >0 && getPiece(x, y).is_protected()==false){
                            
                        capturablePieces.add(getPiece(x, y));

                    }
                    // check if lower value piece can capture highter value piece (sacrafice)

                    if((getPiece(x, y).getAttackers().size()>0)){

                        for (Piece attacker : getPiece(x,y).getAttackers()){

                            if (attacker.getValue()<getPiece(x, y).getValue()){
                                
                                capturablePieces.add(getPiece(x, y));

                                getPiece(x, y).addCaptureWith(attacker);
                            }
                        }
                    }
                    // calculate capturable pieces depending on exchanges of pieces dependent on piece value    

                    // (xray defenders and attackers (depth 1))

                    Square square= new Square(x,y);

                    int xRayAttackers=0;

                    for(Piece piece:getPiece(x, y).getAttackers()){

                        for (Piece piece2:piece.getDefenders()){
                            
                            if ((getPiece(x, y).getDefenders().contains(piece2)==false) && piece2.getMovementRange(x, y, Piece.getMoveDirection(piece2.getX(), piece2.getY(), x, y))!=null && square.isContainedWithin(piece2.getMovementRange(piece2.getX(), piece2.getY(), Piece.getMoveDirection(piece2.getX(), piece2.getY(), x, y)))){
                                xRayAttackers=xRayAttackers+1;
                            }
                        }
                    }

                    int xRayDefeners=0;

                    for(Piece piece:getPiece(x, y).getDefenders()){

                        for (Piece piece2:piece.getDefenders()){
                            
                            if ((getPiece(x, y).getDefenders().contains(piece2)==false) && piece2.getMovementRange(x, y, Piece.getMoveDirection(piece2.getX(), piece2.getY(), x, y))!=null && square.isContainedWithin(piece2.getMovementRange(piece2.getX(), piece2.getY(), Piece.getMoveDirection(piece2.getX(), piece2.getY(), x, y)))){
                                xRayDefeners=xRayDefeners+1;
                            }
                        }
                    }

                    if ((getPiece(x, y).getAttackers().size() + xRayAttackers > getPiece(x, y).getDefenders().size() + xRayDefeners) && getPiece(x, y).getDefenders().size() + xRayDefeners > 0){

                        for (Piece attacker : getPiece(x,y).getAttackers()){

                            if (attacker.getValue()<=getPiece(x, y).getValue()){
                                
                                capturablePieces.add(getPiece(x, y));

                                getPiece(x, y).addCaptureWith(attacker);

                            }
                        }   
                    }
                }
            }
        }

        if (capturablePieces.isEmpty()) {
            capturablePieces = null;
        }
        else{
            updateCaptureableTXT();
            updateCaptureableArrowHints();
        }
    }

    public void updateCaptureableTXT(){  // ++ [remove or make more readable/ user friendly]  ++ chnage number coordinates to letters 

        String capturablePiecesTxt= "";  
        String capture="CAPTURE: ";
        String captureWith;
        String protect=" <br/> PROTECT :";
        String protectFrom;
          
        for (Piece capturablePiece: capturablePieces){

            if ((capturablePiece.isWhite()&&PlayerIsWhite) || (capturablePiece.isWhite()==false&&PlayerIsWhite==false)){
                protect = protect + " (" + capturablePiece.getX() + " , " + capturablePiece.getY() + " ) ";

                protectFrom=" from: ";
                for (Piece attacker: capturablePiece.getAttackers()){

                    protectFrom= protectFrom + " ( " + attacker.getX() + " , " + attacker.getY()+" ), ";
                }
                protect = protect + protectFrom;
            }
            else{
                capture = capture+ "         " + " (" + capturablePiece.getX() + " , " + capturablePiece.getY() + " ) ";

                captureWith = " with: ";
                if (capturablePiece.getCaptureWith().isEmpty()==false){

                    for (Piece attacker: capturablePiece.getCaptureWith()){

                        captureWith= captureWith + " ( " + attacker.getX() + " , " + attacker.getY()+" ) ";
                    }
            
                }
                else{
                    for (Piece attacker: capturablePiece.getAttackers()){

                        captureWith= captureWith + " ( " + attacker.getX() + " , " + attacker.getY()+" ) ";
                    }
                }        
                capture = capture + captureWith;
               
                capture= capture+", " /*+"<br/>"*/;
                
            }
        }

        if  (capture.equals("CAPTURE: ")||protect.equals(" <br/> MOVE :")){
            capturablePiecesTxt = (capture.equals("CAPTURE: "))?"<html>"+protect+"</html>":"<html>"+capture+"</html>";
        }
        else{
            capturablePiecesTxt= "<html>"+capture+protect+"</html>";
        }
        
        //capturablePiecesTxt = (capturablePiecesTxt.length()>0)?capturablePiecesTxt.substring(0, capturablePiecesTxt.length() -1):""; //remove comma at end of txt

        capturablePiecesTxtHint.setText(capturablePiecesTxt);
    }


    public void updateCaptureableArrowHints(){ 

        arrows= new ArrayList<Arrow>();
          
        for (Piece capturablePiece: capturablePieces){

            // add arrows (valid moves) to pieces to possible moves away from opposing piece 

            if ((capturablePiece.isWhite()&&PlayerIsWhite) || (capturablePiece.isWhite()==false&&PlayerIsWhite==false)){
 
                for (Piece attacker: capturablePiece.getAttackers()){

                    String DirectionTowardAttacker="";
                    String DirectionAwayAttacker="";
                    
                    ArrayList<String> allDirections= new ArrayList<String>();
                    allDirections.add("N"); allDirections.add("NE"); allDirections.add("NW"); allDirections.add("S"); allDirections.add("SE"); allDirections.add("SW"); allDirections.add("W"); allDirections.add("E");

                    DirectionTowardAttacker= Piece.getMoveDirection(capturablePiece.getX(), capturablePiece.getY(),attacker.getX(), attacker.getY());
                    DirectionAwayAttacker= Piece.getMoveDirection(attacker.getX(), attacker.getY(), capturablePiece.getX(), capturablePiece.getY());

                    if (attacker.getClass()==Rook.class || attacker.getClass()==Lance.class || attacker.getClass()==Bishop.class){  // ++ [fix extendability -for ranged pieces]

                        allDirections.remove(DirectionTowardAttacker);
                        allDirections.remove(DirectionAwayAttacker);
                    }
                    else{
                        allDirections.remove(DirectionTowardAttacker);
                    }

                    for (String Direction: allDirections){
                    
                        if (capturablePiece.getPossibleDisplacementRange(capturablePiece.getX(), capturablePiece.getY(), Direction)!=null){

                            // check move in direction is safe

                            boolean moveIsSafe=true;

                            for (Square possibleMove : capturablePiece.getPossibleDisplacementRange(capturablePiece.getX(), capturablePiece.getY(), Direction)){
                                
                                int currPosX= capturablePiece.getX();
                                int currPosY= capturablePiece.getY();

                                capturablePiece.setX(possibleMove.x);
                                capturablePiece.setY(possibleMove.y);

                                updatePiecesAttackersAndDefenders();

                                if (capturablePiece.getAttackers().size() > capturablePiece.getDefenders().size()){

                                    moveIsSafe=false;
                                }
                                else{

                                    moveIsSafe=true;

                                    for (Piece Attacker: capturablePiece.getAttackers()){
                                        if (Attacker.getValue()<=capturablePiece.getValue()){
                                            moveIsSafe=false;
                                        }
                                    }
                                }

                                capturablePiece.setX(currPosX);
                                capturablePiece.setY(currPosY);

                                updatePiecesAttackersAndDefenders();

                            }
                            if (moveIsSafe){
                                arrows.add(new Arrow(Direction,capturablePiece.getX(),capturablePiece.getY(), "black"));
                            }
                        }
                    }   
                }

            }
            else{
                // add arrows to pieces toward possible opposing piece capture 

                if (capturablePiece.getCaptureWith().isEmpty()==false){

                    for (Piece attacker: capturablePiece.getCaptureWith()){

                        arrows.add(new Arrow(Piece.getMoveDirection(attacker.getX(), attacker.getY(), capturablePiece.getX(), capturablePiece.getY()),attacker.getX(),attacker.getY(),"green"));
                    }
                }
                else{
                    for (Piece attacker: capturablePiece.getAttackers()){

                        arrows.add(new Arrow(Piece.getMoveDirection(attacker.getX(), attacker.getY(), capturablePiece.getX(), capturablePiece.getY()),attacker.getX(),attacker.getY(),"green"));
                    }
                }        
            }
        }
    }
    

    public void updateBoardStatus() {

        All_Pieces.clear();
        All_Pieces.addAll(White_Pieces);
        All_Pieces.addAll(Black_Pieces); // ++ nessessary?

        // set turn
        whites_turn = (moveCounter % 2 == 1)?false:true;
        
        updatePiecesBlockingCheck();
        updateAttackingSquares();               // <- ++ fix method?
        updatePiecesAttackersAndDefenders();
        updateProtectedPieces();
        updateCaptureablePieces();

        if (checkForCheck(getKing(White_Pieces))/*||whites_turn*/){
            updateGetOutOfCheckMovesAndDrops(White_Pieces);
        }
        else if (checkForCheck(getKing(Black_Pieces))/*||whites_turn==false*/){
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
            if (piece.canMove(king.getX(), king.getY())&& piece.is_captured()==false) {

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

    public boolean checkForCheckMate(){
        return ((getOutOfcheckMoves==null) && (whiteIschecked() || blackIschecked()))?true:false;
    }

                                                                            // ++ TEST 
    public boolean checkForCheckMateMoves(ArrayList<Piece> pieces) {  //++ add promotion checkmates 

        checkMateMoves = new ArrayList<Square>();   
        checkmateArrows= new ArrayList<Arrow>();                // check if piece that blocks checkmate can be taken resulting in next move checkmate


        if (getOpposingKing(pieces).getMovementRange()==null || getOpposingKing(pieces).getMovementRange().size()<=4){ // check if a move can block more than 4 king moves and check for check mate?

            for (Piece piece: pieces){

                if (piece.getMovementRange()!=null){         

                    for (Square possibleMove: piece.getMovementRange()){

                        if (isCheckMateMove(piece, possibleMove.x,possibleMove.y) && !(piece.getClass()==Pawn.class && piece.is_captured())){  //check pawn is not being dropped into checkmate  ++ fix extendability
                            checkMateMoves.add(possibleMove);
                            checkmateArrows.add(new Arrow(Piece.getMoveDirection(piece.getX(), piece.getY(), possibleMove.x, possibleMove.y),piece.getX(),piece.getY(),"gold")); // change to gold arrow
                        }
                    }
                }
            }
        }

        if (checkMateMoves.isEmpty()){
            checkMateMoves=null;
            return false;
        }
        else{
            return true;
        }
    }

    public boolean isCheckMateMove(Piece piece, int x, int y){

        boolean isCheckMateMove;

        int currentXPos= piece.getX();
        int currentYPos= piece.getY();

        Piece king;
        ArrayList<Square> defendingSquares;
  
        if (piece.isBlack()){
            king= getKing(White_Pieces);
            defendingSquares= whiteAttackingSquares;
        }
        else{
            king= getKing(Black_Pieces);
            defendingSquares= blackAttackingSquares;
        }
        
        boolean moveIsSafe=false;

        if (getPiece(x,y)!=null && ((piece.isWhite()&&getPiece(x, y).isBlack())||(piece.isBlack()&&getPiece(x , y).isWhite()))){

            int Defenders=0;

            for (Square defendingSquare: defendingSquares){

                if (defendingSquare.x==x && defendingSquare.y==y){
                    Defenders=Defenders+1;
                }
                if (Defenders>1){
                    moveIsSafe=true;
                    break;
                }
            }
        }
        else{
            moveIsSafe=true;
        }

        // xray

        for (Piece protector: piece.getDefenders()){

            if (protector.getMovementRange(protector.getX(), protector.getY(), Piece.getMoveDirection(protector.getX(), protector.getY(), king.getX(), king.getY()))!=null){

                for (Square square: protector.getMovementRange(protector.getX(), protector.getY(), Piece.getMoveDirection(protector.getX(), protector.getY(), king.getX(), king.getY()))){

                    if (square.x==x && square.y==y){
                        moveIsSafe=true;
                    }
                }
            }
        }

        piece.setX(x);
        piece.setY(y);

        boolean moveIsAdrop=false;

        if (piece.is_captured()){ // ++ test
            piece.captured(false);
            moveIsAdrop=true;
        }

        updateBoardStatus();

        if (checkForCheckMate() &&  moveIsSafe && !(x==king.getX() && y==king.getY())){
            isCheckMateMove=true;
        }
        else{
            isCheckMateMove=false;
        }

        piece.setX(currentXPos);
        piece.setY(currentYPos);

        if (moveIsAdrop){
            piece.captured(true);
        }

        updateBoardStatus();

        return isCheckMateMove;

    }

    // removes duplicate squares from square array 
    public ArrayList<Square> removeDups(ArrayList<Square> Squares){

        ArrayList<Square> newSquares= new ArrayList<Square>();
        ArrayList<Square> tempSquares= new ArrayList<Square>();

        for (Square square: Squares){
            newSquares.add(getSquare(square.x, square.y));
        }
        for (Square square: newSquares){
            if (tempSquares.contains(square)==false){
                tempSquares.add(getSquare(square.x, square.y));
            }
        }
        return tempSquares;                  
    }

    public ArrayList<Square> getSquaresThatBlockCheck(Piece king) { // ++ [is tripple check possible in shogi? can you block a double check?...] test method...

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
        /*
        else if (PiecesCheckingKing.size()==2){

            // double check: only add squares to blocking squares if the squares block both of the checking pieces paths to the king 

            for (Square squareBetweenP1AndKing : getSquaresBetween(PiecesCheckingKing.get(0).getX(), PiecesCheckingKing.get(0).getY(), king.getX(), king.getY())){

                for (Square squareBetweenP2AndKing : getSquaresBetween(PiecesCheckingKing.get(1).getX(), PiecesCheckingKing.get(1).getY(), king.getX(), king.getY())){
                    
                    if (squareBetweenP1AndKing.x == squareBetweenP2AndKing.x && squareBetweenP1AndKing.y==squareBetweenP2AndKing.y){

                        blockingSquares.add(squareBetweenP1AndKing);
                    }
                }
            }
        }*/

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

    class MouseListener extends MouseAdapter {   // ++ make neater

        Board board;
    
        public MouseListener(Board board){
            this.board=board;
        }
        @Override
        public void mousePressed(MouseEvent e) {
           board.mousePressed(e);
        }
    }

        public void mousePressed(MouseEvent e) {
            int mouse_X = e.getX();
            int mouse_Y = e.getY();
            int Clicked_Column = mouse_X / Square_Width;
            int Clicked_Row = mouse_Y / Square_Width;

            // ++ fix drops bug?

            Piece Clicked_Piece = getPiece(Clicked_Column, Clicked_Row);

            // in game menu

            if (mouse_X > 14.5*Square_Width && mouse_Y< 0.8*Square_Width){
                InGameMenuIsDisplay=true;
            }

            if (InGameMenuIsDisplay){
                inGameMenu.detectButtonPress(mouse_X,mouse_Y);
            }
            else{
                 
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

                        if (Active_Piece.is_captured() == false) {
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

                    // end turn, update move counter and save and update board status

                    Active_Piece = null;

                    moveCounter=moveCounter+1;  
                    

                    try {
                        addBoardState();
                    } catch (CloneNotSupportedException e1) {
                        e1.printStackTrace();
                    }

                    updateBoardStatus();
                }

            } else {

                // detect promote button pressed
                if (PromotionButtonOn == true && mouse_X > 692 && mouse_X < 992 && mouse_Y > 190 && mouse_Y < 290) {  // ++ [fix button extendability]
 
                    Previous_Peice.promote(true);
                    PromotionButtonOn = toggle(PromotionButtonOn);

                    if (possiblePromotion.contains(Active_Piece)) {
                        possiblePromotion.remove(Active_Piece);
                    }

                    updateBoardStatus();

                }
                // detect do not promote button pressed
                if (PromotionButtonOn == true && mouse_X > 692 && mouse_X < 992 && mouse_Y > 297 && mouse_Y < 399) {
                    PromotionButtonOn = toggle(PromotionButtonOn);
                }
            }
        }
            
            
            drawBoard();

        }
    

    public void addBoardState() throws CloneNotSupportedException{

        // clone current pieces and return state of board

        ArrayList<Piece> newBoardState= new ArrayList<Piece>();

        for (Piece piece: All_Pieces){

            newBoardState.add((Piece) piece.clone());
        }
        
        BoardStates.add(newBoardState);
    }

    public void revertLastMove(){ // ++ [bug] when user reverts move, moves again and reverts that move...  doesnt work when reverting promotion move 

        if (BoardStates!=null && moveCounter!=0){

            White_Pieces.clear();
            Black_Pieces.clear();

            // remove latest state

            BoardStates.remove(BoardStates.size()-1); 

            // set pieces to pieces within state before latest state
        
            for (Piece piece: (BoardStates.get(BoardStates.size()-1))){

                if (piece.isWhite()){
                    White_Pieces.add(piece);
                }
                else{
                    Black_Pieces.add(piece);
                }
            }

            moveCounter=moveCounter-1;
            
            updateBoardStatus();
            drawBoard();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        addBackground(g2);
        addImages(g2);
    }

    private void addBackground(Graphics2D g2) {

        g2.setColor(getBackground());
        g2.fillRect(0, 0, getWidth(), getHeight());
        Background_Image.drawImage(g2);

    }

    private void addImages(Graphics2D g2) {

        ArrayList<ImageFactory> All_Images= new ArrayList<ImageFactory>();

        All_Images.addAll(Static_Images);
        All_Images.addAll(Piece_Images);
        All_Images.addAll(Arrow_Images); 
        if (TutorialOn && Tutorial_Image!=null){
            All_Images.add(Tutorial_Image);
        }
        if (InGameMenuIsDisplay){
            All_Images.addAll(InGameMenu_Images);
        }

        if (cbImages!=null){
            All_Images.addAll(cbImages);
        }
       
        for (ImageFactory image : All_Images) {
            image.drawImage(g2);
        }
    }

    private boolean toggle(boolean DisplayOn) {

        return (DisplayOn)? false:true;
    }

    // getters and setters...

    public void closeInGameMenu(){
        InGameMenuIsDisplay=false;
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
    public Piece getOpposingKing(ArrayList<Piece> Pieces){

        if (Pieces.equals(White_Pieces)){
            return getKing(Black_Pieces);
        }else{
            return getKing(White_Pieces);
        }
    }
    public BoardFrame getBoardFrame(){
        return boardFrame;
    }
}


